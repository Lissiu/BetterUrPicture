package ui;

import java.time.LocalDate;
import java.util.Scanner;

import model.Album;
import model.PhotoLibrary;
import model.Photo;
import model.ProblemType;
import model.Reflection;

// the running app of better your picture

public class BuPApp {
    private PhotoLibrary photoLibrary;
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
        this.photoLibrary = new PhotoLibrary("my library");
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: displays main menu of options
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tp -> add photo to library");
        System.out.println("\td -> delete photo from library");
        System.out.println("\tw -> write reflection for a photo");
        System.out.println("\tf -> find photos by problem type");
        System.out.println("\te -> edit albums");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "p":
                doAddPhoto();
                break;
            case "d":
                doRemovePhoto();
                break;
            case "w":
                doWriteReflection();
                break;
            case "f":
                doFindCommonPhotos();
                break;
            case "e":
                doEditAlbums();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new photo into the photo library
    private void doAddPhoto() {

        System.out.print("Enter photo name: ");
        String pname = input.next();

        System.out.print("Enter camera: ");
        String camera = input.next();

        System.out.print("Enter iso: ");
        int iso = Integer.parseInt(input.next());

        System.out.print("Enter aperture: ");
        int aperture = Integer.parseInt(input.next());

        System.out.print("Enter shutterspeed: ");
        int shutterSpeed = Integer.parseInt(input.next());

        System.out.print("Enter date(yyyy-mm-dd): ");
        String date = input.next();
        LocalDate lDate = LocalDate.parse(date);

        Photo p = new Photo(pname, camera, iso, aperture, shutterSpeed, lDate);
        photoLibrary.addPhoto(p);
        System.out.println("Photo added: " + pname);
    }

    // MODIFIES: this
    // EFFECTS: writes a reflection (problem + optional comment) for a selected
    // photo
    private void doWriteReflection() {
        if (photoLibrary.getPhotos().isEmpty()) {
            System.out.println("No photo available!");
            return;
        }

        Photo photo = pickPhoto();
        if (photo == null) {
            return;
        }

        Reflection r = promptReflection();
        photo.setReflection(r);
        System.out.println("Reflection saved for " + photo.getPhotoname());
    }

    // REQUIRES: photoLibrary != null
    // EFFECTS: prints photos and returns chosen photo; null if invalid
    private Photo pickPhoto() {
        for (int i = 0; i < photoLibrary.getPhotos().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getPhotos().get(i).getPhotoname());
        }
        System.out.print("Select photo index (0 ~ " + (photoLibrary.getPhotos().size() - 1) + "): ");
        int idx = input.nextInt();
        return photoLibrary.getPhotos().get(idx);
    }

    // EFFECTS: builds a Reflection (problem type + comment + score)
    private Reflection promptReflection() {
        Reflection r = new Reflection();
        r.addProblemType(readProblemType());
        input.nextLine(); // clear buffer

        addOrDeleteComment(r);
        setScore(r);

        return r;
    }

    // EFFECTS: reads a valid problem type from user
    private ProblemType readProblemType() {
        System.out.println("Problem type? (EXPOSURE / SHARPNESS / COLOR / COMPOSITION / OTHER)");
        String raw = input.next().toUpperCase();
        try {
            return ProblemType.valueOf(raw);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid type, using OTHER.");
            return ProblemType.OTHER;
        }
    }

    // MODIFIES: r
    // EFFECTS: adds a comment and optionally deletes it
    private void addOrDeleteComment(Reflection r) {
        System.out.print("Add a comment: ");
        String c = input.nextLine().trim();
        if (!c.isEmpty()) {
            r.addComment(c);
            System.out.print("Delete comment? (y/n): ");
            String ans = input.nextLine().trim().toLowerCase();
            if (ans.equals("y")) {
                r.getComments().clear();
                System.out.println("All comments deleted.");
            }
        }
    }

    // MODIFIES: r
    // EFFECTS: forces user to enter a score between 0–100
    private void setScore(Reflection r) {
        int score = -1;
        while (score < 0 || score > 100) {
            System.out.print("Set score (0–100): ");
            try {
                score = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                score = -1;
            }
        }
        r.setScore(score);
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

        var result = photoLibrary.findCommonPhotos(t);
        System.out.println("Photos with problem type " + type + ":");
        for (Photo p : result) {
            System.out.println("- " + p.getPhotoname());
        }
    }

    // MODIFIES: this
    // EFFECTS: removes a photo from library by index
    private void doRemovePhoto() {
        if (photoLibrary.getPhotos().isEmpty()) {
            System.out.println("No photo in library.");
            return;
        }
        for (int i = 0; i < photoLibrary.getPhotos().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getPhotos().get(i).getPhotoname());
        }
        System.out.print("Select photo index to delete: ");
        int idx = input.nextInt();
        if (idx < 0 || idx >= photoLibrary.getPhotos().size()) {
            System.out.println("Invalid index.");
            return;
        }
        Photo p = photoLibrary.getPhotos().get(idx);
        photoLibrary.removePhoto(p);
        System.out.println("Deleted photo: " + p.getPhotoname());
    }

    // MODIFIES: this
    // EFFECTS: provides submenu for album management
    private void doEditAlbums() {
        boolean editing = true;
        while (editing) {
            System.out.println("\nAlbum Menu:");
            System.out.println("1 -> Add new album");
            System.out.println("2 -> Remove album");
            System.out.println("3 -> View all albums");
            System.out.println("4 -> Manage photos / set type");
            System.out.println("0 -> Back");
            System.out.print("Enter choice: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    doAddAlbum();
                    break;
                case "2":
                    doRemoveAlbum();
                    break;
                case "3":
                    doViewAlbums();
                    break;
                case "4":
                    doEditAlbum();
                    break;
                case "0":
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new album to the library
    private void doAddAlbum() {
        System.out.print("Enter album name: ");
        String name = input.nextLine().trim();
        Album album = new Album(name);
        photoLibrary.addAlbum(album);
        System.out.println("Album created: " + name);
    }

    // MODIFIES: this
    // EFFECTS: removes an album by index if exists
    private void doRemoveAlbum() {
        if (photoLibrary.getAlbums().isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        System.out.println("\nSelect album to remove:");
        for (int i = 0; i < photoLibrary.getAlbums().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getAlbums().get(i).getAlbumName());
        }

        System.out.print("Enter index: ");
        try {
            int idx = Integer.parseInt(input.nextLine());
            if (idx >= 0 && idx < photoLibrary.getAlbums().size()) {
                Album removed = photoLibrary.getAlbums().remove(idx);
                System.out.println("Removed album: " + removed.getAlbumName());
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // EFFECTS: displays all albums and their photo counts
    private void doViewAlbums() {
        if (photoLibrary.getAlbums().isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        System.out.println("\nAll albums:");
        for (Album a : photoLibrary.getAlbums()) {
            System.out.println("- " + a.getAlbumName()
                    + " (" + a.getPhotos().size() + " photos)");
        }
    }

    // MODIFIES: album
    // EFFECTS: lets user edit photos in one album
    private void doEditAlbum() {
        Album album = selectAlbum();
        if (album == null) {
            return;
        }

        boolean editing = true;
        while (editing) {
            System.out.println("\nEditing: " + album.getAlbumName());
            System.out.println("1 -> Add photo");
            System.out.println("2 -> Delete photo");
            System.out.println("0 -> Back");
            System.out.print("Enter choice: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    addPhotoToAlbum(album);
                    break;
                case "2":
                    deletePhotoFromAlbum(album);
                    break;
                case "0":
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

    }

    // EFFECTS: prompts user to choose an album, returns it or null if invalid
    private Album selectAlbum() {
        if (photoLibrary.getAlbums().isEmpty()) {
            System.out.println("No albums available.");
            return null;
        }
        System.out.println("\nSelect an album:");
        for (int i = 0; i < photoLibrary.getAlbums().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getAlbums().get(i).getAlbumName());
        }
        System.out.print("Enter index: ");
        try {
            int idx = Integer.parseInt(input.nextLine());
            if (idx >= 0 && idx < photoLibrary.getAlbums().size()) {
                return photoLibrary.getAlbums().get(idx);
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        System.out.println("Invalid selection.");
        return null;
    }

    // MODIFIES: album
    // EFFECTS: adds a photo from library to album
    private void addPhotoToAlbum(Album album) {
        if (photoLibrary.getPhotos().isEmpty()) {
            System.out.println("No photos in library.");
            return;
        }
        System.out.println("\nSelect a photo to add:");
        for (int i = 0; i < photoLibrary.getPhotos().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getPhotos().get(i).getPhotoname());
        }
        System.out.print("Enter index: ");
        try {
            int idx = Integer.parseInt(input.nextLine());
            if (idx >= 0 && idx < photoLibrary.getPhotos().size()) {
                Photo p = photoLibrary.getPhotos().get(idx);
                album.addPhoto(p);
                System.out.println("Added: " + p.getPhotoname());
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // MODIFIES: album
    // EFFECTS: deletes a photo from selected album
    private void deletePhotoFromAlbum(Album album) {
        if (album.getPhotos().isEmpty()) {
            System.out.println("Album is empty.");
            return;
        }
        System.out.println("\nSelect a photo to delete:");
        for (int i = 0; i < album.getPhotos().size(); i++) {
            System.out.println(i + ": " + album.getPhotos().get(i).getPhotoname());
        }
        System.out.print("Enter index: ");
        try {
            int idx = Integer.parseInt(input.nextLine());
            if (idx >= 0 && idx < album.getPhotos().size()) {
                Photo removed = album.getPhotos().remove(idx);
                System.out.println("Deleted: " + removed.getPhotoname());
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

}
