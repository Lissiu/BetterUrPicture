package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAlbum {
    private Album testAlbum;
    private Photo p1;
    private Photo p2;
    private AlbumCollection a1;
    
    @BeforeEach
    void runBefore() {
        testAlbum = new Album("Sun", LocalDate.of(2001, 1, 20));
        p1 = new Photo("flower", "a6300", 400, 6.3, 1/400);
        p2 = new Photo("cloud", "a6300", 200, 2.8, 1/1000);
        a1 = new AlbumCollection("C");
    }

    @Test
    void testConstructor() {
        assertEquals("Sun", testAlbum.getAlbumName());
        assertEquals(LocalDate.of(2001, 1, 20), testAlbum.getDate());
    }

    @Test
    void testAddOnePhoto() {
        testAlbum.addPhoto(p1);
        assertEquals(1, testAlbum.getPhotos().size());
        assertEquals(p1, testAlbum.getPhotos().get(0));
    }

    @Test
    void testAddMultipleDifferentPhotos() {
        testAlbum.addPhoto(p1);
        testAlbum.addPhoto(p2);
        assertEquals(2, testAlbum.getPhotos().size());
        assertEquals(p1, testAlbum.getPhotos().get(0));
        assertEquals(p2, testAlbum.getPhotos().get(1));

    }

    @Test
    void testAddMultipleSamePhotos() {
        testAlbum.addPhoto(p1);
        testAlbum.addPhoto(p1);
        assertEquals(1, testAlbum.getPhotos().size());
        assertEquals(p1, testAlbum.getPhotos().get(0));

    }


    @Test
    void testRemoveOnePhoto() {
        testAlbum.addPhoto(p1);
        testAlbum.removePhoto(p1);
        assertEquals(0, testAlbum.getPhotos().size());
    }

    @Test
    void testRemoveMultiplePhotosInOrder() {
        testAlbum.addPhoto(p1);
        testAlbum.addPhoto(p2);
        testAlbum.removePhoto(p1);
        assertEquals(1, testAlbum.getPhotos().size());
        assertEquals(p2, testAlbum.getPhotos().get(0));
        testAlbum.removePhoto(p2);
        assertEquals(0, testAlbum.getPhotos().size());

    }

    @Test
    void testRemoveMultiplePhotosNotInOrder() {
        testAlbum.addPhoto(p1);
        testAlbum.addPhoto(p2);
        testAlbum.removePhoto(p2);
        assertEquals(1, testAlbum.getPhotos().size());
        assertEquals(p1, testAlbum.getPhotos().get(0));
        testAlbum.removePhoto(p1);
        assertEquals(0, testAlbum.getPhotos().size());

    }

}
