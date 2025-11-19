package ui.gui.adapters;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Stores extra data that is not part of the core model, such as
// photo file paths and album reflection texts, in simple JSON files.
@ExcludeFromJacocoGeneratedReport
public class SidecarStores {
    private static final String PATHS_FILE = "./data/photopaths.json";
    private static final String ALBUM_REFL_FILE = "./data/album_reflections.json";

    private final Map<String, String> photoPath = new HashMap<>();
    private final Map<String, String> albumRefl = new HashMap<>();

    // EFFECTS: returns the stored path for the given photo name, or
    // the empty string if none is stored.
    public String getPhotoPath(String photoName) {
        return photoPath.getOrDefault(photoName, "");
    }

    // MODIFIES: this
    // EFFECTS: associates the given path with the given photo name.
    public void putPhotoPath(String photoName, String path) {
        photoPath.put(photoName, path);
    }

    // MODIFIES: this
    // EFFECTS: removes any path entry for the given photo name.
    public void removePhotoPath(String photoName) {
        photoPath.remove(photoName);
    }

    // EFFECTS: returns the stored reflection text for the album, or
    // the empty string if none is stored.
    public String getAlbumReflection(String albumName) {
        return albumRefl.getOrDefault(albumName, "");
    }

    // MODIFIES: this
    // EFFECTS: stores the given reflection text for the album name;
    // null text is stored as an empty string.
    public void putAlbumReflection(String albumName, String text) {
        albumRefl.put(albumName, text == null ? "" : text);
    }

    // MODIFIES: this
    // EFFECTS: removes any reflection entry for the given album name.
    public void removeAlbumReflection(String albumName) {
        albumRefl.remove(albumName);
    }

    // MODIFIES: this
    // EFFECTS: if a reflection is stored under oldName, moves it to
    // newName; otherwise does nothing.
    public void renameAlbumKey(String oldName, String newName) {
        if (oldName == null || newName == null || !albumRefl.containsKey(oldName)) {
            return;
        }
        String v = albumRefl.remove(oldName);
        albumRefl.put(newName, v);
    }

    // MODIFIES: this
    // EFFECTS: loads photoPath and albumRefl maps from their JSON files
    // if the files exist; if a file does not exist, the
    // corresponding map is left empty.
    public void loadAll() {
        loadJson(PATHS_FILE, photoPath);
        loadJson(ALBUM_REFL_FILE, albumRefl);
    }

    // MODIFIES: filesystem
    // EFFECTS: writes the photoPath and albumRefl maps to their JSON
    // files; IOExceptions are ignored.
    public void saveAll() {
        saveJson(PATHS_FILE, photoPath);
        saveJson(ALBUM_REFL_FILE, albumRefl);
    }

    // MODIFIES: map
    // EFFECTS: clears the map and fills it with key value pairs from
    // the JSON object in the given file if the file exists;
    // if the file does not exist, the map is left empty;
    // IOExceptions are ignored.
    private void loadJson(String path, Map<String, String> map) {
        map.clear();
        File f = new File(path);
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String json = br.lines().reduce("", (a, b) -> a + b).trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1).trim();
                if (!json.isEmpty()) {
                    fillMapFromJson(json, map);
                }
            }
        } catch (IOException ignored) {
            // ignored
        }
    }

    private void fillMapFromJson(String json, Map<String, String> map) {
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String kv : pairs) {
            String[] arr = kv.split(":", 2);
            if (arr.length == 2) {
                String k = arr[0].trim().replaceAll("^\"|\"$", "");
                String v = arr[1].trim().replaceAll("^\"|\"$", "");
                v = v.replace("\\n", "\n");
                map.put(k, v);
            }
        }
    }

    // MODIFIES: filesystem
    // EFFECTS: writes the map as a single JSON object into the given
    // file; IOExceptions are ignored.
    private void saveJson(String path, Map<String, String> map) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, String> e : map.entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                String k = e.getKey().replace("\"", "'");
                String v = e.getValue().replace("\n", "\\n").replace("\"", "'");
                sb.append("\"").append(k).append("\":\"").append(v).append("\"");
            }
            sb.append("}");
            bw.write(sb.toString());
        } catch (IOException ignored) {
            // ignore
        }
    }
}
