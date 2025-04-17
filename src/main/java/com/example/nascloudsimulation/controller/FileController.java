package com.example.nascloudsimulation.controller;

import com.example.nascloudsimulation.model.FileInfo;
import com.example.nascloudsimulation.service.StorageSimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.nio.file.Path;
import java.util.Map;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageSimulationService storageSimulationService;

    public FileController(StorageSimulationService storageSimulationService) {
        this.storageSimulationService = storageSimulationService;
    }

    // Upload endpoint: Accepts file + category (as request parameters)
    @PostMapping("/upload")
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam("category") String category) {
        try {
            FileInfo fileInfo = storageSimulationService.uploadFile(file, category);
            return new ResponseEntity<>(fileInfo, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all files as a flat list if needed
    @GetMapping
    public ResponseEntity<List<FileInfo>> getAllFiles() {
        List<FileInfo> files = storageSimulationService.getAllFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    // Get files grouped by category
    @GetMapping("/categories")
    public ResponseEntity<Map<String, List<FileInfo>>> getFilesByCategory() {
        Map<String, List<FileInfo>> filesByCategory = storageSimulationService.getFilesByCategory();
        return new ResponseEntity<>(filesByCategory, HttpStatus.OK);
    }

    // Archive endpoint: Archive a file by id
    @PostMapping("/{id}/archive")
    public ResponseEntity<String> archiveFile(@PathVariable Long id) {
        boolean success = storageSimulationService.archiveFile(id);
        if (success) {
            return ResponseEntity.ok("File archived successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }

    // Delete endpoint: Delete a file by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        boolean success = storageSimulationService.deleteFile(id);
        if (success) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }

    // Download endpoint
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        FileInfo fileInfo = storageSimulationService.getFile(id);
        if (fileInfo == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get("uploads").resolve(fileInfo.getName()).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileInfo.getName() + "\"")
                .body(resource);
    }
}