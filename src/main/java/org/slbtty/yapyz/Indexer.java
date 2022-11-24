package org.slbtty.yapyz;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class Indexer{


    // TODO: switch to per-file path?
    private List<String> indexPaths;


    private final Path indexStoragePath = Paths.get(System.getProperty("user.home")).resolve(".yapyz").resolve("index");
    private final IndexWriterConfig writerConfig;

    public void setPaths(List<String> paths) {
        this.indexPaths = paths;
    }

    public Indexer() throws IOException {

        // Init indexWriter

        Files.createDirectories(indexStoragePath); // ensure path

        var analyzer = new StandardAnalyzer();

        writerConfig = new IndexWriterConfig(analyzer);
        writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

    }

    private IndexWriter obtainWriter() throws IOException {
        return new IndexWriter(FSDirectory.open(indexStoragePath), writerConfig);
    }

    public void rebuildIndex() throws IOException {
        var writer = obtainWriter();
        writer.deleteAll();
        for (var path : indexPaths) {
            Path p = Path.of(path);
            if (Files.isDirectory(p.toAbsolutePath())) {
                indexDocs(writer,p);
            } else {
                Logger.error(path, " is not a valid path");
            }
        }
        writer.close();
    }

    void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        try (InputStream stream = Files.newInputStream(file)) {

            // All info within a Document storied within
            var doc = new Document();
            Field pathField = new StringField("path", file.toString(), Field.Store.YES);
            doc.add(pathField);
            doc.add(new LongPoint("modified", lastModified));
            doc.add(
                    new TextField(
                            "contents",
                            new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                writer.addDocument(doc);
            } else {
                writer.updateDocument(new Term("path", file.toString()), doc);
            }
            Logger.info("Index written " + file);
        }
    }

    void indexDocs(IndexWriter writer, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(
                    path,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes atts) {
                            try {
                                indexDoc(writer, file, atts.lastModifiedTime().toMillis());
                            } catch (IOException e) {
                                Logger.error(e);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
}

