package org.slbtty.yapyz;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ResultEntry {
    private final ObjectProperty<String> path = new SimpleObjectProperty<>();
    private final ObjectProperty<Float> score = new SimpleObjectProperty<>();

    public ResultEntry(String path, float score){
        this.path.set(path);
        this.score.set(score);
    }

    public String getPath(){
        return path.toString();
    }

    public float getScore(){
        return getScore();
    }

    public ObjectProperty<String> getPathProperty() {
        return path;
    }

    public ObjectProperty<Float> getScoreProperty() {
        return score;
    }
}
