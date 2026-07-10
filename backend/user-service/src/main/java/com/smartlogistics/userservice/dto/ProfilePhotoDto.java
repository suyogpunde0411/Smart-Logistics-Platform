package com.smartlogistics.userservice.dto;

import java.util.UUID;

public class ProfilePhotoDto {

    public record ProfilePhotoResponse(
            UUID id,
            String url,
            String contentType
    ) {}
}
