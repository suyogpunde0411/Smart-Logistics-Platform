package com.smartlogistics.notificationservice.mapper;

import com.smartlogistics.notificationservice.dto.NotificationDto;
import com.smartlogistics.notificationservice.dto.PreferenceDto;
import com.smartlogistics.notificationservice.dto.TemplateDto;
import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.entity.NotificationPreference;
import com.smartlogistics.notificationservice.entity.NotificationTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationDto.Response toResponse(Notification notification);

    PreferenceDto.Response toResponse(NotificationPreference preference);

    TemplateDto.Response toResponse(NotificationTemplate template);

    NotificationTemplate toEntity(TemplateDto.CreateRequest request);
}
