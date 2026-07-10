package com.smartlogistics.userservice.service;

import com.smartlogistics.userservice.domain.entity.*;
import com.smartlogistics.userservice.domain.repository.*;
import com.smartlogistics.userservice.dto.AddressDto.AddressRequest;
import com.smartlogistics.userservice.dto.AddressDto.AddressResponse;
import com.smartlogistics.userservice.dto.DocumentDto.DocumentResponse;
import com.smartlogistics.userservice.dto.EmergencyContactDto.EmergencyContactRequest;
import com.smartlogistics.userservice.dto.EmergencyContactDto.EmergencyContactResponse;
import com.smartlogistics.userservice.dto.PreferenceDto.PreferenceResponse;
import com.smartlogistics.userservice.dto.PreferenceDto.PreferenceUpdateRequest;
import com.smartlogistics.userservice.dto.ProfileDto.BusinessProfileResponse;
import com.smartlogistics.userservice.dto.ProfileDto.BusinessProfileUpdateRequest;
import com.smartlogistics.userservice.dto.ProfileDto.DriverProfileResponse;
import com.smartlogistics.userservice.dto.ProfileDto.DriverProfileUpdateRequest;
import com.smartlogistics.userservice.dto.ProfileDto.FleetOwnerProfileResponse;
import com.smartlogistics.userservice.dto.ProfileDto.FleetOwnerProfileUpdateRequest;
import com.smartlogistics.userservice.dto.ProfilePhotoDto.ProfilePhotoResponse;
import com.smartlogistics.shared.dto.UserDTO.UserResponse;
import com.smartlogistics.shared.dto.UserDTO.UserUpdateRequest;
import com.smartlogistics.userservice.dto.UserSettingsDto.UserSettingsResponse;
import com.smartlogistics.userservice.dto.UserSettingsDto.UserSettingsUpdateRequest;
import com.smartlogistics.userservice.exception.*;
import com.smartlogistics.userservice.mapper.UserMapper;
import com.smartlogistics.userservice.service.kafka.UserEventPublisher;
import com.smartlogistics.userservice.service.storage.StorageService;
import com.smartlogistics.userservice.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserProfileRepository userRepository;
    private final DriverProfileRepository driverRepository;
    private final BusinessProfileRepository businessRepository;
    private final FleetOwnerProfileRepository fleetOwnerRepository;
    private final AddressRepository addressRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final IdentityDocumentRepository documentRepository;
    private final PreferenceRepository preferenceRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String query, Pageable pageable) {
        if (query != null && !query.trim().isEmpty()) {
            return userRepository.searchUsers(query.trim(), pageable).map(userMapper::toUserResponse);
        }
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public void createProfile(com.smartlogistics.userservice.dto.UserRegisteredEvent event) {
        if (userRepository.existsById(event.userId())) {
            log.info("User profile already exists for userId: {}", event.userId());
            return;
        }

        // Validate unique email and phone
        if (userRepository.existsByEmail(event.email())) {
            throw new DuplicateEmailException("Email is already registered: " + event.email());
        }
        if (event.phone() != null && userRepository.existsByPhone(event.phone())) {
            throw new DuplicatePhoneException("Phone number is already registered: " + event.phone());
        }

        UserProfile user = UserProfile.builder()
                .email(event.email())
                .phone(event.phone())
                .status("ACTIVE")
                .build();
        user.setId(event.userId());

        // Initialize default preferences and settings
        Preference preference = Preference.builder()
                .user(user)
                .language("en")
                .theme("LIGHT")
                .build();
        user.setPreference(preference);

        UserSettings settings = UserSettings.builder()
                .user(user)
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .pushNotificationsEnabled(true)
                .twoFactorEnabled(false)
                .build();
        user.setSettings(settings);

        userRepository.save(user);
        log.info("Created user profile for userId: {}", event.userId());
        eventPublisher.publishEvent("UserProfileCreated", event.userId(), event);
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        UserProfile user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.phone() != null && !request.phone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.phone())) {
                throw new DuplicatePhoneException("Phone number is already registered to another user: " + request.phone());
            }
            if (!ValidationUtils.isValidIndianMobile(request.phone())) {
                throw new InvalidDocumentException("Invalid Indian mobile number format: " + request.phone());
            }
            user.setPhone(request.phone());
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        user = userRepository.save(user);
        eventPublisher.publishEvent("ProfileUpdated", id, request);
        return userMapper.toUserResponse(user);
    }

    // --- Addresses CRUD ---

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return addressRepository.findByUser_Id(userId).stream()
                .map(userMapper::toAddressResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse addAddress(UUID userId, AddressRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!ValidationUtils.isValidPinCode(request.zip())) {
            throw new InvalidDocumentException("Invalid PIN code format: " + request.zip());
        }

        Address address = userMapper.toAddress(request);
        address.setUser(user);
        address = addressRepository.save(address);
        
        eventPublisher.publishEvent("ProfileUpdated", userId, "Added Address: " + address.getId());
        return userMapper.toAddressResponse(address);
    }

    @Transactional
    public AddressResponse updateAddress(UUID userId, UUID addressId, AddressRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Address does not belong to this user");
        }

        if (!ValidationUtils.isValidPinCode(request.zip())) {
            throw new InvalidDocumentException("Invalid PIN code format: " + request.zip());
        }

        userMapper.updateAddressFromRequest(request, address);
        address = addressRepository.save(address);
        
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated Address: " + address.getId());
        return userMapper.toAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Address does not belong to this user");
        }

        // Soft Delete
        address.setDeleted(true);
        addressRepository.save(address);
        
        eventPublisher.publishEvent("ProfileUpdated", userId, "Deleted Address: " + addressId);
    }

    // --- Emergency Contacts CRUD ---

    @Transactional(readOnly = true)
    public List<EmergencyContactResponse> getEmergencyContacts(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return emergencyContactRepository.findByUser_Id(userId).stream()
                .map(userMapper::toEmergencyContactResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmergencyContactResponse addEmergencyContact(UUID userId, EmergencyContactRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!ValidationUtils.isValidIndianMobile(request.phone())) {
            throw new InvalidDocumentException("Invalid emergency phone number format: " + request.phone());
        }

        EmergencyContact contact = userMapper.toEmergencyContact(request);
        contact.setUser(user);
        contact = emergencyContactRepository.save(contact);
        
        eventPublisher.publishEvent("ProfileUpdated", userId, "Added Emergency Contact: " + contact.getId());
        return userMapper.toEmergencyContactResponse(contact);
    }

    @Transactional
    public EmergencyContactResponse updateEmergencyContact(UUID userId, UUID contactId, EmergencyContactRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        EmergencyContact contact = emergencyContactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found"));

        if (!contact.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Emergency contact does not belong to this user");
        }

        if (!ValidationUtils.isValidIndianMobile(request.phone())) {
            throw new InvalidDocumentException("Invalid emergency phone number format: " + request.phone());
        }

        userMapper.updateEmergencyContactFromRequest(request, contact);
        contact = emergencyContactRepository.save(contact);
        
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated Emergency Contact: " + contact.getId());
        return userMapper.toEmergencyContactResponse(contact);
    }

    @Transactional
    public void deleteEmergencyContact(UUID userId, UUID contactId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        EmergencyContact contact = emergencyContactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found"));

        if (!contact.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Emergency contact does not belong to this user");
        }

        // Soft Delete
        contact.setDeleted(true);
        emergencyContactRepository.save(contact);
        
        eventPublisher.publishEvent("ProfileUpdated", userId, "Deleted Emergency Contact: " + contactId);
    }

    // --- Driver Profile CRUD ---

    @Transactional
    public DriverProfileResponse updateDriverProfile(UUID userId, DriverProfileUpdateRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.licenseNumber() == null || request.licenseNumber().trim().isEmpty()) {
            throw new InvalidDocumentException("Driving license number is mandatory for driver profile");
        }
        if (request.licenseExpiry() == null || request.licenseExpiry().isBefore(LocalDate.now())) {
            throw new InvalidDocumentException("Valid future license expiry date is mandatory");
        }
        if (request.experienceYears() == null || request.experienceYears() < 0) {
            throw new InvalidDocumentException("Valid experience years is mandatory");
        }

        DriverProfile profile = driverRepository.findByUser_Id(userId)
                .orElse(DriverProfile.builder().user(user).status("PENDING").build());

        profile.setLicenseNumber(request.licenseNumber().trim().toUpperCase());
        profile.setLicenseExpiry(request.licenseExpiry());
        profile.setExperienceYears(request.experienceYears());

        profile = driverRepository.save(profile);
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated Driver Profile");
        return userMapper.toDriverProfileResponse(profile);
    }

    @Transactional
    public DriverProfileResponse verifyDriverProfile(UUID userId, String status) {
        if (!"VERIFIED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status) && !"PENDING".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Invalid verification status. Must be VERIFIED, REJECTED, or PENDING");
        }

        DriverProfile profile = driverRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));

        profile.setStatus(status.toUpperCase());
        profile = driverRepository.save(profile);

        if ("VERIFIED".equalsIgnoreCase(status)) {
            eventPublisher.publishEvent("DriverVerified", userId, profile.getId());
        } else {
            eventPublisher.publishEvent("ProfileUpdated", userId, "Driver Profile Verification: " + status);
        }

        return userMapper.toDriverProfileResponse(profile);
    }

    // --- Business Profile CRUD ---

    @Transactional
    public BusinessProfileResponse updateBusinessProfile(UUID userId, BusinessProfileUpdateRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.companyName() == null || request.companyName().trim().isEmpty()) {
            throw new InvalidDocumentException("Company name is mandatory for business profile");
        }
        if (request.taxId() == null || !ValidationUtils.isValidGst(request.taxId())) {
            throw new InvalidDocumentException("Valid GST number (Tax ID) is mandatory for business profile");
        }

        BusinessProfile profile = businessRepository.findByUser_Id(userId)
                .orElse(BusinessProfile.builder().user(user).build());

        profile.setCompanyName(request.companyName().trim());
        profile.setTaxId(request.taxId().trim().toUpperCase());
        profile.setWebsite(request.website() != null ? request.website().trim() : null);

        profile = businessRepository.save(profile);
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated Business Profile");
        return userMapper.toBusinessProfileResponse(profile);
    }

    @Transactional
    public BusinessProfileResponse verifyBusinessProfile(UUID userId, String status) {
        if (!"VERIFIED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Invalid verification status. Must be VERIFIED or REJECTED");
        }

        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BusinessProfile profile = businessRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Business profile not found"));

        // If verified, we can also update user status to active/verified if required.
        if ("VERIFIED".equalsIgnoreCase(status)) {
            eventPublisher.publishEvent("BusinessVerified", userId, profile.getId());
        } else {
            eventPublisher.publishEvent("ProfileUpdated", userId, "Business Profile Verification: " + status);
        }

        return userMapper.toBusinessProfileResponse(profile);
    }

    // --- Fleet Owner Profile CRUD ---

    @Transactional
    public FleetOwnerProfileResponse updateFleetOwnerProfile(UUID userId, FleetOwnerProfileUpdateRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.companyName() == null || request.companyName().trim().isEmpty()) {
            throw new InvalidDocumentException("Company name is mandatory for fleet owner profile");
        }
        if (request.fleetSize() == null || request.fleetSize() < 0) {
            throw new InvalidDocumentException("Valid fleet size is mandatory");
        }

        FleetOwnerProfile profile = fleetOwnerRepository.findByUser_Id(userId)
                .orElse(FleetOwnerProfile.builder().user(user).build());

        profile.setCompanyName(request.companyName().trim());
        profile.setFleetSize(request.fleetSize());

        profile = fleetOwnerRepository.save(profile);
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated Fleet Owner Profile");
        return userMapper.toFleetOwnerProfileResponse(profile);
    }

    // --- Preferences CRUD ---

    @Transactional(readOnly = true)
    public PreferenceResponse getPreference(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Preference preference = preferenceRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Preferences not found for user"));
        return userMapper.toPreferenceResponse(preference);
    }

    @Transactional
    public PreferenceResponse updatePreference(UUID userId, PreferenceUpdateRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Preference preference = preferenceRepository.findByUser_Id(userId)
                .orElse(Preference.builder().user(user).build());

        preference.setLanguage(request.language().trim().toLowerCase());
        preference.setTheme(request.theme().trim().toUpperCase());

        preference = preferenceRepository.save(preference);
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated Preferences");
        return userMapper.toPreferenceResponse(preference);
    }

    // --- User Settings CRUD ---

    @Transactional(readOnly = true)
    public UserSettingsResponse getSettings(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        UserSettings settings = userSettingsRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found for user"));
        return userMapper.toUserSettingsResponse(settings);
    }

    @Transactional
    public UserSettingsResponse updateSettings(UUID userId, UserSettingsUpdateRequest request) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserSettings settings = userSettingsRepository.findByUser_Id(userId)
                .orElse(UserSettings.builder().user(user).build());

        settings.setEmailNotificationsEnabled(request.emailNotificationsEnabled());
        settings.setSmsNotificationsEnabled(request.smsNotificationsEnabled());
        settings.setPushNotificationsEnabled(request.pushNotificationsEnabled());
        settings.setTwoFactorEnabled(request.twoFactorEnabled());

        settings = userSettingsRepository.save(settings);
        eventPublisher.publishEvent("ProfileUpdated", userId, "Updated User Settings");
        return userMapper.toUserSettingsResponse(settings);
    }

    // --- Documents Management ---

    @Transactional(readOnly = true)
    public List<DocumentResponse> getDocuments(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return documentRepository.findByUser_Id(userId).stream()
                .map(userMapper::toDocumentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DocumentResponse uploadDocument(UUID userId, String documentType, MultipartFile file) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (documentType == null || documentType.trim().isEmpty()) {
            throw new InvalidDocumentException("Document type is mandatory");
        }

        // Validate documentType matches allowed types
        String type = documentType.trim().toUpperCase();
        List<String> allowedDocTypes = List.of(
                "LICENSE", "DRIVING_LICENSE", "AADHAAR", "PAN", 
                "GST", "GST_CERTIFICATE", "COMPANY_REG", "COMPANY_REGISTRATION", 
                "VEHICLE_PERMIT", "INSURANCE_COPY", "RC_BOOK"
        );
        if (!allowedDocTypes.contains(type)) {
            throw new InvalidDocumentException("Unsupported document type: " + type);
        }

        // Limit documents to 10 MB maximum
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new InvalidDocumentException("Document size exceeds maximum limit of 10 MB");
        }

        String url = storageService.uploadFile("documents", userId + "_" + type, file);

        IdentityDocument doc = IdentityDocument.builder()
                .user(user)
                .type(type)
                .url(url)
                .status("PENDING")
                .build();

        doc = documentRepository.save(doc);
        eventPublisher.publishEvent("DocumentUploaded", userId, doc.getId());
        return userMapper.toDocumentResponse(doc);
    }

    @Transactional
    public void deleteDocument(UUID userId, UUID documentId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        IdentityDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        if (!doc.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Document does not belong to this user");
        }

        // Soft Delete
        doc.setDeleted(true);
        documentRepository.save(doc);

        storageService.deleteFile(doc.getUrl());
        eventPublisher.publishEvent("ProfileUpdated", userId, "Deleted Document: " + documentId);
    }

    // --- Profile Photos Management ---

    @Transactional(readOnly = true)
    public ProfilePhotoResponse getProfilePhoto(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        ProfilePhoto photo = profilePhotoRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile photo not found"));
        return userMapper.toProfilePhotoResponse(photo);
    }

    @Transactional
    public ProfilePhotoResponse uploadProfilePhoto(UUID userId, MultipartFile file) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Upload and do validation checks (e.g. format JPG/PNG/WEBP and 5MB limit) inside StorageService
        String url = storageService.uploadFile("photos", userId.toString(), file);

        ProfilePhoto photo = profilePhotoRepository.findByUser_Id(userId)
                .orElse(ProfilePhoto.builder().user(user).build());

        photo.setUrl(url);
        photo.setContentType(file.getContentType());
        photo.setDeleted(false); // recover if soft-deleted

        photo = profilePhotoRepository.save(photo);
        eventPublisher.publishEvent("ProfileUpdated", userId, "Uploaded Profile Photo");
        return userMapper.toProfilePhotoResponse(photo);
    }

    @Transactional
    public void deleteProfilePhoto(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        ProfilePhoto photo = profilePhotoRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile photo not found"));

        // Soft Delete
        photo.setDeleted(true);
        profilePhotoRepository.save(photo);

        storageService.deleteFile(photo.getUrl());
        eventPublisher.publishEvent("ProfileUpdated", userId, "Deleted Profile Photo");
    }
}
