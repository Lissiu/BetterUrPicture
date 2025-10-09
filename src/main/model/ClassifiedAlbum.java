package model;

import java.time.LocalDate;
import java.util.ArrayList;


// the album of photos with problem classified
public class ClassifiedAlbum extends Album{
    public ClassifiedAlbum(String name, LocalDate date, String type) {
        super(name, date);
        //TODO Auto-generated constructor stub
    }

    private String type;
    private ArrayList<Photo> classifiedAlbum;

public ArrayList<Photo> setType() {
    return classifiedAlbum;

}





}
