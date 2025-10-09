package model;

import java.util.ArrayList;
import java.time.LocalDate;

// the album of photos
public class Album {
    private String albumName;
    private LocalDate date;
    private ArrayList<Photo> photos;

    // EFFECTS: construct a session of photos with date and name recorded
    public Album(String name, LocalDate date2) {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: adds the given photo to the album if not already in the album
    public ArrayList<Photo> addPhoto(Photo p) {
        return photos;
    }

    // MODIFIES: this
    // EFFECTS: removes the given photo from the album
    public ArrayList<Photo> removePhoto(Photo p) {
        return photos;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
