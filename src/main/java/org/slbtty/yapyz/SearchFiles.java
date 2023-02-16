package org.slbtty.yapyz;


import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchFiles {

    public static List<ResultEntry> simpleTermSearch(String queryString){
        var dotYpz = Paths.get(System.getProperty("user.home")).resolve(".yapyz");
        var indexStoragePath = dotYpz.resolve("index");

        try (org.apache.lucene.index.DirectoryReader reader = DirectoryReader.open(FSDirectory.open(indexStoragePath.toAbsolutePath()))) {
            var searcher = new IndexSearcher(reader);
            var storedFields = reader.storedFields();

            Query query = new TermQuery(new Term("contents", queryString));

            TopDocs topDocs = searcher.search(query, 10);

            List<ResultEntry> result = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                result.add(new ResultEntry(storedFields.document(scoreDoc.doc).get("path"), scoreDoc.score));
            }

            return result;
        } catch (IOException e) {
            Logger.error(e, "Cannot open index reader");
        }

        return null;
    }

}
