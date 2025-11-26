# BetterThePicture
## Enhance your photography skills through reflective journaling.

**BetterThePicture** is a Java desktop application that helps users document and reflect on their photography. Users can record photo entries along with technical details like camera model, aperture, shutter speed, and ISO. They can also write down thoughts and improvements, and mark any areas in the photo they feel could be improved (e.g., lighting, composition). Over time, this creates a personalized learning gallery that can be revisited to better understand how to take great photos in similar situations.

This application is especially helpful for beginner photographers who are still exploring how to use manual settings on their cameras. As someone who enjoys photography but sometimes struggles with technical configurations, I wanted to build a tool to help myself and others review past mistakes and improve faster.

---

*User Stories*
-  As a user, I want to be able to create a new shooting album by entering the name of the album.
- As a user, I want to be able to add multiple photo entries (X) to a shooting album (Y), each with title, aperture, shutter speed, ISO, scene description, date and reflection.
- As a user, I want to view all the photo entries from a selected shooting session in a list.
- As a user, I want to be able to categorize reflections (e.g., “exposure”, “composition”, “focus”) and filter photo entries by these categories.
- As a user, I want to add, list, and delete comments and problemtypes on a photo’s reflection and rate the photo.
- As a user, I want to save my photo library to file and have the option to do so or not.
- As a user, I want to be given the option to load my photo library from file.

---

## Instructions for End User

- You can view the Photos (Xs) inside an Album (Y) by selecting an album in the left **Albums** panel.  
  The middle **Photos** panel shows all Xs in that Y.

- You can generate the first required action for the user story “adding multiple Xs to a Y” by selecting:
  Photo -> Add to Album  
  This adds the selected Photo into an Album.

- You can generate the second required action by selecting:
  Photo -> Remove from Album  
  This removes the selected Photo from the currently selected Album.

- You can add a Reflection to a Photo by selecting:
  Photo -> Edit Reflection...  
  This lets you enter a score, choose a problem type, and add comments.  
  The Reflection will appear in the right **Reflection** panel.

- You can locate the visual component by clicking any Photo;  
  the right **Preview** panel displays the actual image.

- You can save the application state by selecting:
  File -> Save...

- You can reload the previously saved state by selecting:
  File -> Load...

---
## Phase 4: Task 2
Tue Nov 25 15:02:20 PST 2025
Add photo to library: IMG_5610

Tue Nov 25 15:02:42 PST 2025
Add album: trees

Tue Nov 25 15:02:50 PST 2025
Photo IMG_5610 added to album trees

Tue Nov 25 15:03:12 PST 2025
Reflection updated for photo IMG_5610

Tue Nov 25 15:03:27 PST 2025
Add photo to library: water

Tue Nov 25 15:03:42 PST 2025
Photo water added to album trees

Tue Nov 25 15:03:51 PST 2025
Photo water removed from album trees




## Phase 4: Task 3

I would refactor by introducing an Observer pattern between the `model` and the `gui`. Right now, when photos or albums change, the GUI has to manually call `refreshAll`, `setAlbums`, and `setPhotos`, which couples `MainFrame` and `AppMenuBar` to the panels and makes it easy to miss a refresh. Instead, `PhotoLibrary` and `Album` could be observable via a small listener interface, such as `albumAdded`, `albumRemoved`, `photoAddedToAlbum`, `photoRemovedFromAlbum`, and `AlbumListPanel` / `PhotoListPanel` would register as observers and update themselves automatically when these events fire. This would reduce coupling, keep each class more cohesive, and make it easier to add new views that react to model changes without touching existing code.

I would also introduce a common abstraction for `PhotoCollection`, since right now `PhotoLibrary` and `Album` are handled differently and the GUI has to special-case concepts like the “(All Photos)” item and `null` current albums. I would extract an interface `PhotoCollection` with operations like `getPhotos`, `addPhoto`, and `removePhoto`, and have both `PhotoLibrary` and `Album` implement it. Then the UI could work with `PhotoCollection` instead of checking for special cases, which would simplify the control flow and make the overall design more consistent with the design principles discussed in class.

