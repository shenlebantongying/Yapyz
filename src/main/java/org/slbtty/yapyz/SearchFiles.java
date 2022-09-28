package org.slbtty.yapyz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.search.ScoreDoc;
import org.tinylog.Logger;

public class SearchFiles {

    public static ObservableList<Entry> simpleTermSearch(String queryString){
        var dotYpz = Paths.get(System.getProperty("user.home")).resolve(".yapyz");
        var indexStoragePath = dotYpz.resolve("index");

        try (org.apache.lucene.index.DirectoryReader reader = DirectoryReader.open(FSDirectory.open(indexStoragePath.toAbsolutePath()))) {
            var searcher = new IndexSearcher(reader);

            Query query = new TermQuery(new Term("contents", queryString));

            TopDocs topDocs = searcher.search(query, 10);

            ObservableList<Entry> result = FXCollections.observableArrayList();


            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                result.add(new Entry(searcher.doc(scoreDoc.doc).get("path"),scoreDoc.score));
            }

            return result;
        } catch (IOException e) {
            Logger.error(e, "Cannot open index reader");
        }

        return null;
    }

}
