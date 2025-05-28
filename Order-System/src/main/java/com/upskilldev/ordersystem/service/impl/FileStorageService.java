package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.config.FileStorageProperties;
import com.upskilldev.ordersystem.exception.FileStorageException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties props) {
        this.fileStorageLocation = Paths.get(props.getUploadDir()).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory at " + this.fileStorageLocation, e);
        }
    }

    /**
     * Stores the file and returns the filename (not full path).
     */
    public String storeFile(MultipartFile file) {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int idx = original.lastIndexOf(".");
        if (idx > 0) ext = original.substring(idx);

        String fileName = UUID.randomUUID().toString() + ext;

        try {
            if (original.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + original);
            }
            Path target = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }

    /**
     * Resolve a stored filename to a Path on disk.
     */
    public Path loadFileAsPath(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }


}
