package com.smartlogistics.userservice.service.storage;

import com.smartlogistics.userservice.exception.InvalidDocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MockStorageService implements StorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    @Override
    public String uploadFile(String directory, String fileName, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidDocumentException("File is empty or missing");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidDocumentException("File size exceeds maximum limit of 5 MB");
        }

        String contentType = file.getContentType();

        if ("photos".equalsIgnoreCase(directory)) {
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                throw new InvalidDocumentException("Unsupported image type. Supported formats are: JPEG, PNG, WEBP");
            }
        }

        String mockUrl = String.format("https://s3.amazonaws.com/smartlogistics-platform/%s/%s_%s",
                directory, UUID.randomUUID().toString(), file.getOriginalFilename());

        log.info("Mock uploaded file to S3: {}", mockUrl);
        return mockUrl;
    }

    @Override
    public void deleteFile(String fileUrl) {
        log.info("Mock deleted file from S3: {}", fileUrl);
    }
}
