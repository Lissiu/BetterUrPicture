package model;

import java.util.ArrayList;

// A reflection for the photo taken

public class Reflection {
    private ArrayList<ProblemType> problems;
    private ArrayList<String> comments;
    private int score;

    // EFFECTS: construct a reflection of a photos defining the problems and set the
    // photo to be reflected
    public Reflection() {
        this.problems = new ArrayList<ProblemType>();
        this.comments = new ArrayList<>();
        this.score = 0;

    }

    // MODIFIES: this
    // EFFECTS: add given problemype to the photo reflection
    public void addProblemType(ProblemType p) {
        this.problems.add(p);
        
    }

    // MODIFIES: this
    // EFFECTS: add discriptions and comments to the photo reflection
    public void addComment(String comment) {
        this.comments.add(comment);
    }

    // MODIFIES: this
    // EFFECTS: remove discriptions and comments to the photo reflection if exist
    public void removeComment(String comment) {
        this.comments.remove(comment);
    }


    public ArrayList<String> getComments() {
        return comments;
    }

    public ArrayList<ProblemType> getProblems() {
        return problems;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
