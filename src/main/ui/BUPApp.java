package ui;

import java.time.LocalDate;
import java.util.Scanner;

import model.Album;
import model.AlbumCollection;
import model.Photo;
import model.ProblemType;
import model.Reflection;

public class BuPApp {
    private AlbumCollection albumCollection;
    private Scanner input;

    public BuPApp() {
        this.runBuP();

    }

    private void runBuP() {
        boolean keepGoing = true;
        String command = null;
        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");

    }

    // MODIFIES: this
    // EFFECTS: initializes albums
    private void init() {
        this.albumCollection = new AlbumCollection("my collection");
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: displays main menu of options
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add new album");
        System.out.println("\tp -> add photo to album");
        System.out.println("\tr -> write reflection for a photo");
        System.out.println("\tf -> find photos by problem type");
        System.out.println("\tv -> view all albums");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "a":
                doAddAlbum();
                break;
            case "p":
                doAddPhoto();
                break;
            case "r":
                doWriteReflection();
                break;
            case "f":
                doFindCommonPhotos();
                break;
            case "v":
                doViewAlbums();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new album
    private void doAddAlbum() {
        System.out.print("Enter album name: ");
        String name = input.next();
        Album album = new Album(name, LocalDate.now());
        albumCollection.addAlbum(album);
        System.out.println("Album created: " + name);
    }

    // MODIFIES: this
    // EFFECTS: adds a new photo into an existing album
    private void doAddPhoto() {
        if (albumCollection.getAlbums().isEmpty()) {
            System.out.println("No albums yet! Please create one first.");
            return;
        }

        System.out.print("Enter album index (0 ~ " + (albumCollection.getAlbums().size() - 1) + "): ");
        int index = input.nextInt();
        Album selected = albumCollection.getAlbums().get(index);

        System.out.print("Enter photo name: ");
        String pname = input.next();

        Photo p = new Photo(pname, "Nikon", 100, 2.8, 1 / 100.0);
        selected.addPhoto(p);
        System.out.println("Photo added: " + pname + " to album " + selected.getAlbumName());
    }

    // MODIFIES: this
    // EFFECTS: writes a reflection (problem + optional comment) for a selected
    // photo
    private void doWriteReflection() {
        if (albumCollection.getAlbums().isEmpty()) {
            System.out.println("No albums available!");
            return;
        }

        Album album = pickAlbum();
        if (album == null || album.getPhotos().isEmpty()) {
            System.out.println("Album has no photos!");
            return;
        }

        Photo photo = pickPhoto(album);
        if (photo == null) {
            return;
        }

        Reflection r = promptReflection();
        photo.setReflection(r);
        System.out.println("Reflection saved for " + photo.getPhotoname());
    }

    // EFFECTS: prints albums and returns chosen album; null if invalid
    private Album pickAlbum() {
        var albums = albumCollection.getAlbums();
        for (int i = 0; i < albums.size(); i++) {
            System.out.println(i + ": " + albums.get(i).getAlbumName() + " (" + albums.get(i).getPhotos().size() + ")");
        }
        System.out.print("Select album index (0 ~ " + (albumCollection.getAlbums().size() - 1) + "): ");
        int idx = input.nextInt();
        return albums.get(idx);
    }

    // REQUIRES: album != null
    // EFFECTS: prints photos and returns chosen photo; null if invalid
    private Photo pickPhoto(Album album) {
        for (int i = 0; i < album.getPhotos().size(); i++) {
            System.out.println(i + ": " + album.getPhotos().get(i).getPhotoname());
        }
        System.out.print("Select photo index (0 ~ " + (album.getPhotos().size() - 1) + "): ");
        int idx = input.nextInt();
        return album.getPhotos().get(idx);
    }

    // EFFECTS: interactively builds a Reflection (problem type + optional comments)
    private Reflection promptReflection() {
        Reflection r = new Reflection();

        System.out.println("Problem type? (EXPOSURE / SHARPNESS / COLOR / COMPOSITION / OTHER)");
        String raw = input.next().toUpperCase();
        ProblemType type;
        try {
            type = ProblemType.valueOf(raw);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid type, using OTHER.");
            type = ProblemType.OTHER;
        }
        r.addProblemType(type);
        
        input.nextLine();

        System.out.print("Add a comment (blank to skip): ");
        String c = input.nextLine().trim();
        if (!c.isEmpty()) {
            r.addComment(c);
        }

        return r;
    }

    // EFFECTS: finds and lists photos by problem type
    private void doFindCommonPhotos() {
        System.out.println("Enter problem type: EXPOSURE / SHARPNESS / COLOR / COMPOSITION / OTHER");
        String type = input.next().toUpperCase();
        ProblemType t;

        try {
            t = ProblemType.valueOf(type);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid problem type!");
            return;
        }

        var result = albumCollection.findCommonPhotos(t);
        System.out.println("Photos with problem type " + type + ":");
        for (Photo p : result) {
            System.out.println("- " + p.getPhotoname());
        }
    }

    // EFFECTS: displays all albums and their photo counts
    private void doViewAlbums() {
        System.out.println("\nAll albums:");
        for (int i = 0; i < albumCollection.getAlbums().size(); i++) {
            Album a = albumCollection.getAlbums().get(i);
            System.out.println(i + ": " + a.getAlbumName() + " (" + a.getPhotos().size() + " photos)");
        }
    }
}
