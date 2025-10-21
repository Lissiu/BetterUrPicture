package model;

import java.util.ArrayList;

// the collection of all photos and albums
public class PhotoLibrary {
    String libName;
    private ArrayList<Photo> photos;
    private ArrayList<Album> albums;

    // EFFECTS: construct an empty session of photos
    public PhotoLibrary(String libName) {
        this.libName = libName;
        this.photos = new ArrayList<Photo>();

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

    // MODIFIES: this
    // EFFECTS: add the given album to the photo library if not already in the
    // collection
    public void addAlbum(Album a) {
        if (!this.albums.contains(a)) {
            this.albums.add(a);
        }
    }

    // MODIFIES: this
    // EFFECTS: remove the given album from the photo library if contains
    public void removeAlbum(Album a) {
        this.albums.remove(a);

    }

    // MODIFIES: Album
    // EFFECTS: generate a new classified album based on the common issues from the
    // given album collections
    public ArrayList<Photo> findCommonPhotos(ProblemType t) {
        ArrayList<Photo> classifiedAlbum = new ArrayList<>();
        for (Photo p : photos) {
            Reflection r = p.getReflection();
            if (r == null) {
                continue;
            }
            for (ProblemType pt : r.getProblems()) {
                if (pt.equals(t)) {
                    classifiedAlbum.add(p);
                    break;
                }
            }
        }
        return classifiedAlbum;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

}
