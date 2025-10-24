package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.Album;
import model.Photo;
import model.PhotoLibrary;
import model.ProblemType;
import model.Reflection;
import persistence.JsonReader;
import persistence.JsonWriter;

/**
 * The app of Better Your Picture (collects photos and reflections).
 */
@ExcludeFromJacocoGeneratedReport
public class BuPApp {
    private static final String JSON_STORE = "./data/photolibrary.json";

    private PhotoLibrary photoLibrary;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Map<String, Runnable> handlers;

    // EFFECTS: constructs the app and enters main loop
    public BuPApp() throws FileNotFoundException {
        runBuP();
    }

    // MODIFIES: this
    // EFFECTS: initializes state, registers handlers, and dispatches commands
    private void runBuP() {
        boolean keepGoing = true;
        init();
        initHandlers();

        while (keepGoing) {
            displayMenu();
            String cmd = input.nextLine().trim().toLowerCase();

            if ("q".equals(cmd)) {
                keepGoing = false;
                continue;
            }

            Runnable action = handlers.get(cmd);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Selection not valid...");
            }
        }
        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes library, IO objects, and scanner
    private void init() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        photoLibrary = new PhotoLibrary("my library");
    }

    // MODIFIES: this
    // EFFECTS: installs command handlers
    private void initHandlers() {
        handlers = new HashMap<>();
        handlers.put("l", this::loadPhotoLibrary);
        handlers.put("s", this::savePhotoLibrary);
        handlers.put("p", this::doAddPhoto);
        handlers.put("d", this::doRemovePhoto);
        handlers.put("w", this::doWriteReflection);
        handlers.put("x", this::handleDeleteComment); // delete one comment
        handlers.put("f", this::doFindCommonPhotos);
        handlers.put("e", this::doEditAlbums);
    }

    // EFFECTS: prints main menu
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tl -> load photo library");
        System.out.println("\ts -> save photo library");
        System.out.println("\tp -> add photo to library");
        System.out.println("\td -> delete photo from library");
        System.out.println("\tw -> write reflection for a photo");
        System.out.println("\tx -> delete ONE comment from a reflection");
        System.out.println("\tf -> find photos by problem type");
        System.out.println("\te -> edit albums");
        System.out.println("\tq -> quit");
        System.out.print("> ");
    }

    // EFFECTS: prompts until user enters a non-empty line; returns it trimmed
    private String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    // EFFECTS: prompts once; returns trimmed line (may be empty)
    private String readLineAllowEmpty(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    // EFFECTS: prompts until a valid int is entered; returns it
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // EFFECTS: prompts until a valid double is entered; returns it
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // EFFECTS: prompts until a valid ISO-8601 date (yyyy-mm-dd) is entered; returns
    // it
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Please enter a date in format yyyy-mm-dd.");
            }
        }
    }

    // REQUIRES: size > 0
    // EFFECTS: prompts until a valid index in [0, size) is entered; returns it
    private int readIndex(String prompt, int size) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            try {
                int idx = Integer.parseInt(s);
                if (idx >= 0 && idx < size) {
                    return idx;
                }
                System.out.println("Index out of range (0.." + (size - 1) + ").");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer index.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a photo to the library
    private void doAddPhoto() {
        String pname = readNonEmptyLine("Enter photo name: ");
        String camera = readNonEmptyLine("Enter camera: ");
        int iso = readInt("Enter ISO (e.g., 100/200/...): ");
        double aperture = readDouble("Enter aperture (e.g., 2.8/5.6): ");
        double shutterSpeed = readDouble("Enter shutter speed (seconds, e.g., 0.005): ");
        LocalDate date = readDate("Enter date (yyyy-mm-dd): ");

        Photo p = new Photo(pname, camera, iso, aperture, shutterSpeed, date);
        photoLibrary.addPhoto(p);
        System.out.println("Photo added: " + pname);
    }

    // MODIFIES: this
    // EFFECTS: removes the chosen photo from the library
    private void doRemovePhoto() {
        if (photoLibrary.getPhotos().isEmpty()) {
            System.out.println("No photo in library.");
            return;
        }
        System.out.println("\nPhotos:");
        for (int i = 0; i < photoLibrary.getPhotos().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getPhotos().get(i).getPhotoname());
        }
        int idx = readIndex("Select photo index to delete: ", photoLibrary.getPhotos().size());
        Photo p = photoLibrary.getPhotos().get(idx);
        photoLibrary.removePhoto(p);
        System.out.println("Deleted photo: " + p.getPhotoname());
    }

    // MODIFIES: this
    // EFFECTS: writes a reflection (type, optional comment, score) to a selected
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

    // REQUIRES: photoLibrary has at least one photo
    // EFFECTS: prints photos and returns the chosen one
    private Photo pickPhoto() {
        System.out.println("\nSelect a photo:");
        for (int i = 0; i < photoLibrary.getPhotos().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getPhotos().get(i).getPhotoname());
        }
        int idx = readIndex("Select photo index (0.." + (photoLibrary.getPhotos().size() - 1) + "): ",
                photoLibrary.getPhotos().size());
        return photoLibrary.getPhotos().get(idx);
    }

    // MODIFIES: none
    // EFFECTS: builds a Reflection by reading type, optional single comment, and
    // score
    private Reflection promptReflection() {
        Reflection r = new Reflection();
        r.addProblemType(readProblemType());

        String c = readLineAllowEmpty("Add a comment (or press Enter to skip): ");
        if (!c.isEmpty()) {
            r.addComment(c);
        }

        setScore(r);
        return r;
    }

    // MODIFIES: none
    // EFFECTS: reads a valid problem type from user; keeps prompting until valid
    private ProblemType readProblemType() {
        System.out.println("Enter problem type (EXPOSURE / SHARPNESS / COLOR / COMPOSITION / OTHER):");
        while (true) {
            String raw = input.nextLine().trim().toUpperCase();
            try {
                return ProblemType.valueOf(raw);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid type, try again: (EXPOSURE / SHARPNESS / COLOR / COMPOSITION / OTHER)");
            }
        }
    }

    // MODIFIES: r
    // EFFECTS: prompts user for an integer 0..100 and sets r.score
    private void setScore(Reflection r) {
        int score = -1;
        while (score < 0 || score > 100) {
            System.out.print("Set score (0–100): ");
            String s = input.nextLine().trim();
            try {
                score = Integer.parseInt(s);
                if (score < 0 || score > 100) {
                    System.out.println("Please enter a number between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
        r.setScore(score);
    }

    // EFFECTS: finds and lists photos by problem type
    private void doFindCommonPhotos() {
        System.out.println("Enter problem type: EXPOSURE / SHARPNESS / COLOR / COMPOSITION / OTHER");
        ProblemType t = readProblemType();
        var result = photoLibrary.findCommonPhotos(t);
        System.out.println("Photos with problem type " + t + ":");
        for (Photo p : result) {
            System.out.println("- " + p.getPhotoname());
        }
    }

    // MODIFIES: this
    // EFFECTS: lets user choose a photo, then remove exactly one comment from its
    // reflection
    private void handleDeleteComment() {
        if (photoLibrary.getPhotos().isEmpty()) {
            System.out.println("No photos available.");
            return;
        }
        Photo photo = pickPhoto();
        if (photo == null) {
            return;
        }
        deleteCommentFromReflection(photo);
    }

    // REQUIRES: photo != null
    // MODIFIES: photo
    // EFFECTS: prints comments; removes the selected one if index valid; reports
    // result
    private void deleteCommentFromReflection(Photo photo) {
        Reflection ref = photo.getReflection();
        if (ref == null || ref.getComments().isEmpty()) {
            System.out.println("This photo has no comments.");
            return;
        }

        System.out.println("Comments for " + photo.getPhotoname() + ":");
        for (int i = 0; i < ref.getComments().size(); i++) {
            System.out.println(i + ": " + ref.getComments().get(i));
        }

        int idx = readIndex("Enter index of comment to delete: ", ref.getComments().size());
        String removed = ref.getComments().remove(idx);
        System.out.println("Removed comment: \"" + removed + "\"");
    }

    // MODIFIES: this
    // EFFECTS: provides submenu for album management
    private void doEditAlbums() {
        if (photoLibrary.getAlbums().isEmpty()) {
            // allow operating on empty set as well, user can create
        }
        boolean editing = true;

        Map<String, Runnable> albumMenu = new HashMap<>();
        albumMenu.put("1", this::doAddAlbum);
        albumMenu.put("2", this::doRemoveAlbum);
        albumMenu.put("3", this::doViewAlbums);
        albumMenu.put("4", this::doEditAlbum);

        while (editing) {
            showAlbumMenu();
            String choice = input.nextLine().trim();

            if ("0".equals(choice)) {
                editing = false;
                continue;
            }

            Runnable action = albumMenu.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // EFFECTS: prints the album menu
    private void showAlbumMenu() {
        System.out.println("\nAlbum Menu:");
        System.out.println("1 -> Add new album");
        System.out.println("2 -> Remove album");
        System.out.println("3 -> View all albums");
        System.out.println("4 -> Manage photos");
        System.out.println("0 -> Back");
        System.out.print("> ");
    }

    // MODIFIES: this
    // EFFECTS: adds a new album to the library
    private void doAddAlbum() {
        String name = readNonEmptyLine("Enter album name: ");
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
        int idx = readIndex("Enter index: ", photoLibrary.getAlbums().size());
        Album removed = photoLibrary.getAlbums().remove(idx);
        System.out.println("Removed album: " + removed.getAlbumName());
    }

    // EFFECTS: displays all albums and their photo counts
    private void doViewAlbums() {
        if (photoLibrary.getAlbums().isEmpty()) {
            System.out.println("No albums found.");
            return;
        }
        System.out.println("\nAll albums:");
        for (Album a : photoLibrary.getAlbums()) {
            System.out.println("- " + a.getAlbumName() + " (" + a.getPhotos().size() + " photos)");
        }
    }

    // MODIFIES: album
    // EFFECTS: add/delete photo in a chosen album
    private void doEditAlbum() {
        Album album = selectAlbum();
        if (album == null) {
            return;
        }

        Map<String, Runnable> menu = new HashMap<>();
        menu.put("1", () -> addPhotoToAlbum(album));
        menu.put("2", () -> deletePhotoFromAlbum(album));

        boolean editing = true;
        while (editing) {
            String choice = showEditAlbumMenu(album);
            if ("0".equals(choice)) {
                editing = false;
                continue;
            }
            Runnable action = menu.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    // EFFECTS: prints album edit menu and returns user choice
    private String showEditAlbumMenu(Album album) {
        System.out.println("\nEditing: " + album.getAlbumName());
        System.out.println("1 -> Add photo");
        System.out.println("2 -> Delete photo");
        System.out.println("0 -> Back");
        System.out.print("> ");
        return input.nextLine().trim();
    }

    // EFFECTS: prompts user to choose an album, returns it or null if none
    private Album selectAlbum() {
        if (photoLibrary.getAlbums().isEmpty()) {
            System.out.println("No albums available.");
            return null;
        }
        System.out.println("\nSelect an album:");
        for (int i = 0; i < photoLibrary.getAlbums().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getAlbums().get(i).getAlbumName());
        }
        int idx = readIndex("Enter index: ", photoLibrary.getAlbums().size());
        return photoLibrary.getAlbums().get(idx);
    }

    // MODIFIES: album
    // EFFECTS: adds a library photo to the selected album
    private void addPhotoToAlbum(Album album) {
        if (photoLibrary.getPhotos().isEmpty()) {
            System.out.println("No photos in library.");
            return;
        }
        System.out.println("\nSelect a photo to add:");
        for (int i = 0; i < photoLibrary.getPhotos().size(); i++) {
            System.out.println(i + ": " + photoLibrary.getPhotos().get(i).getPhotoname());
        }
        int idx = readIndex("Enter index: ", photoLibrary.getPhotos().size());
        Photo p = photoLibrary.getPhotos().get(idx);
        album.addPhoto(p);
        System.out.println("Added: " + p.getPhotoname());
    }

    // MODIFIES: album
    // EFFECTS: deletes a photo from the selected album
    private void deletePhotoFromAlbum(Album album) {
        if (album.getPhotos().isEmpty()) {
            System.out.println("Album is empty.");
            return;
        }
        System.out.println("\nSelect a photo to delete:");
        for (int i = 0; i < album.getPhotos().size(); i++) {
            System.out.println(i + ": " + album.getPhotos().get(i).getPhotoname());
        }
        int idx = readIndex("Enter index: ", album.getPhotos().size());
        Photo removed = album.getPhotos().remove(idx);
        System.out.println("Deleted: " + removed.getPhotoname());
    }

    // EFFECTS: saves the photoLibrary to JSON_STORE
    private void savePhotoLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(photoLibrary);
            jsonWriter.close();
            System.out.println("Saved " + photoLibrary.getLibName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads photoLibrary from JSON_STORE and shows a brief summary
    private void loadPhotoLibrary() {
        try {
            photoLibrary = jsonReader.read();
            System.out.println("Loaded " + photoLibrary.getLibName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
