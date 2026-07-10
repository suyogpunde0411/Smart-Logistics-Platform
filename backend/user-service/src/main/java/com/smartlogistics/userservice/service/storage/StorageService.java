package com.smartlogistics.userservice.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(String directory, String fileName, MultipartFile file);
    void deleteFile(String fileUrl);
}
