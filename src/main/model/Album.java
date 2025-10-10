package model;

import java.util.ArrayList;
import java.time.LocalDate;

// the album of photos
public class Album {
    private String albumName;
    private LocalDate date;
    private ArrayList<Photo> photos;
    private String type;

    // EFFECTS: construct a session of photos with date and name recorded
    public Album(String name, LocalDate date) {
        this.albumName = name;
        this.date = date;
        photos = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds the given photo to the album if not already in the album
    public ArrayList<Photo> addPhoto(Photo p) {
        if (!photos.contains(p)) {
            this.photos.add(p);
        }
        return photos;
    }

    // MODIFIES: this
    // EFFECTS: removes the given photo from the album if it contains it
    public ArrayList<Photo> removePhoto(Photo p) {
        if (photos.contains(p)) {
            this.photos.remove(p);
        }

        return photos;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    // set the type of reflection of the newly generated classified album
    public void setType(String type) {
        this.type = type;
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
