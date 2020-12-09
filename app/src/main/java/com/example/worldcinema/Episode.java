package com.example.worldcinema;

public class Episode {
    private String name;
    private String description;
    private String preview;
    private String year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Episode(String name, String description, String preview, String year){
        this.description = description;
        this.name = name;
        this.preview = preview;
        this.year = year;
    }
}
