package model;

import java.util.ArrayList;

// the collection of photo albums
public class AlbumCollection {
    private ArrayList<Album> albums;
    private String collectionName;
    private Album album;

    // EFFECTS: construct a session of photos with given name and empty albumlist
    public AlbumCollection(String n) {
        this.collectionName = n;
        this.albums = null;

    }

    // MODIFIES: this
    // EFFECTS: add the given album to the album collection if not already in the
    // collection
    public ArrayList<Album> addAlbum(Album a) {
        if (!this.albums.contains(a)) {
            this.albums.add(a);
        }
        return albums;
    }

    // MODIFIES: this
    // EFFECTS: remove the given album from the album collection if contains
    public ArrayList<Album> removeAlbum(Album a) {
        if (this.albums.contains(a)) {
            this.albums.remove(a);
        }
        return albums;
    }
    

    // MODIFIES: this
    // EFFECTS: generate a new classified album based on the common issues from the
    // given album collections
    public ArrayList<Photo> findCommonPhotos(ProblemType t) {
        ArrayList<Photo> classifiedAlbum = new ArrayList<>();
        for (Album album : albums) {
            ArrayList<Photo> photos = album.getPhotos();
            for (Photo p : photos) {
                ArrayList<ProblemType> pts = p.getReflection().getProblems();
                for (ProblemType pt : pts) {
                    if (pt.equals(t)) {
                        classifiedAlbum.add(p);
                    }

                    
                }
                
            }
        }
        return classifiedAlbum; 
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
