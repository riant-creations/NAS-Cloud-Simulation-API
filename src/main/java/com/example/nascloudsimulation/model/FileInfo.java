package com.example.nascloudsimulation.model;

public class FileInfo {
    private Long id;
    private String name;
    private String category;  // New: Category for the file (e.g., "course-form", "waec-result")
    private String storageType; // "NAS" by default; later "Cloud" when archived
    private boolean archived;  // false when first uploaded, true when archived

    public FileInfo() { }

    public FileInfo(Long id, String name, String category, String storageType, boolean archived) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.storageType = storageType;
        this.archived = archived;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getStorageType() {
        return storageType;
    }
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
    public boolean isArchived() {
        return archived;
    }
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
