package com.application.Services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
        String filePath = uploadDir + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return filePath;
    }

    public File getFile(String path) {
        return new File(path);
    }
}

