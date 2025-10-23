package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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


}
