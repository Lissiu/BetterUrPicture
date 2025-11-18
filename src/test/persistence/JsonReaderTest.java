package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import model.Photo;
import model.PhotoLibrary;
import model.ProblemType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            PhotoLibrary pl = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyPhotoLibrary() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPhotoLibrary.json");
        try {
            PhotoLibrary pl = reader.read();
            assertEquals("My photo library", pl.getLibName());
            assertEquals(0, pl.numPhotos());
            assertEquals(0, pl.numAlbums());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNoAlbumsArray() {
        try {
            JSONArray photos = new JSONArray()
                    .put(photoJson("a.jpg", "XT5", 100, 2.8, 0.005, "2025-10-21"));
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", photos);
            String path = "./data/testReaderNoAlbumsArray.json";
            writeJson(path, root);

            PhotoLibrary pl = new JsonReader(path).read();
            assertEquals(1, pl.getPhotos().size());
            assertEquals(0, pl.getAlbums().size());
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderAlbumWithoutPhotosField() {
        try {
            JSONObject a0 = new JSONObject().put("albumName", "Orphan");
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray())
                    .put("albums", new JSONArray().put(a0));
            String path = "./data/testReaderAlbumWithoutPhotosField.json";
            writeJson(path, root);

            PhotoLibrary pl = new JsonReader(path).read();
            assertEquals(1, pl.getAlbums().size());
            assertEquals("Orphan", pl.getAlbums().get(0).getAlbumName());
            assertEquals(0, pl.getAlbums().get(0).getPhotos().size());
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderAlbumWithPhotos() {
        try {
            JSONObject p1 = photoJson("p1.jpg", "XT5", 200, 5.6, 0.01, "2025-10-22");
            JSONObject album = new JSONObject()
                    .put("albumName", "A1")
                    .put("photos", new JSONArray().put(p1));
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p1))
                    .put("albums", new JSONArray().put(album));
            String path = "./data/testReaderAlbumWithPhotos.json";
            writeJson(path, root);

            PhotoLibrary pl = new JsonReader(path).read();
            assertEquals("A1", pl.getAlbums().get(0).getAlbumName());
            assertEquals(1, pl.getAlbums().get(0).getPhotos().size());
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderPhotoWithoutReflection() {
        try {
            JSONObject p = photoJson("noref.jpg", "XT5", 320, 3.5, 0.02, "2025-10-23");
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p))
                    .put("albums", new JSONArray());
            String path = "./data/testReaderPhotoWithoutReflection.json";
            writeJson(path, root);

            Photo photo = new JsonReader(path).read().getPhotos().get(0);

        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderReflectionOnlyScore() {
        try {
            JSONObject p = photoJson("score.jpg", "XT5", 400, 2.8, 0.01, "2025-10-24");
            p.put("reflection", refl(88, null, null));
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p));
            String path = "./data/testReaderReflectionOnlyScore.json";
            writeJson(path, root);

            Photo photo = new JsonReader(path).read().getPhotos().get(0);
            assertEquals(88, photo.getReflection().getScore());
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderReflectionEmptyArrays() {
        try {
            JSONObject p = photoJson("empty.jpg", "XT5", 160, 4.0, 0.03, "2025-10-25");
            p.put("reflection", refl(70, List.of(), List.of()));
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p));
            String path = "./data/testReaderReflectionEmptyArrays.json";
            writeJson(path, root);

            Photo photo = new JsonReader(path).read().getPhotos().get(0);
            assertTrue(photo.getReflection().getProblems().isEmpty());
            assertTrue(photo.getReflection().getComments().isEmpty());
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderReflectionProblemNameNull() {
        try {
            JSONArray probs = new JSONArray().put(JSONObject.NULL);
            JSONObject ref = new JSONObject()
                    .put("score", 50)
                    .put("problems", probs)
                    .put("comments", new JSONArray().put("ok"));
            JSONObject p = photoJson("nullprob.jpg", "XT5", 100, 2.8, 0.01, "2025-10-26");
            p.put("reflection", ref);
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p));
            String path = "./data/testReaderReflectionProblemNameNull.json";
            writeJson(path, root);

            Photo photo = new JsonReader(path).read().getPhotos().get(0);
            assertTrue(photo.getReflection().getProblems().isEmpty());
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderReflectionCommentNull() {
        try {
            JSONArray comms = new JSONArray().put(JSONObject.NULL);
            JSONObject ref = new JSONObject()
                    .put("score", 55)
                    .put("problems", new JSONArray().put("EXPOSURE"))
                    .put("comments", comms);
            JSONObject p = photoJson("nullcomm.jpg", "XT5", 200, 5.6, 0.02, "2025-10-27");
            p.put("reflection", ref);
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p));
            String path = "./data/testReaderReflectionCommentNull.json";
            writeJson(path, root);

            Photo photo = new JsonReader(path).read().getPhotos().get(0);
            assertTrue(photo.getReflection().getComments().isEmpty());
            assertTrue(photo.getReflection().getProblems().contains(ProblemType.EXPOSURE));
        } catch (IOException e) {
            fail("IO");
        }
    }

    @Test
    void testReaderFullReflection() {
        try {
            JSONObject p = photoJson("full.jpg", "XT5", 640, 3.2, 0.008, "2025-10-28");
            p.put("reflection", refl(95,
                    List.of("EXPOSURE", "COLOR"),
                    List.of("too bright", "color cast")));
            JSONObject root = new JSONObject()
                    .put("name", "Lib")
                    .put("photos", new JSONArray().put(p));
            String path = "./data/testReaderFullReflection.json";
            writeJson(path, root);

            Photo photo = new JsonReader(path).read().getPhotos().get(0);
            assertEquals(95, photo.getReflection().getScore());
            assertTrue(photo.getReflection().getProblems().contains(ProblemType.EXPOSURE));
            assertTrue(photo.getReflection().getComments().contains("color cast"));
        } catch (IOException e) {
            fail("IO");
        }
    }

    // HELPER ----------------------------------------------------

    private void writeJson(String path, JSONObject obj) throws IOException {
        try (PrintWriter out = new PrintWriter(new File(path))) {
            out.print(obj.toString(2));
        }
    }

    private JSONObject photoJson(String name, String cam, int iso,
            double ap, double ss, String date) {
        JSONObject p = new JSONObject();
        p.put("photoname", name);
        p.put("camera", cam);
        p.put("iso", iso);
        p.put("aperture", ap);
        p.put("shutterspeed", ss);
        p.put("date", date);
        return p;
    }

    private JSONObject refl(Integer score, List<String> probs, List<String> comms) {
        JSONObject r = new JSONObject();
        if (score != null) {
            r.put("score", score);
        }
        if (probs != null) {
            r.put("problems", new JSONArray(probs));
        }
        if (comms != null) {
            r.put("comments", new JSONArray(comms));
        }
        return r;
    }
}
