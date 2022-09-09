package org.slbtty.yapyz;

import org.apache.lucene.document.Document;
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

public class SearchFiles {

    public static void main(String[] args) throws IOException {
        var dotYpz = Paths.get(System.getProperty("user.home")).resolve(".yapyz");
        var indexStoragePath = dotYpz.resolve("index");

        try (org.apache.lucene.index.DirectoryReader reader = DirectoryReader.open(FSDirectory.open(indexStoragePath.toAbsolutePath()))) {
            var searcher = new IndexSearcher(reader);
            
            System.out.println(reader.getDocCount("contents"));
            
            var queryString = "lambda";
            
            Query query = new TermQuery(new Term("contents", queryString));
            
            TopDocs topDocs = searcher.search(query, 10);
            
            System.out.println(topDocs.scoreDocs.length);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document resultDoc = searcher.doc(scoreDoc.doc);
                System.out.println("> got" + resultDoc.get("path"));
            }
        }
    }
}
