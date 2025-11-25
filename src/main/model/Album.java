package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// the album of photos
public class Album implements Writable {
    private String albumName;
    private ArrayList<Photo> photos;

    // EFFECTS: construct a session of photos with date and problem type recorded
    public Album(String name) {
        this.albumName = name;
        photos = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add the given photo to the photo library if not already in the
    // collection
    public void addPhoto(Photo p) {
        if (!this.photos.contains(p)) {
            this.photos.add(p);
            EventLog.getInstance().logEvent(
                    new Event("Photo " + p.getPhotoname() + " added to album " + getAlbumName()));
            ;
        }
    }

    // MODIFIES: this
    // EFFECTS: remove the given photo from the photo library if contains
    public void removePhoto(Photo p) {
        this.photos.remove(p);
        EventLog.getInstance().logEvent(
                new Event("Photo " + p.getPhotoname() + " removed from album " + getAlbumName()));

    }

    public String getAlbumName() {
        return albumName;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("albumName", albumName);

        JSONArray photoArray = new JSONArray();
        for (Photo p : photos) {
            photoArray.put(p.toJson());
        }
        json.put("photos", photoArray);

        return json;
    }

}
