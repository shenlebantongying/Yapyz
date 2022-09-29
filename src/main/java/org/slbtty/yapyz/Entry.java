package org.slbtty.yapyz;

public class Entry {
    private final String path;
    private final float score;

    public Entry(String path, float desc){
        this.path = path;
        this.score = desc;
    }

    public String getPath() {
        return path;
    }

    public float getScore() {
        return score;
    }
}
