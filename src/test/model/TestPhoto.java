package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPhoto {
    private Photo testPhoto;
    private Reflection ref;

    
    @BeforeEach
    void runBefore() {
        testPhoto = new Photo("flower", "a6300", 400, 6.3, 1 / 400, LocalDate.of(2001, 1, 20));
        ref = new Reflection();

    }

    @Test
    void testConstructor() {
        assertEquals("flower", testPhoto.getPhotoname());
        assertEquals("a6300", testPhoto.getCamera());
        assertEquals(400, testPhoto.getIso());
        assertEquals(6.3, testPhoto.getAperture());
        assertEquals(1 / 400, testPhoto.getShutterspeed());
        assertEquals(LocalDate.of(2001, 1, 20), testPhoto.getDate());

    }

    @Test
    void testSetReflection() {
        testPhoto.setReflection(ref);
        assertTrue(testPhoto.getReflect());

    }

}
