package model;

import java.util.ArrayList;

public class Reflection {
    private ArrayList<ProblemType> problems;
    private String comment;
    private Photo photo;

    // EFFECTS: construct a reflection of a photos defining the problems and set the photo to be reflected
    public Reflection() {
        this.problems = new ArrayList<ProblemType>();
        this.photo.setReflect(true);
        
    }

    // MODIFIES: this
    // EFFECTS: add given problemype to the photo reflection
    public void addProblemType(ProblemType p) {
        this.problems.add(p);

    }

    // MODIFIES: this
    // EFFECTS: add discriptions and comments to the photo reflection
    public void addComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<ProblemType> getProblems() {
        return problems;
    }

    public void setProblems(ArrayList<ProblemType> problems) {
        this.problems = problems;
    }
}
