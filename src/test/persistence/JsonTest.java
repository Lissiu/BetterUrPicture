package persistence;

import model.Photo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport

public class JsonTest {
    protected void checkPhoto(String name, String c, int iso, double apert, double sspeed, LocalDate d, Photo p) {
        assertEquals(name, p.getPhotoname());
        assertEquals(c, p.getCamera());
        assertEquals(iso, p.getIso());
        assertEquals(apert, p.getAperture());
        assertEquals(sspeed, p.getShutterspeed());
        assertEquals(d, p.getDate());
    }
}
