package org.slbtty.yapyz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class indexSettings {
    public ObservableList<String> indexPaths;

    private final static Path fpath = Paths.get(System.getProperty("user.home"))
            .resolve(".yapyz")
            .resolve("indexPaths.txt");

    public indexSettings() {

        indexPaths = FXCollections.observableArrayList();

        // ensure the file
        if (!Files.isRegularFile(fpath)) {
            try {
                Files.createFile(fpath);
            } catch (Exception e) {
                System.out.println("Unable to create index path");
            }
        }

        loadFromDisk();
    }

    public void addPath(String p) {
        indexPaths.add(p);
    }

    public void addPath(Path p) {
        indexPaths.add(p.toAbsolutePath().toString());
    }

    public void removePath(String p) {
        indexPaths.removeIf(s -> s.equals(p));
    }

    public void loadFromDisk() {
        try (Stream<String> lines = Files.lines(fpath)) {
            lines.filter(Predicate.not(String::isEmpty))
                    .forEach(indexPaths::add);
        } catch (Exception e) {
            System.out.println("Unable to load index config");
        }
    }

    public void saveToDisk() {
        try (PrintWriter out = new PrintWriter(fpath.toFile())) {
            for (String x : indexPaths) {
                out.println(x);
            }
        } catch (Exception e) {
            System.out.println("Unable to save file");
        }
    }
}
