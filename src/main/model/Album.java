package model;

import java.util.ArrayList;

// the album of photos
public class Album {
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
        }
    }

    // MODIFIES: this
    // EFFECTS: remove the given photo from the photo library if contains
    public void removePhoto(Photo p) {
        this.photos.remove(p);

    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }




}
