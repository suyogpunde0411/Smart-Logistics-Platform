package com.smartlogistics.userservice.mapper;

import com.smartlogistics.shared.dto.UserDTO;
import com.smartlogistics.userservice.domain.entity.Address;
import com.smartlogistics.userservice.domain.entity.BusinessProfile;
import com.smartlogistics.userservice.domain.entity.DriverProfile;
import com.smartlogistics.userservice.domain.entity.EmergencyContact;
import com.smartlogistics.userservice.domain.entity.FleetOwnerProfile;
import com.smartlogistics.userservice.domain.entity.IdentityDocument;
import com.smartlogistics.userservice.domain.entity.Preference;
import com.smartlogistics.userservice.domain.entity.ProfilePhoto;
import com.smartlogistics.userservice.domain.entity.UserProfile;
import com.smartlogistics.userservice.domain.entity.UserSettings;
import com.smartlogistics.userservice.dto.AddressDto;
import com.smartlogistics.userservice.dto.DocumentDto;
import com.smartlogistics.userservice.dto.EmergencyContactDto;
import com.smartlogistics.userservice.dto.PreferenceDto;
import com.smartlogistics.userservice.dto.ProfileDto;
import com.smartlogistics.userservice.dto.ProfilePhotoDto;
import com.smartlogistics.userservice.dto.UserSettingsDto;
import java.time.LocalDate;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:46:06+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO.UserResponse toUserResponse(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UUID id = null;
        String email = null;
        String phone = null;
        String firstName = null;
        String lastName = null;
        String status = null;

        id = userProfile.getId();
        email = userProfile.getEmail();
        phone = userProfile.getPhone();
        firstName = userProfile.getFirstName();
        lastName = userProfile.getLastName();
        status = userProfile.getStatus();

        UserDTO.UserResponse userResponse = new UserDTO.UserResponse( id, email, phone, firstName, lastName, status );

        return userResponse;
    }

    @Override
    public ProfileDto.DriverProfileResponse toDriverProfileResponse(DriverProfile driverProfile) {
        if ( driverProfile == null ) {
            return null;
        }

        UUID id = null;
        String licenseNumber = null;
        LocalDate licenseExpiry = null;
        Integer experienceYears = null;
        String status = null;

        id = driverProfile.getId();
        licenseNumber = driverProfile.getLicenseNumber();
        licenseExpiry = driverProfile.getLicenseExpiry();
        experienceYears = driverProfile.getExperienceYears();
        status = driverProfile.getStatus();

        ProfileDto.DriverProfileResponse driverProfileResponse = new ProfileDto.DriverProfileResponse( id, licenseNumber, licenseExpiry, experienceYears, status );

        return driverProfileResponse;
    }

    @Override
    public ProfileDto.BusinessProfileResponse toBusinessProfileResponse(BusinessProfile businessProfile) {
        if ( businessProfile == null ) {
            return null;
        }

        UUID id = null;
        String companyName = null;
        String taxId = null;
        String website = null;

        id = businessProfile.getId();
        companyName = businessProfile.getCompanyName();
        taxId = businessProfile.getTaxId();
        website = businessProfile.getWebsite();

        ProfileDto.BusinessProfileResponse businessProfileResponse = new ProfileDto.BusinessProfileResponse( id, companyName, taxId, website );

        return businessProfileResponse;
    }

    @Override
    public ProfileDto.FleetOwnerProfileResponse toFleetOwnerProfileResponse(FleetOwnerProfile fleetOwnerProfile) {
        if ( fleetOwnerProfile == null ) {
            return null;
        }

        UUID id = null;
        String companyName = null;
        Integer fleetSize = null;

        id = fleetOwnerProfile.getId();
        companyName = fleetOwnerProfile.getCompanyName();
        fleetSize = fleetOwnerProfile.getFleetSize();

        ProfileDto.FleetOwnerProfileResponse fleetOwnerProfileResponse = new ProfileDto.FleetOwnerProfileResponse( id, companyName, fleetSize );

        return fleetOwnerProfileResponse;
    }

    @Override
    public AddressDto.AddressResponse toAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        UUID id = null;
        String line1 = null;
        String line2 = null;
        String city = null;
        String state = null;
        String zip = null;
        String country = null;
        String type = null;

        id = address.getId();
        line1 = address.getLine1();
        line2 = address.getLine2();
        city = address.getCity();
        state = address.getState();
        zip = address.getZip();
        country = address.getCountry();
        type = address.getType();

        AddressDto.AddressResponse addressResponse = new AddressDto.AddressResponse( id, line1, line2, city, state, zip, country, type );

        return addressResponse;
    }

    @Override
    public Address toAddress(AddressDto.AddressRequest request) {
        if ( request == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.line1( request.line1() );
        address.line2( request.line2() );
        address.city( request.city() );
        address.state( request.state() );
        address.zip( request.zip() );
        address.country( request.country() );
        address.type( request.type() );

        return address.build();
    }

    @Override
    public void updateAddressFromRequest(AddressDto.AddressRequest request, Address address) {
        if ( request == null ) {
            return;
        }

        address.setLine1( request.line1() );
        address.setLine2( request.line2() );
        address.setCity( request.city() );
        address.setState( request.state() );
        address.setZip( request.zip() );
        address.setCountry( request.country() );
        address.setType( request.type() );
    }

    @Override
    public EmergencyContactDto.EmergencyContactResponse toEmergencyContactResponse(EmergencyContact contact) {
        if ( contact == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String phone = null;
        String relationship = null;

        id = contact.getId();
        name = contact.getName();
        phone = contact.getPhone();
        relationship = contact.getRelationship();

        EmergencyContactDto.EmergencyContactResponse emergencyContactResponse = new EmergencyContactDto.EmergencyContactResponse( id, name, phone, relationship );

        return emergencyContactResponse;
    }

    @Override
    public EmergencyContact toEmergencyContact(EmergencyContactDto.EmergencyContactRequest request) {
        if ( request == null ) {
            return null;
        }

        EmergencyContact.EmergencyContactBuilder emergencyContact = EmergencyContact.builder();

        emergencyContact.name( request.name() );
        emergencyContact.phone( request.phone() );
        emergencyContact.relationship( request.relationship() );

        return emergencyContact.build();
    }

    @Override
    public void updateEmergencyContactFromRequest(EmergencyContactDto.EmergencyContactRequest request, EmergencyContact contact) {
        if ( request == null ) {
            return;
        }

        contact.setName( request.name() );
        contact.setPhone( request.phone() );
        contact.setRelationship( request.relationship() );
    }

    @Override
    public DocumentDto.DocumentResponse toDocumentResponse(IdentityDocument document) {
        if ( document == null ) {
            return null;
        }

        UUID id = null;
        String type = null;
        String url = null;
        String status = null;

        id = document.getId();
        type = document.getType();
        url = document.getUrl();
        status = document.getStatus();

        DocumentDto.DocumentResponse documentResponse = new DocumentDto.DocumentResponse( id, type, url, status );

        return documentResponse;
    }

    @Override
    public PreferenceDto.PreferenceResponse toPreferenceResponse(Preference preference) {
        if ( preference == null ) {
            return null;
        }

        String language = null;
        String theme = null;

        language = preference.getLanguage();
        theme = preference.getTheme();

        PreferenceDto.PreferenceResponse preferenceResponse = new PreferenceDto.PreferenceResponse( language, theme );

        return preferenceResponse;
    }

    @Override
    public UserSettingsDto.UserSettingsResponse toUserSettingsResponse(UserSettings settings) {
        if ( settings == null ) {
            return null;
        }

        boolean emailNotificationsEnabled = false;
        boolean smsNotificationsEnabled = false;
        boolean pushNotificationsEnabled = false;
        boolean twoFactorEnabled = false;

        emailNotificationsEnabled = settings.isEmailNotificationsEnabled();
        smsNotificationsEnabled = settings.isSmsNotificationsEnabled();
        pushNotificationsEnabled = settings.isPushNotificationsEnabled();
        twoFactorEnabled = settings.isTwoFactorEnabled();

        UserSettingsDto.UserSettingsResponse userSettingsResponse = new UserSettingsDto.UserSettingsResponse( emailNotificationsEnabled, smsNotificationsEnabled, pushNotificationsEnabled, twoFactorEnabled );

        return userSettingsResponse;
    }

    @Override
    public ProfilePhotoDto.ProfilePhotoResponse toProfilePhotoResponse(ProfilePhoto profilePhoto) {
        if ( profilePhoto == null ) {
            return null;
        }

        UUID id = null;
        String url = null;
        String contentType = null;

        id = profilePhoto.getId();
        url = profilePhoto.getUrl();
        contentType = profilePhoto.getContentType();

        ProfilePhotoDto.ProfilePhotoResponse profilePhotoResponse = new ProfilePhotoDto.ProfilePhotoResponse( id, url, contentType );

        return profilePhotoResponse;
    }
}
