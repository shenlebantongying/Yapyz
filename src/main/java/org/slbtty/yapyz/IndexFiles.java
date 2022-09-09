package org.slbtty.yapyz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

public class IndexFiles implements AutoCloseable {

    private IndexFiles() throws IOException {

    }

    void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        System.out.println("Index " + file);
        try (InputStream stream = Files.newInputStream(file)) {
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

        }

    }

    void indexDocs(final IndexWriter writer, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(
                    path,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes atts) {
                            try {
                                indexDoc(writer, file, atts.lastModifiedTime().toMillis());
                            } catch (IOException e) {
                                e.printStackTrace(System.err);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }

    public static void main(String[] args) throws IOException {
        var dotYpz = Paths.get(System.getProperty("user.home")).resolve(".yapyz");
        System.out.println(Files.isDirectory(dotYpz));

        var indexStoragePath = dotYpz.resolve("index");
        Files.createDirectories(indexStoragePath);

        var m_SearchPath = Paths.get("F:", "desktop", "test");

        try {
            var dir = FSDirectory.open(indexStoragePath);
            var analyzer = new StandardAnalyzer();
            var writerConfig = new IndexWriterConfig(analyzer);

            writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

            try (IndexWriter writer = new IndexWriter(dir, writerConfig);
                 var indexFiles = new IndexFiles()) {
                indexFiles.indexDocs(writer, m_SearchPath.toAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.close();
            }


        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    @Override
    public void close() {

    }
}

