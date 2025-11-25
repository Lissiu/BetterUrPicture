package model;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// photo imported by the user

public class Photo implements Writable {
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
        this.reflect = false;
        this.reflection = null;

    }

    // EFFECTS: write the reflection for photo and set the photo to be reflected
    public void setReflection(Reflection reflection) {
        this.reflection = reflection;
        setReflect(true);
        EventLog.getInstance().logEvent(
                new Event("Reflection updated for photo " + getPhotoname()));

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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        addBasicFields(json);
        addReflectionIfPresent(json);
        return json;
    }

    // EFFECTS: adds simple scalar fields
    private void addBasicFields(JSONObject json) {
        json.put("photoname", photoname);
        json.put("camera", camera);
        json.put("iso", iso);
        json.put("aperture", aperture);
        json.put("shutterspeed", shutterspeed);
        json.put("date", date);
        json.put("reflect", reflect);
    }

    // EFFECTS: adds reflection object if not null
    private void addReflectionIfPresent(JSONObject json) {
        if (reflection == null) {
            return;
        }
        JSONObject ref = new JSONObject();
        ref.put("score", reflection.getScore());
        ref.put("problems", buildProblemsArray());
        ref.put("comments", buildCommentsArray());
        json.put("reflection", ref);
    }

    // EFFECTS: builds JSONArray for problems
    private JSONArray buildProblemsArray() {
        JSONArray probs = new JSONArray();
        for (ProblemType pt : reflection.getProblems()) {
            probs.put(pt.name());
        }
        return probs;
    }

    // EFFECTS: builds JSONArray for comments
    private JSONArray buildCommentsArray() {
        JSONArray comms = new JSONArray();
        for (String c : reflection.getComments()) {
            comms.put(c);
        }
        return comms;
    }

}
