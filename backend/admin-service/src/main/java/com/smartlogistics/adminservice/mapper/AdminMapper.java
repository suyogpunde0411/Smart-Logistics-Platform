package com.smartlogistics.adminservice.mapper;

import com.smartlogistics.adminservice.dto.*;
import com.smartlogistics.adminservice.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "flagKey", source = "flagName")
    FeatureFlagDto toDto(FeatureFlag entity);
    @Mapping(target = "flagName", source = "flagKey")
    FeatureFlag toEntity(FeatureFlagDto dto);
    
    SystemConfigurationDto toDto(SystemConfiguration entity);
    SystemConfiguration toEntity(SystemConfigurationDto dto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "message")
    @Mapping(target = "targetAudience", source = "audience")
    @Mapping(target = "isActive", source = "isActive")
    AnnouncementDto toDto(Announcement entity);
    @Mapping(target = "message", source = "content")
    @Mapping(target = "audience", source = "targetAudience")
    Announcement toEntity(AnnouncementDto dto);

    VerificationRequestDto toDto(VerificationRequest entity);
    VerificationRequest toEntity(VerificationRequestDto dto);

    @Mapping(target = "subject", source = "title")
    SupportTicketDto toDto(SupportTicket entity);
    @Mapping(target = "title", source = "subject")
    SupportTicket toEntity(SupportTicketDto dto);

    @Mapping(target = "id", source = "id")
    AuditLogDto toDto(AuditLog entity);
}
