package edu.birzeit.zamilihotal.model;

public class Image {

    private String imageUrl;

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgURL() {
        return imageUrl;
    }

    public void setImgURL(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
