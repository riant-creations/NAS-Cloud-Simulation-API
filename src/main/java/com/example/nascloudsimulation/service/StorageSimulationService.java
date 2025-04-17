package com.example.nascloudsimulation.service;

import com.example.nascloudsimulation.model.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StorageSimulationService {

    // For demo purposes, using an in-memory storage
    private final Map<Long, FileInfo> fileStorage = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    // Upload file: accepts file and its category; defaults storageType to "NAS"
    public FileInfo uploadFile(MultipartFile file, String category) throws IOException {
        Long id = idCounter.incrementAndGet();
        Path uploadDir = Paths.get("uploads/");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Save file locally (you might want to improve error handling here)
        Path filePath = uploadDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create file record with category; default storageType: "NAS", archived: false
        FileInfo fileInfo = new FileInfo(id, file.getOriginalFilename(), category, "NAS", false);
        fileStorage.put(id, fileInfo);
        return fileInfo;
    }

    // Return all files
    public List<FileInfo> getAllFiles() {
        return new ArrayList<>(fileStorage.values());
    }

    // Return files grouped by category
    // Group active (non-archived) files by their category,
    // and archived files under a separate "archived" key.
    public Map<String, List<FileInfo>> getFilesByCategory() {
        Map<String, List<FileInfo>> groupedFiles = new HashMap<>();
        // For active files, group by their category
        for (FileInfo file : fileStorage.values()) {
            if (!file.isArchived()) {
                String cat = file.getCategory();
                groupedFiles.computeIfAbsent(cat, k -> new ArrayList<>()).add(file);
            }
        }
        // For archived files, add them under a key "archived"
        List<FileInfo> archivedFiles = new ArrayList<>();
        for (FileInfo file : fileStorage.values()) {
            if (file.isArchived()) {
                archivedFiles.add(file);
            }
        }
        if (!archivedFiles.isEmpty()) {
            groupedFiles.put("archived", archivedFiles);
        }
        return groupedFiles;
    }

    // Get a single file by id (used for download, etc.)
    public FileInfo getFile(Long id) {
        return fileStorage.get(id);
    }

    // Archive a file: mark file as archived and change storageType to "Cloud"
    public boolean archiveFile(Long id) {
        FileInfo file = fileStorage.get(id);
        if (file != null) {
            file.setArchived(true);
            file.setStorageType("Cloud");
            return true;
        }
        return false;
    }

    // Delete a file
    public boolean deleteFile(Long id) {
        return fileStorage.remove(id) != null;
    }
}