package com.smartlogistics.shared.util;

import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
public class MockStorageService implements StorageService {

    @Override
    public String uploadFile(String bucketName, String key, InputStream fileStream, String contentType, long contentLength) {
        long maxSize = contentType.startsWith("image/") ? 5 * 1024 * 1024 : 10 * 1024 * 1024;
        if (contentLength > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum permitted limit (" + (maxSize / (1024 * 1024)) + "MB)");
        }
        if (contentType.startsWith("image/") && !contentType.matches("image/(jpeg|jpg|png|webp)")) {
            throw new IllegalArgumentException("Unsupported image format. Allowed formats: JPEG, JPG, PNG, WEBP");
        }
        return "https://mock-s3-bucket.s3.amazonaws.com/" + bucketName + "/" + key;
    }

    @Override
    public void deleteFile(String bucketName, String key) {
        // Mock deletion logic
    }
}
