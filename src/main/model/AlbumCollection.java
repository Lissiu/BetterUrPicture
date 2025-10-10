package model;

import java.util.ArrayList;

// the collection of photo albums
public class AlbumCollection {
    private ArrayList<Album> albums;
    private String collectionName;
    private Album album;

    // EFFECTS: construct a session of photos with given name
    public AlbumCollection(String n) {

    }

        // MODIFIES: this
    // EFFECTS: add the given album to the album collection if not already in the collection
    public ArrayList<Album> addAlbum(Album a) {
        return albums;
    }

    // MODIFIES: this
    // EFFECTS: remove the given album from the album collection
    public ArrayList<Album> removeAlbum(Album a) {
        return albums;
    }

    // MODIFIES: this
    // EFFECTS: generate a new classified album based on the common issues from the
    // given album collections
    public ArrayList<Photo> findCommonPhotos(ProblemType t) {
        return null; // stub
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }




}
