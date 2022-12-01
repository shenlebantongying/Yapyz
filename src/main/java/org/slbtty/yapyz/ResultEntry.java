package org.slbtty.yapyz;

public class ResultEntry {
    private final String path;
    private final Float score;

    public ResultEntry(String path, float score){
        this.path = path;
        this.score = score;
    }

    public String getPath(){
        return this.path;
    }

    public float getScore(){
        return this.score;
    }

}
