package persistence;

import model.Album;
import model.Photo;
import model.ProblemType;
import model.Reflection;
import model.PhotoLibrary;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads PhotoLibrary from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads PhotoLibrary from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PhotoLibrary read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePhotoLibrary(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses PhotoLibrary from JSON object and returns it
    private PhotoLibrary parsePhotoLibrary(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        PhotoLibrary lib = new PhotoLibrary(name);
        addPhotos(lib, jsonObject);
        addAlbums(lib, jsonObject);
        return lib;
    }

    // MODIFIES: lib
    // EFFECTS: parses Photos from JSON object and adds them to PhotoLibrary
    private void addPhotos(PhotoLibrary lib, JSONObject root) {
        JSONArray arr = root.optJSONArray("photos");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject nextPhoto = arr.getJSONObject(i);
            lib.addPhoto(parsePhoto(nextPhoto));
        }
    }

    // MODIFIES: lib
    // EFFECTS: parses Albums from JSON object and adds them to PhotoLibrary
    private void addAlbums(PhotoLibrary lib, JSONObject root) {
        JSONArray arr = root.optJSONArray("albums");
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length(); i++) {
            JSONObject nextAlbum = arr.getJSONObject(i);
            addAlbum(lib, nextAlbum);
        }
    }

    // MODIFIES: pl
    // EFFECTS: parses a single album from JSON object and adds it to PhotoLibrary
    private void addAlbum(PhotoLibrary pl, JSONObject jsonObject) {
        String albumName = jsonObject.getString("albumName");
        Album album = new Album(albumName);

        JSONArray photosArray = jsonObject.optJSONArray("photos");
        if (photosArray != null) {
            for (Object obj : photosArray) {
                JSONObject nextPhoto = (JSONObject) obj;
                Photo p = parsePhoto(nextPhoto);
                album.addPhoto(p);
            }
        }

        pl.addAlbum(album);
    }

    // EFFECTS: builds and returns a Photo from the given JSON
    private Photo parsePhoto(JSONObject j) {
        String photoname = j.getString("photoname");
        String camera = j.getString("camera");
        int iso = j.getInt("iso");
        double aperture = j.getDouble("aperture");
        double shutterspeed = j.getDouble("shutterspeed");
        LocalDate date = LocalDate.parse(j.getString("date"));
        Photo p = new Photo(photoname, camera, iso, aperture, shutterspeed, date);
        JSONObject refObj = j.optJSONObject("reflection");
        if (refObj != null) {
            p.setReflection(parseReflection(refObj));
        }
        return p;
    }

    // EFFECTS: parses a Reflection object from JSON
    private Reflection parseReflection(JSONObject rj) {
        Reflection r = new Reflection();
        r.setScore(rj.optInt("score", 0));
        addProblemsFromJson(r, rj.optJSONArray("problems"));
        addCommentsFromJson(r, rj.optJSONArray("comments"));
        return r;
    }

    // MODIFIES: r
    // EFFECTS: adds ProblemTypes from JSONArray to reflection
    private void addProblemsFromJson(Reflection r, JSONArray probs) {
        if (probs == null) {
            return;
        }
        for (int i = 0; i < probs.length(); i++) {
            String name = probs.optString(i, null);
            if (name == null) {
                continue;
            }
            r.addProblemType(ProblemType.valueOf(name));

        }
    }

    // MODIFIES: r
    // EFFECTS: adds comments from JSONArray to reflection
    private void addCommentsFromJson(Reflection r, JSONArray comms) {
        if (comms == null) {
            return;
        }
        for (int i = 0; i < comms.length(); i++) {
            String c = comms.optString(i, null);
            if (c != null) {
                r.addComment(c);
            }
        }
    }

}
