package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// the collection of all photos and albums
public class PhotoLibrary implements Writable {
    String libName;
    private ArrayList<Photo> photos;
    private ArrayList<Album> albums;

    // EFFECTS: construct an empty session of photos
    public PhotoLibrary(String libName) {
        this.libName = libName;
        this.photos = new ArrayList<Photo>();
        this.albums = new ArrayList<Album>();

    }

    // MODIFIES: this
    // EFFECTS: add the given photo to the photo library if not already in the
    // collection
    public void addPhoto(Photo p) {
        if (!this.photos.contains(p)) {
            this.photos.add(p);
            EventLog.getInstance().logEvent(new Event("Add photo to library: " + p.getPhotoname()));
        }
    }

    // MODIFIES: this
    // EFFECTS: remove the given photo from the photo library if contains
    public void removePhoto(Photo p) {
        this.photos.remove(p);
        EventLog.getInstance().logEvent(new Event("Remove photo frim library: " + p.getPhotoname()));

    }

    // MODIFIES: this
    // EFFECTS: add the given album to the photo library if not already in the
    // collection
    public void addAlbum(Album a) {
        if (!this.albums.contains(a)) {
            this.albums.add(a);
            EventLog.getInstance().logEvent(new Event("Add album: " + a.getAlbumName()));
        }
    }

    // MODIFIES: this
    // EFFECTS: remove the given album from the photo library if contains
    public void removeAlbum(Album a) {
        this.albums.remove(a);
        EventLog.getInstance().logEvent(new Event("Remove album: " + a.getAlbumName()));

    }

    // EFFECTS: generate a new classified album based on the common issues from the
    // given photo library
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



    public ArrayList<Album> getAlbums() {
        return albums;
    }



    public ArrayList<Photo> getPhotos() {
        return photos;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", libName);
        json.put("photos", photosToJson());
        json.put("albums", albumsToJson());
        return json;
    }

    // EFFECTS: returns things in this library as a JSON array
    private JSONArray photosToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Photo p : photos) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns things in this library as a JSON array
    private JSONArray albumsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Album a : albums) {
            jsonArray.put(a.toJson());
        }

        return jsonArray;
    }

    public Integer numPhotos() {
        return this.photos.size();
    }

    public Integer numAlbums() {
        return this.albums.size();
    }

}
