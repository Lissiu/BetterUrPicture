package model;
// photo imported by the user
public class Photo {
    private String photoname;
    private String camera;
    private String scenedescription;
    private String reflection;
    private int iso;
    private int aperture;
    private int shutterspeed;
    private int score;
    private Boolean reflect;



    // REQUIRES: iso, aperture, shutterspeed> 0
    // EFFECTS: create a photo with information recorded
    public Photo() {
        //stub
    }


    public void setPhotoname(String photoname) {
        this.photoname = photoname;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public void setScenedescription(String scenedescription) {
        this.scenedescription = scenedescription;
    }

    public void setReflection(String reflection) {
        this.reflection = reflection;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public void setAperture(int aperture) {
        this.aperture = aperture;
    }

    public void setShutterspeed(int shutterspeed) {
        this.shutterspeed = shutterspeed;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getScenedescription() {
        return scenedescription;
    }

    public String getReflection() {
        return reflection;
    }

    public int getShutterspeed() {
        return shutterspeed;
    }

    public int getAperture() {
        return aperture;
    }

    public int getScore() {
        return score;
    }

}
