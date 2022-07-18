package com.project.ir.service;

import com.project.ir.model.SearchResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private static final String SEARCH_FIELD = "contents";
    private static final int MAX_RESULT_NUMBER = 20;
    private static final Path indexDirectory = Paths.get(
            "/home/amir/Documents/Java/Projects/ir-project/ir-project/z_index"
    );

    public List<SearchResult> search(String searchText) {
        try {
            DirectoryReader reader = DirectoryReader.open(FSDirectory.open(indexDirectory));
            IndexSearcher searcher = new IndexSearcher(reader);
            // set similarity to tf-idf
            searcher.setSimilarity(new ClassicSimilarity());

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser(SEARCH_FIELD, analyzer);
            Query query = parser.parse(searchText);

            TopDocs results = searcher.search(query, MAX_RESULT_NUMBER);
            ScoreDoc[] hits = results.scoreDocs;

            return getSearchResultLIst(searcher, hits);
        } catch (Exception e) {
            return new ArrayList<>(0);
        }
    }

    private List<SearchResult> getSearchResultLIst(IndexSearcher searcher, ScoreDoc[] hits) throws IOException {
        List<SearchResult> resultList = new ArrayList<>(hits.length);
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            resultList.add(new SearchResult(doc.get("uri"), doc.get("title")));
        }
        return resultList;
    }

}
