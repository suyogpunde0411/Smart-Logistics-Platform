package com.smartlogistics.notificationservice.mapper;

import com.smartlogistics.notificationservice.dto.NotificationDto;
import com.smartlogistics.notificationservice.dto.PreferenceDto;
import com.smartlogistics.notificationservice.dto.TemplateDto;
import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.entity.NotificationPreference;
import com.smartlogistics.notificationservice.entity.NotificationTemplate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-10T23:58:43+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDto.Response toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        UUID id = null;
        UUID recipientId = null;
        String type = null;
        String channel = null;
        String title = null;
        String message = null;
        String status = null;
        LocalDateTime readAt = null;
        String failureReason = null;
        Integer retryCount = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = notification.getId();
        recipientId = notification.getRecipientId();
        type = notification.getType();
        channel = notification.getChannel();
        title = notification.getTitle();
        message = notification.getMessage();
        status = notification.getStatus();
        readAt = notification.getReadAt();
        failureReason = notification.getFailureReason();
        retryCount = notification.getRetryCount();
        createdAt = notification.getCreatedAt();
        updatedAt = notification.getUpdatedAt();

        boolean isRead = false;

        NotificationDto.Response response = new NotificationDto.Response( id, recipientId, type, channel, title, message, status, isRead, readAt, failureReason, retryCount, createdAt, updatedAt );

        return response;
    }

    @Override
    public PreferenceDto.Response toResponse(NotificationPreference preference) {
        if ( preference == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        boolean emailEnabled = false;
        boolean smsEnabled = false;
        boolean pushEnabled = false;
        boolean marketingEnabled = false;
        boolean systemAlertsEnabled = false;
        boolean tripUpdatesEnabled = false;
        boolean shipmentUpdatesEnabled = false;

        id = preference.getId();
        userId = preference.getUserId();
        emailEnabled = preference.isEmailEnabled();
        smsEnabled = preference.isSmsEnabled();
        pushEnabled = preference.isPushEnabled();
        marketingEnabled = preference.isMarketingEnabled();
        systemAlertsEnabled = preference.isSystemAlertsEnabled();
        tripUpdatesEnabled = preference.isTripUpdatesEnabled();
        shipmentUpdatesEnabled = preference.isShipmentUpdatesEnabled();

        PreferenceDto.Response response = new PreferenceDto.Response( id, userId, emailEnabled, smsEnabled, pushEnabled, marketingEnabled, systemAlertsEnabled, tripUpdatesEnabled, shipmentUpdatesEnabled );

        return response;
    }

    @Override
    public TemplateDto.Response toResponse(NotificationTemplate template) {
        if ( template == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String titleTemplate = null;
        String bodyTemplate = null;
        String channel = null;
        String type = null;

        id = template.getId();
        name = template.getName();
        titleTemplate = template.getTitleTemplate();
        bodyTemplate = template.getBodyTemplate();
        channel = template.getChannel();
        type = template.getType();

        TemplateDto.Response response = new TemplateDto.Response( id, name, titleTemplate, bodyTemplate, channel, type );

        return response;
    }

    @Override
    public NotificationTemplate toEntity(TemplateDto.CreateRequest request) {
        if ( request == null ) {
            return null;
        }

        NotificationTemplate.NotificationTemplateBuilder notificationTemplate = NotificationTemplate.builder();

        notificationTemplate.name( request.name() );
        notificationTemplate.titleTemplate( request.titleTemplate() );
        notificationTemplate.bodyTemplate( request.bodyTemplate() );
        notificationTemplate.channel( request.channel() );
        notificationTemplate.type( request.type() );

        return notificationTemplate.build();
    }
}
