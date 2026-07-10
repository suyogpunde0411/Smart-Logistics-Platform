package com.smartlogistics.userservice.controller;

import com.smartlogistics.userservice.dto.ApiResponse;
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
import com.smartlogistics.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User profile and resource management APIs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Search and paginate users")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<UserResponse> userPage = userService.searchUsers(search, pageRequest);
        
        ApiResponse.PaginationInfo pagination = new ApiResponse.PaginationInfo(
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
        
        return ResponseEntity.ok(ApiResponse.successPaginated("Users fetched successfully", userPage.getContent(), pagination));
    }

    @Operation(summary = "Get user profile by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userService.getUser(id)));
    }

    @Operation(summary = "Update basic user profile")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.updateUser(id, request)));
    }

    // --- Address endpoints ---

    @Operation(summary = "List user addresses")
    @GetMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Addresses fetched successfully", userService.getAddresses(id)));
    }

    @Operation(summary = "Add a new address")
    @PostMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(@PathVariable UUID id, @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Address added successfully", userService.addAddress(id, request)));
    }

    @Operation(summary = "Update an address")
    @PutMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable UUID id, 
            @PathVariable UUID addressId, 
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully", userService.updateAddress(id, addressId, request)));
    }

    @Operation(summary = "Delete an address")
    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable UUID id, @PathVariable UUID addressId) {
        userService.deleteAddress(id, addressId);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }

    // --- Emergency Contact endpoints ---

    @Operation(summary = "List user emergency contacts")
    @GetMapping("/{id}/emergency-contacts")
    public ResponseEntity<ApiResponse<List<EmergencyContactResponse>>> getEmergencyContacts(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Emergency contacts fetched successfully", userService.getEmergencyContacts(id)));
    }

    @Operation(summary = "Add emergency contact")
    @PostMapping("/{id}/emergency-contacts")
    public ResponseEntity<ApiResponse<EmergencyContactResponse>> addEmergencyContact(
            @PathVariable UUID id, @Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Emergency contact added successfully", userService.addEmergencyContact(id, request)));
    }

    @Operation(summary = "Update emergency contact")
    @PutMapping("/{id}/emergency-contacts/{contactId}")
    public ResponseEntity<ApiResponse<EmergencyContactResponse>> updateEmergencyContact(
            @PathVariable UUID id, 
            @PathVariable UUID contactId, 
            @Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Emergency contact updated successfully", userService.updateEmergencyContact(id, contactId, request)));
    }

    @Operation(summary = "Delete emergency contact")
    @DeleteMapping("/{id}/emergency-contacts/{contactId}")
    public ResponseEntity<ApiResponse<Void>> deleteEmergencyContact(@PathVariable UUID id, @PathVariable UUID contactId) {
        userService.deleteEmergencyContact(id, contactId);
        return ResponseEntity.ok(ApiResponse.success("Emergency contact deleted successfully", null));
    }

    // --- Profile verification & sub-profile endpoints ---

    @Operation(summary = "Update driver profile")
    @PutMapping("/{id}/driver-profile")
    public ResponseEntity<ApiResponse<DriverProfileResponse>> updateDriverProfile(
            @PathVariable UUID id, @Valid @RequestBody DriverProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Driver profile updated successfully", userService.updateDriverProfile(id, request)));
    }

    @Operation(summary = "Verify driver profile (Internal/Admin)")
    @PutMapping("/{id}/driver-profile/verify")
    public ResponseEntity<ApiResponse<DriverProfileResponse>> verifyDriverProfile(
            @PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Driver profile verified status updated", userService.verifyDriverProfile(id, status)));
    }

    @Operation(summary = "Update business profile")
    @PutMapping("/{id}/business-profile")
    public ResponseEntity<ApiResponse<BusinessProfileResponse>> updateBusinessProfile(
            @PathVariable UUID id, @Valid @RequestBody BusinessProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Business profile updated successfully", userService.updateBusinessProfile(id, request)));
    }

    @Operation(summary = "Verify business profile (Internal/Admin)")
    @PutMapping("/{id}/business-profile/verify")
    public ResponseEntity<ApiResponse<BusinessProfileResponse>> verifyBusinessProfile(
            @PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Business profile verified status updated", userService.verifyBusinessProfile(id, status)));
    }

    @Operation(summary = "Update fleet owner profile")
    @PutMapping("/{id}/fleet-owner-profile")
    public ResponseEntity<ApiResponse<FleetOwnerProfileResponse>> updateFleetOwnerProfile(
            @PathVariable UUID id, @Valid @RequestBody FleetOwnerProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Fleet owner profile updated successfully", userService.updateFleetOwnerProfile(id, request)));
    }

    // --- Preferences and Settings endpoints ---

    @Operation(summary = "Get user preferences")
    @GetMapping("/{id}/preferences")
    public ResponseEntity<ApiResponse<PreferenceResponse>> getPreference(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("User preferences fetched successfully", userService.getPreference(id)));
    }

    @Operation(summary = "Update user preferences")
    @PutMapping("/{id}/preferences")
    public ResponseEntity<ApiResponse<PreferenceResponse>> updatePreference(
            @PathVariable UUID id, @Valid @RequestBody PreferenceUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User preferences updated successfully", userService.updatePreference(id, request)));
    }

    @Operation(summary = "Get user notifications and settings")
    @GetMapping("/{id}/settings")
    public ResponseEntity<ApiResponse<UserSettingsResponse>> getSettings(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("User settings fetched successfully", userService.getSettings(id)));
    }

    @Operation(summary = "Update user settings")
    @PutMapping("/{id}/settings")
    public ResponseEntity<ApiResponse<UserSettingsResponse>> updateSettings(
            @PathVariable UUID id, @Valid @RequestBody UserSettingsUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User settings updated successfully", userService.updateSettings(id, request)));
    }

    // --- Documents endpoints ---

    @Operation(summary = "List uploaded documents metadata")
    @GetMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getDocuments(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Documents list fetched successfully", userService.getDocuments(id)));
    }

    @Operation(summary = "Upload KYC/License documents")
    @PostMapping(value = "/{id}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @PathVariable UUID id,
            @RequestParam("documentType") String documentType,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", userService.uploadDocument(id, documentType, file)));
    }

    @Operation(summary = "Delete an uploaded document")
    @DeleteMapping("/{id}/documents/{documentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable UUID id, @PathVariable UUID documentId) {
        userService.deleteDocument(id, documentId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }

    // --- Profile Photo endpoints ---

    @Operation(summary = "Get profile photo metadata")
    @GetMapping("/{id}/profile-photo")
    public ResponseEntity<ApiResponse<ProfilePhotoResponse>> getProfilePhoto(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Profile photo details fetched successfully", userService.getProfilePhoto(id)));
    }

    @Operation(summary = "Upload profile photo")
    @PostMapping(value = "/{id}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfilePhotoResponse>> uploadProfilePhoto(
            @PathVariable UUID id, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("Profile photo uploaded successfully", userService.uploadProfilePhoto(id, file)));
    }

    @Operation(summary = "Delete profile photo")
    @DeleteMapping("/{id}/profile-photo")
    public ResponseEntity<ApiResponse<Void>> deleteProfilePhoto(@PathVariable UUID id) {
        userService.deleteProfilePhoto(id);
        return ResponseEntity.ok(ApiResponse.success("Profile photo deleted successfully", null));
    }
}
