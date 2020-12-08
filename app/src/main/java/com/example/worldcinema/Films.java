package com.example.worldcinema;

public class Films {
    private String photo;
    private String name;
    private String tag;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Films(String photo, String name, String tag){
        this.photo = photo;
        this.name = name;
        this.tag = tag;
    }
}
