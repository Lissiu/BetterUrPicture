package model;

import java.time.LocalDate;


// photo imported by the user

public class Photo {
    private String photoname;
    private String camera;
    private Reflection reflection;
    private int iso;
    private double aperture;
    private double shutterspeed;
    private Boolean reflect;
    private LocalDate date;



    // REQUIRES: iso, aperture, shutterspeed> 0
    // EFFECTS: create a photo with information recorded
    public Photo(String photoname, String camera, int iso, double aperture, double shutterspeed, LocalDate date) {
        this.photoname = photoname;
        this.camera = camera;
        this.iso = iso;
        this.aperture = aperture;
        this.shutterspeed = shutterspeed;
        this.reflection = new Reflection();
        this.date = date;

    }


    public void setPhotoname(String photoname) {
        this.photoname = photoname;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    //EFFECTS: write the reflection for photo and set the photo to be reflected
    public void setReflection(Reflection reflection) {
        this.reflection = reflection;
        setReflect(true);

    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    public void setShutterspeed(double shutterspeed) {
        this.shutterspeed = shutterspeed;
    }


    public Boolean getReflect() {
        return reflect;
    }

    public void setReflect(Boolean reflect) {
        this.reflect = reflect;
    }

    public int getIso() {
        return iso; // stub
    }

    public String getPhotoname() {
        return photoname;
    }

    public String getCamera() {
        return camera;
    }


    public Reflection getReflection() {
        return reflection;
    }

    public double getShutterspeed() {
        return shutterspeed;
    }

    public double getAperture() {
        return aperture;
    }



    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }
    

}
