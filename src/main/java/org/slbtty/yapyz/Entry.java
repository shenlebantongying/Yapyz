package org.slbtty.yapyz;

public class Entry {
    private final String name;
    private final float score;

    public Entry(String name, float desc){
        this.name = name;
        this.score = desc;
    }

    public String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }
}
