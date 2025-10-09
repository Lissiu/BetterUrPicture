package model;

import java.util.ArrayList;
import java.util.Date;
// the album of photos
public class Album {
    private String albumName;
    private Date date;
    private ArrayList<Photo> photos;

    // EFFECTS: construct a session of photos with date and name recorded
    public Album() {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: adds the given photo to the album
    public ArrayList<Photo> addPhoto() {
        return photos;
    }

    // MODIFIES: this
    // EFFECTS: removes the given photo from the album
    public ArrayList<Photo> removePhoto() {
        return photos;
    }

    // MODIFIES: this
    // EFFECTS: generate a new classified album based on the common issues from the given album collections
    public ArrayList<Photo> findCommonPhotos() {

        return photos; // stub
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

}
