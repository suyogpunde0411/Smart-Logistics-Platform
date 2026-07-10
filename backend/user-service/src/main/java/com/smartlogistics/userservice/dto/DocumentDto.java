package com.smartlogistics.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class DocumentDto {

    public record DocumentResponse(
            UUID id,
            String type,
            String url,
            String status
    ) {}

    public record DocumentUploadRequest(
            @NotBlank(message = "Document type is mandatory")
            String documentType // LICENSE, AADHAAR, PAN, GST, COMPANY_REG
            // Note: The actual file will be passed as a MultipartFile in the controller,
            // not within this JSON DTO, but we might receive a JSON part alongside.
    ) {}
}
