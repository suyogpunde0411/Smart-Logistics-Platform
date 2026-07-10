package com.smartlogistics.shared.util;

import java.io.InputStream;

public interface StorageService {
    String uploadFile(String bucketName, String key, InputStream fileStream, String contentType, long contentLength);
    void deleteFile(String bucketName, String key);
}
