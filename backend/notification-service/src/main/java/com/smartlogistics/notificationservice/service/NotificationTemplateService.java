package com.smartlogistics.notificationservice.service;

import com.smartlogistics.notificationservice.dto.TemplateDto;
import java.util.List;
import java.util.UUID;

public interface NotificationTemplateService {
    TemplateDto.Response createTemplate(TemplateDto.CreateRequest request);
    TemplateDto.Response getTemplateById(UUID id);
    TemplateDto.Response getTemplateByName(String name);
    List<TemplateDto.Response> getAllTemplates();
    void deleteTemplate(UUID id);
}
