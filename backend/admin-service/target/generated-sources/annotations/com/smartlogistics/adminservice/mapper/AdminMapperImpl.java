package com.smartlogistics.adminservice.mapper;

import com.smartlogistics.adminservice.dto.AnnouncementDto;
import com.smartlogistics.adminservice.dto.AuditLogDto;
import com.smartlogistics.adminservice.dto.FeatureFlagDto;
import com.smartlogistics.adminservice.dto.SupportTicketDto;
import com.smartlogistics.adminservice.dto.SystemConfigurationDto;
import com.smartlogistics.adminservice.dto.VerificationRequestDto;
import com.smartlogistics.adminservice.entity.Announcement;
import com.smartlogistics.adminservice.entity.AuditLog;
import com.smartlogistics.adminservice.entity.FeatureFlag;
import com.smartlogistics.adminservice.entity.SupportTicket;
import com.smartlogistics.adminservice.entity.SystemConfiguration;
import com.smartlogistics.adminservice.entity.VerificationRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T20:15:05+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public FeatureFlagDto toDto(FeatureFlag entity) {
        if ( entity == null ) {
            return null;
        }

        String flagKey = null;
        UUID id = null;
        boolean isEnabled = false;
        String description = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        flagKey = entity.getFlagName();
        id = entity.getId();
        if ( entity.getIsEnabled() != null ) {
            isEnabled = entity.getIsEnabled();
        }
        description = entity.getDescription();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        FeatureFlagDto featureFlagDto = new FeatureFlagDto( id, flagKey, isEnabled, description, createdAt, updatedAt );

        return featureFlagDto;
    }

    @Override
    public FeatureFlag toEntity(FeatureFlagDto dto) {
        if ( dto == null ) {
            return null;
        }

        FeatureFlag.FeatureFlagBuilder featureFlag = FeatureFlag.builder();

        featureFlag.flagName( dto.flagKey() );
        featureFlag.isEnabled( dto.isEnabled() );
        featureFlag.description( dto.description() );

        return featureFlag.build();
    }

    @Override
    public SystemConfigurationDto toDto(SystemConfiguration entity) {
        if ( entity == null ) {
            return null;
        }

        String configKey = null;
        String configValue = null;
        String description = null;
        String module = null;

        configKey = entity.getConfigKey();
        configValue = entity.getConfigValue();
        description = entity.getDescription();
        module = entity.getModule();

        SystemConfigurationDto systemConfigurationDto = new SystemConfigurationDto( configKey, configValue, description, module );

        return systemConfigurationDto;
    }

    @Override
    public SystemConfiguration toEntity(SystemConfigurationDto dto) {
        if ( dto == null ) {
            return null;
        }

        SystemConfiguration.SystemConfigurationBuilder systemConfiguration = SystemConfiguration.builder();

        systemConfiguration.configKey( dto.configKey() );
        systemConfiguration.configValue( dto.configValue() );
        systemConfiguration.description( dto.description() );
        systemConfiguration.module( dto.module() );

        return systemConfiguration.build();
    }

    @Override
    public AnnouncementDto toDto(Announcement entity) {
        if ( entity == null ) {
            return null;
        }

        String title = null;
        String content = null;
        String targetAudience = null;
        boolean isActive = false;
        UUID id = null;
        LocalDateTime createdAt = null;

        title = entity.getTitle();
        content = entity.getMessage();
        targetAudience = entity.getAudience();
        if ( entity.getIsActive() != null ) {
            isActive = entity.getIsActive();
        }
        id = entity.getId();
        createdAt = entity.getCreatedAt();

        AnnouncementDto announcementDto = new AnnouncementDto( id, title, content, targetAudience, isActive, createdAt );

        return announcementDto;
    }

    @Override
    public Announcement toEntity(AnnouncementDto dto) {
        if ( dto == null ) {
            return null;
        }

        Announcement.AnnouncementBuilder announcement = Announcement.builder();

        announcement.message( dto.content() );
        announcement.audience( dto.targetAudience() );
        announcement.title( dto.title() );
        announcement.isActive( dto.isActive() );

        return announcement.build();
    }

    @Override
    public VerificationRequestDto toDto(VerificationRequest entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        UUID entityId = null;
        String entityType = null;
        String status = null;
        String comments = null;
        UUID reviewedBy = null;
        LocalDateTime reviewedAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = entity.getId();
        entityId = entity.getEntityId();
        entityType = entity.getEntityType();
        status = entity.getStatus();
        comments = entity.getComments();
        reviewedBy = entity.getReviewedBy();
        reviewedAt = entity.getReviewedAt();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        VerificationRequestDto verificationRequestDto = new VerificationRequestDto( id, entityId, entityType, status, comments, reviewedBy, reviewedAt, createdAt, updatedAt );

        return verificationRequestDto;
    }

    @Override
    public VerificationRequest toEntity(VerificationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        VerificationRequest.VerificationRequestBuilder verificationRequest = VerificationRequest.builder();

        verificationRequest.entityId( dto.entityId() );
        verificationRequest.entityType( dto.entityType() );
        verificationRequest.status( dto.status() );
        verificationRequest.comments( dto.comments() );
        verificationRequest.reviewedBy( dto.reviewedBy() );
        verificationRequest.reviewedAt( dto.reviewedAt() );

        return verificationRequest.build();
    }

    @Override
    public SupportTicketDto toDto(SupportTicket entity) {
        if ( entity == null ) {
            return null;
        }

        String subject = null;
        UUID id = null;
        UUID userId = null;
        String description = null;
        String status = null;
        String priority = null;
        String resolution = null;
        UUID resolvedBy = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        subject = entity.getTitle();
        id = entity.getId();
        userId = entity.getUserId();
        description = entity.getDescription();
        status = entity.getStatus();
        priority = entity.getPriority();
        resolution = entity.getResolution();
        resolvedBy = entity.getResolvedBy();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        SupportTicketDto supportTicketDto = new SupportTicketDto( id, userId, subject, description, status, priority, resolution, resolvedBy, createdAt, updatedAt );

        return supportTicketDto;
    }

    @Override
    public SupportTicket toEntity(SupportTicketDto dto) {
        if ( dto == null ) {
            return null;
        }

        SupportTicket.SupportTicketBuilder supportTicket = SupportTicket.builder();

        supportTicket.title( dto.subject() );
        supportTicket.userId( dto.userId() );
        supportTicket.description( dto.description() );
        supportTicket.status( dto.status() );
        supportTicket.priority( dto.priority() );
        supportTicket.resolvedBy( dto.resolvedBy() );
        supportTicket.resolution( dto.resolution() );

        return supportTicket.build();
    }

    @Override
    public AuditLogDto toDto(AuditLog entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String correlationId = null;
        String traceId = null;
        String serviceName = null;
        String message = null;
        String logLevel = null;
        LocalDateTime createdAt = null;

        id = entity.getId();
        correlationId = entity.getCorrelationId();
        traceId = entity.getTraceId();
        serviceName = entity.getServiceName();
        message = entity.getMessage();
        logLevel = entity.getLogLevel();
        createdAt = entity.getCreatedAt();

        AuditLogDto auditLogDto = new AuditLogDto( id, correlationId, traceId, serviceName, message, logLevel, createdAt );

        return auditLogDto;
    }
}
