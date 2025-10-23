package persistence;

import model.Album;
import model.Photo;
import model.PhotoLibrary;
import model.ProblemType;
import model.Reflection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport

class JsonWriterTest extends JsonTest {
    // NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter
    // is to
    // write data to a file and then use the reader to read it back in and check
    // that we
    // read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyPhotoLibrary() {
        try {
            PhotoLibrary pl = new PhotoLibrary("My photo library");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPhotoLibrary.json");
            writer.open();
            writer.write(pl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPhotoLibrary.json");
            pl = reader.read();
            assertEquals("My photo library", pl.getLibName());
            assertEquals(0, pl.numPhotos());
            assertEquals(0, pl.numAlbums());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterPhotoLibraryWithAlbums() {
        try {
            String path = "./data/testWriterPhotoLibrary.json";
            PhotoLibrary lib = new PhotoLibrary("My photo library");
            Photo p = new Photo("cream.jpg","XT5",200,5.6,0.01,LocalDate.parse("2025-10-21"));
            Album a = new Album("Travel Album"); 
            a.addPhoto(p); 
            lib.addPhoto(p); 
            lib.addAlbum(a);
    
            JsonWriter w = new JsonWriter(path); 
            w.open(); 
            w.write(lib); 
            w.close();
    
            PhotoLibrary loaded = new JsonReader(path).read();
            assertEquals("My photo library", loaded.getLibName());
            assertEquals(1, loaded.getPhotos().size());
            assertEquals(1, loaded.getAlbums().size());
    
            checkPhoto("cream.jpg","XT5",200,5.6,0.01,LocalDate.parse("2025-10-21"),
                       loaded.getAlbums().get(0).getPhotos().get(0));
            assertEquals("Travel Album", loaded.getAlbums().get(0).getAlbumName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
    


    @Test
    void testAddReflectionIfPresentWithProblemsAndComments() {
        Photo p = new Photo("flower.jpg", "XT5", 100, 2.8, 0.005, LocalDate.now());
        Reflection r = new Reflection();
        r.setScore(100);
        r.addProblemType(ProblemType.EXPOSURE);
        r.addProblemType(ProblemType.SHARPNESS);
        r.addComment("Too bright");
        r.addComment("Focus slightly off");
        p.setReflection(r);

        JSONObject json = p.toJson();
        JSONObject ref = json.getJSONObject("reflection");

        assertEquals(100, ref.getInt("score"));
        JSONArray probs = ref.getJSONArray("problems");
        JSONArray comms = ref.getJSONArray("comments");

        assertTrue(probs.toList().contains("EXPOSURE"));
        assertTrue(probs.toList().contains("SHARPNESS"));
        assertTrue(comms.toList().contains("Too bright"));
        assertTrue(comms.toList().contains("Focus slightly off"));
    }
    

}
