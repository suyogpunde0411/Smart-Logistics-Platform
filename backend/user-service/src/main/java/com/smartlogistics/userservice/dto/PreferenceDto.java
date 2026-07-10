package com.smartlogistics.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PreferenceDto {

    public record PreferenceResponse(
            String language,
            String theme
    ) {}

    public record PreferenceUpdateRequest(
            @NotBlank(message = "Language is mandatory")
            @Pattern(regexp = "^(en|hi|mr)$", message = "Supported languages are: en, hi, mr")
            String language,

            @NotBlank(message = "Theme is mandatory")
            @Pattern(regexp = "^(LIGHT|DARK)$", message = "Supported themes are: LIGHT, DARK")
            String theme
    ) {}
}
