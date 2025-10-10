package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

public class TestAlbumCollection {
    private AlbumCollection tesAlbumCollection;
    private Album a1;
    private Album a2;
    private Photo p1;
    private Photo p2;
    private Photo p3;
    private Photo p4;

    @BeforeEach
    void runBefore() {
        tesAlbumCollection = new AlbumCollection("all");
        a1 = new Album("A1", LocalDate.of(2020, 04, 26));
        a2 = new Album("A2", LocalDate.of(2021, 04, 26));
        p1 = new Photo("shine", "80D", 500, 18, 1 / 800);
        p2 = new Photo("dim", "80D", 1600, 18, 1 / 80);
        p3 = new Photo("rain", "80D", 1600, 18, 1 / 80);
        p4 = new Photo("wind", "80D", 1600, 18, 1 / 900);

    }

    @Test
    void testConstructor() {
        assertEquals("all", tesAlbumCollection.getCollectionName());

    }

    @Test
    void testAddOneAlbum() {
        tesAlbumCollection.addAlbum(a1);
        assertEquals(a1, tesAlbumCollection.getAlbums().get(0));
        assertEquals(1, tesAlbumCollection.getAlbums().size());
    }

    @Test
    void testAddMultipleAlbums() {
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a2);
        assertEquals(a1, tesAlbumCollection.getAlbums().get(0));
        assertEquals(a2, tesAlbumCollection.getAlbums().get(1));
        assertEquals(2, tesAlbumCollection.getAlbums().size());
    }

    @Test
    void testAddMultipleSameAlbum() {
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a1);
        assertEquals(a1, tesAlbumCollection.getAlbums().get(0));
        assertEquals(1, tesAlbumCollection.getAlbums().size());
    }

    @Test
    void testRemoveOneAlbum() {
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a2);
        tesAlbumCollection.removeAlbum(a1);
        assertEquals(a2, tesAlbumCollection.getAlbums().get(0));
        assertEquals(1, tesAlbumCollection.getAlbums().size());
    }

    @Test
    void testRemoveMultipleAlbums() {
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a2);
        tesAlbumCollection.removeAlbum(a1);
        tesAlbumCollection.removeAlbum(a2);
        assertEquals(0, tesAlbumCollection.getAlbums().size());
    }

    @Test
    void testRemoveMultipleSameAlbums() {
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a2);
        tesAlbumCollection.removeAlbum(a1);
        tesAlbumCollection.removeAlbum(a1);
        assertEquals(1, tesAlbumCollection.getAlbums().size());
        assertEquals(a2, tesAlbumCollection.getAlbums().get(0));
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
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a2);
        ArrayList<Photo> expected = new ArrayList<Photo>();
        expected.add(p1);
        expected.add(p2);
        assertEquals(expected, tesAlbumCollection.findCommonPhotos(ProblemType.EXPOSURE));

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
        tesAlbumCollection.addAlbum(a1);
        tesAlbumCollection.addAlbum(a2);
        ArrayList<Photo> expected = new ArrayList<Photo>();
        expected.add(p1);
        expected.add(p3);
        assertEquals(expected, tesAlbumCollection.findCommonPhotos(ProblemType.EXPOSURE));

    }
}
