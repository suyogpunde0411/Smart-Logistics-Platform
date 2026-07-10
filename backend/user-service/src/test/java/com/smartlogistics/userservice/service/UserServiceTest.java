package com.smartlogistics.userservice.service;

import com.smartlogistics.userservice.domain.entity.*;
import com.smartlogistics.userservice.domain.repository.*;
import com.smartlogistics.userservice.dto.*;
import com.smartlogistics.userservice.dto.AddressDto.AddressRequest;
import com.smartlogistics.userservice.dto.AddressDto.AddressResponse;
import com.smartlogistics.userservice.dto.ProfileDto.DriverProfileResponse;
import com.smartlogistics.userservice.dto.ProfileDto.DriverProfileUpdateRequest;
import com.smartlogistics.shared.dto.UserDTO.UserResponse;
import com.smartlogistics.userservice.exception.*;
import com.smartlogistics.userservice.mapper.UserMapper;
import com.smartlogistics.userservice.service.kafka.UserEventPublisher;
import com.smartlogistics.userservice.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserProfileRepository userRepository;
    @Mock private DriverProfileRepository driverRepository;
    @Mock private BusinessProfileRepository businessRepository;
    @Mock private FleetOwnerProfileRepository fleetOwnerRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private EmergencyContactRepository emergencyContactRepository;
    @Mock private IdentityDocumentRepository documentRepository;
    @Mock private PreferenceRepository preferenceRepository;
    @Mock private UserSettingsRepository userSettingsRepository;
    @Mock private ProfilePhotoRepository profilePhotoRepository;
    @Mock private UserMapper userMapper;
    @Mock private UserEventPublisher eventPublisher;
    @Mock private StorageService storageService;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser_Success() {
        UUID userId = UUID.randomUUID();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(userId);
        userProfile.setEmail("test@example.com");

        UserResponse userResponse = new UserResponse(userId, "test@example.com", null, null, null, "ACTIVE");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userProfile));
        when(userMapper.toUserResponse(userProfile)).thenReturn(userResponse);

        UserResponse result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals("test@example.com", result.email());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createProfile_DuplicateEmail_ThrowsException() {
        UserRegisteredEvent event = new UserRegisteredEvent(UUID.randomUUID(), "duplicate@example.com", "+919876543210", "ROLE_DRIVER");
        when(userRepository.existsById(event.userId())).thenReturn(false);
        when(userRepository.existsByEmail(event.email())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createProfile(event));
    }

    @Test
    void createProfile_Success() {
        UserRegisteredEvent event = new UserRegisteredEvent(UUID.randomUUID(), "new@example.com", "+919876543210", "ROLE_DRIVER");
        when(userRepository.existsById(event.userId())).thenReturn(false);
        when(userRepository.existsByEmail(event.email())).thenReturn(false);
        when(userRepository.existsByPhone(event.phone())).thenReturn(false);

        userService.createProfile(event);

        verify(userRepository, times(1)).save(any(UserProfile.class));
        verify(eventPublisher, times(1)).publishEvent(eq("UserProfileCreated"), eq(event.userId()), eq(event));
    }

    @Test
    void deleteAddress_Success() {
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        UserProfile user = new UserProfile();
        user.setId(userId);

        Address address = Address.builder()
                .line1("123 Main St")
                .city("Pune")
                .state("MH")
                .zip("411001")
                .country("India")
                .type("HOME")
                .user(user)
                .build();
        address.setId(addressId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        userService.deleteAddress(userId, addressId);

        assertTrue(address.isDeleted());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void verifyDriverProfile_Success() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setId(userId);

        DriverProfile profile = DriverProfile.builder()
                .user(user)
                .licenseNumber("DL14110012345")
                .licenseExpiry(LocalDate.now().plusYears(5))
                .experienceYears(8)
                .status("PENDING")
                .build();
        profile.setId(UUID.randomUUID());

        when(driverRepository.findByUser_Id(userId)).thenReturn(Optional.of(profile));
        when(driverRepository.save(any(DriverProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.verifyDriverProfile(userId, "VERIFIED");

        assertEquals("VERIFIED", profile.getStatus());
        verify(eventPublisher, times(1)).publishEvent(eq("DriverVerified"), eq(userId), eq(profile.getId()));
    }
}
