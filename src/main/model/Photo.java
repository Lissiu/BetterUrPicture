package model;
// photo imported by the user
public class Photo {
    private String photoname;
    private String camera;
    private String scenedescription;
    private Reflection reflection;
    private int iso;
    private double aperture;
    private double shutterspeed;
    private int score;
    private Boolean reflect;



    // REQUIRES: iso, aperture, shutterspeed> 0
    // EFFECTS: create a photo with information recorded
    public Photo(String photoname, String camera, int iso, double aperture, double shutterspeed) {
        this.photoname = photoname;
        this.camera = camera;
        this.iso = iso;
        this.aperture = aperture;
        this.shutterspeed = shutterspeed;
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

    public void setReflection(Reflection reflection) {
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

    public Reflection getReflection() {
        return reflection;
    }

    public double getShutterspeed() {
        return shutterspeed;
    }

    public double getAperture() {
        return aperture;
    }

    public int getScore() {
        return score;
    }

}
