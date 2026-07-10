package com.smartlogistics.userservice.mapper;

import com.smartlogistics.userservice.domain.entity.*;
import com.smartlogistics.userservice.dto.AddressDto.AddressRequest;
import com.smartlogistics.userservice.dto.AddressDto.AddressResponse;
import com.smartlogistics.userservice.dto.DocumentDto.DocumentResponse;
import com.smartlogistics.userservice.dto.EmergencyContactDto.EmergencyContactRequest;
import com.smartlogistics.userservice.dto.EmergencyContactDto.EmergencyContactResponse;
import com.smartlogistics.userservice.dto.ProfileDto.BusinessProfileResponse;
import com.smartlogistics.userservice.dto.ProfileDto.DriverProfileResponse;
import com.smartlogistics.userservice.dto.ProfileDto.FleetOwnerProfileResponse;
import com.smartlogistics.shared.dto.UserDTO.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toUserResponse(UserProfile userProfile);
    
    DriverProfileResponse toDriverProfileResponse(DriverProfile driverProfile);
    
    BusinessProfileResponse toBusinessProfileResponse(BusinessProfile businessProfile);
    
    FleetOwnerProfileResponse toFleetOwnerProfileResponse(FleetOwnerProfile fleetOwnerProfile);
    
    AddressResponse toAddressResponse(Address address);
    
    Address toAddress(AddressRequest request);
    
    @Mapping(target = "id", ignore = true)
    void updateAddressFromRequest(AddressRequest request, @MappingTarget Address address);
    
    EmergencyContactResponse toEmergencyContactResponse(EmergencyContact contact);
    
    EmergencyContact toEmergencyContact(EmergencyContactRequest request);
    
    @Mapping(target = "id", ignore = true)
    void updateEmergencyContactFromRequest(EmergencyContactRequest request, @MappingTarget EmergencyContact contact);
    
    DocumentResponse toDocumentResponse(IdentityDocument document);

    com.smartlogistics.userservice.dto.PreferenceDto.PreferenceResponse toPreferenceResponse(Preference preference);

    com.smartlogistics.userservice.dto.UserSettingsDto.UserSettingsResponse toUserSettingsResponse(UserSettings settings);

    com.smartlogistics.userservice.dto.ProfilePhotoDto.ProfilePhotoResponse toProfilePhotoResponse(ProfilePhoto profilePhoto);
}
