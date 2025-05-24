package com.ajouton_2.server.api.dto.file;

public record PresignedUrlResponse(
        String fileName,
        String uploadUrl
) {}