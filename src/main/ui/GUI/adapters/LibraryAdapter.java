package ui.gui.adapters;

import model.Album;
import model.Photo;
import model.PhotoLibrary;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;


// Adapter between the GUI and the PhotoLibrary model.
// Provides helper methods to access albums and photos,
// manage sidecar data such as photo paths and album reflections, 
// and save or load the whole library from disk.
@ExcludeFromJacocoGeneratedReport
public class LibraryAdapter {
    private static final String JSON_STORE = "./data/photolibrary.json";

    private PhotoLibrary lib = new PhotoLibrary("my library");
    private final JsonReader reader = new JsonReader(JSON_STORE);
    private final JsonWriter writer = new JsonWriter(JSON_STORE);
    private final SidecarStores sidecars = new SidecarStores();

    // Basic view methods



// EFFECTS:  returns the list of albums in the library.
    public List<Album> albums() {
        return lib.getAlbums();
    }

// EFFECTS:  returns the list of all photos in the library.
    public List<Photo> allPhotos() {
        return lib.getPhotos();
    }


// EFFECTS:  returns a label of the form "name (count)" for the album.
    public String albumLabel(Album a) {
        return a.getAlbumName() + " (" + a.getPhotos().size() + ")";
    }

    // Photo path sidecar


// EFFECTS:  returns the stored file path for the given photo name,
//          or the empty string if none is stored.
    public String getPhotoPath(Photo p) {
        return sidecars.getPhotoPath(p.getPhotoname());
    }



// MODIFIES: sidecars
// EFFECTS:  stores the given absolute path for the given photo name.
    public void setPhotoPath(Photo p, String absPath) {
        sidecars.putPhotoPath(p.getPhotoname(), absPath);
    }



//  MODIFIES: sidecars
// EFFECTS:  removes any stored path for the given photo name.
    public void removePhotoPath(Photo p) {
        sidecars.removePhotoPath(p.getPhotoname());
    }




// REQUIRES: iso > 0, aperture > 0, shutter > 0
// MODIFIES: lib, sidecars
// EFFECTS:  creates a new Photo with the given metadata, adds it to the
//          library, stores the photo path in the sidecar, and returns it.
    public Photo addImportedPhoto(String photoname, String absPath,
                                  String camera, int iso, double aperture,
                                  double shutter, LocalDate date) {
        Photo p = new Photo(photoname, camera, iso, aperture, shutter, date);
        lib.addPhoto(p);
        setPhotoPath(p, absPath);
        return p;
    }




// REQUIRES: trimmed length > 0 and no existing album has the same name ignoring case
// MODIFIES: lib
// EFFECTS:  creates a new album with the given name and adds it to the
//           library; throws IllegalArgumentException if name is invalid
//           or already exists.
    public void createAlbum(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Album name cannot be empty.");
        }
        for (Album a : lib.getAlbums()) {
            if (a.getAlbumName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Album name exists.");
            }
        }
        lib.addAlbum(new Album(name.trim()));
    }



// MODIFIES: lib, sidecars
// EFFECTS:  removes the album from the library.
    public void removeAlbum(Album a) {
        lib.removeAlbum(a);
    }



// MODIFIES: lib, sidecars
// EFFECTS:  removes the photo from all albums and from the library,
//            and removes its stored path from the sidecar.
    public void removePhotoFromLibrary(Photo p) {
        for (Album a : lib.getAlbums()) {
            a.removePhoto(p);
        }
        lib.removePhoto(p);
        removePhotoPath(p);
    }


// MODIFIES: filesystem
// EFFECTS:  writes the current PhotoLibrary to JSON_STORE and saves all
//            sidecar data; throws RuntimeException if the main file
//          cannot be opened for writing.
    public void saveAll() {
        try {
            writer.open();
            writer.write(lib);
            writer.close();
            sidecars.saveAll();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Save failed: " + e.getMessage(), e);
        }
    }


// MODIFIES: lib, sidecars
// EFFECTS:  loads the PhotoLibrary from JSON_STORE if possible and
//           loads sidecar data; if the main file cannot be read, keeps
//           an empty library and still tries to load sidecar data.
    public void loadAll() {
        try {
            lib = reader.read();
            sidecars.loadAll();
        } catch (IOException e) {
            sidecars.loadAll();
        }
    }



// MODIFIES: album
// EFFECTS:  removes the photo from the given album if present;
//         does not modify sidecar data.
    public void removePhotoFromAlbum(Album album, Photo photo) {
        if (album == null || photo == null) {
            return;
        }
        album.removePhoto(photo);
    }


// REQUIRES: trimmed length > 0; no other album has the same name ignoring case
// MODIFIES: lib, sidecars
// EFFECTS: creates a new album with the new name and the same photos,
//          replaces the old album in the library list; throws
//          IllegalArgumentException if the new name is invalid or
//          already in use.
    public void renameAlbum(Album album, String newName) {
        if (album == null) {
            return;
        }
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("New name cannot be empty.");
        }
        String nn = newName.trim();

        for (Album a : albums()) {
            if (a != album && a.getAlbumName().equalsIgnoreCase(nn)) {
                throw new IllegalArgumentException("Album name already exists.");
            }
        }

        Album repl = new Album(nn);
        repl.getPhotos().addAll(album.getPhotos());

        List<Album> list = albums();
        int idx = list.indexOf(album);
        if (idx >= 0) {
            list.set(idx, repl);
        } else {
            list.add(repl);
        }
    }
}

