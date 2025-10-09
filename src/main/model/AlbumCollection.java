package model;

import java.util.ArrayList;
// the collection of photo albums
public class AlbumCollection {
    private ArrayList<Album> albums;
    private String collectionName;


    // EFFECTS: construct a session of photos with given name
    public AlbumCollection(String n) {

    }

    // MODIFIES: this
    // EFFECTS: add the given album to the album collection
    public ArrayList<Album> addAlbum() {
        return albums;
    }

    // MODIFIES: this
    // EFFECTS: remove the given album from the album collection
    public ArrayList<Album> removeAlbum() {
        return albums;
    }

}
