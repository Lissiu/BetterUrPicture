package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

public class TestPhotoLibrary{
    private PhotoLibrary testPhotoLibrary;
    private Album a1;
    private Album a2;
    private Photo p1;
    private Photo p2;
    private Photo p3;
    private Photo p4;
    private ArrayList photos;

    @BeforeEach
    void runBefore() {
        testPhotoLibrary = new PhotoLibrary("all");
        photos = new ArrayList<>();
        a1 = new Album("A1");
        a2 = new Album("A2");
        p1 = new Photo("shine", "80D", 500, 18, 1 / 800, LocalDate.of(2020, 04, 26));
        p2 = new Photo("dim", "80D", 1600, 18, 1 / 80, LocalDate.of(2021, 04, 26));
        p3 = new Photo("rain", "80D", 1600, 18, 1 / 80, LocalDate.of(2021, 04, 26));
        p4 = new Photo("wind", "80D", 1600, 18, 1 / 900, LocalDate.of(2021, 04, 26));

    }

    @Test
    void testConstructor() {
        assertEquals("all", testPhotoLibrary.getLibName());
        assertEquals(photos, testPhotoLibrary.getPhotos());

    }

    @Test
    void testAddOneAlbum() {
        testPhotoLibrary.addAlbum(a1);
        assertEquals(a1, testPhotoLibrary.getAlbums().get(0));
        assertEquals(1, testPhotoLibrary.getAlbums().size());
    }

    @Test
    void testAddMultipleAlbums() {
        testPhotoLibrary.addAlbum(a1);
        testPhotoLibrary.addAlbum(a2);
        assertEquals(a1, testPhotoLibrary.getAlbums().get(0));
        assertEquals(a2, testPhotoLibrary.getAlbums().get(1));
        assertEquals(2, testPhotoLibrary.getAlbums().size());
    }

    @Test
    void testAddMultipleSameAlbum() {
        testPhotoLibrary.addAlbum(a1);
        testPhotoLibrary.addAlbum(a1);
        assertEquals(a1, testPhotoLibrary.getAlbums().get(0));
        assertEquals(1, testPhotoLibrary.getAlbums().size());
    }

    @Test
    void testRemoveOneAlbum() {
        testPhotoLibrary.addAlbum(a1);
        testPhotoLibrary.addAlbum(a2);
        testPhotoLibrary.removeAlbum(a1);
        assertEquals(a2, testPhotoLibrary.getAlbums().get(0));
        assertEquals(1, testPhotoLibrary.getAlbums().size());
    }

    @Test
    void testRemoveMultipleAlbums() {
        testPhotoLibrary.addAlbum(a1);
        testPhotoLibrary.addAlbum(a2);
        testPhotoLibrary.removeAlbum(a1);
        testPhotoLibrary.removeAlbum(a2);
        assertEquals(0, testPhotoLibrary.getAlbums().size());
    }


    @Test
    void testFindCommonPhotosSameAlbum() {
        a1.addPhoto(p1);
        a1.addPhoto(p2);
        a2.addPhoto(p3);
        a2.addPhoto(p4);
        Reflection r1 = new Reflection();
        r1.addProblemType(ProblemType.EXPOSURE);
        p1.setReflection(r1);
        p2.setReflection(r1);

        ArrayList<Photo> expected = new ArrayList<Photo>();
        expected.add(p1);
        expected.add(p2);
        assertEquals(expected, testPhotoLibrary.findCommonPhotos(ProblemType.EXPOSURE));

    }

    @Test
    void testFindCommonPhotosDifferentAlbum() {
        a1.addPhoto(p1);
        a1.addPhoto(p2);
        a2.addPhoto(p3);
        a2.addPhoto(p4);
        Reflection r1 = new Reflection();
        r1.addProblemType(ProblemType.EXPOSURE);
        p1.setReflection(r1);
        p3.setReflection(r1);
        ArrayList<Photo> expected = new ArrayList<Photo>();
        expected.add(p1);
        expected.add(p3);
        assertEquals(expected, testPhotoLibrary.findCommonPhotos(ProblemType.EXPOSURE));

    }

    @Test
    void testFindCommonPhotosNull() {
        ArrayList<Photo> expected = new ArrayList<Photo>();
        assertEquals(expected, testPhotoLibrary.findCommonPhotos(ProblemType.EXPOSURE));

    }
}
