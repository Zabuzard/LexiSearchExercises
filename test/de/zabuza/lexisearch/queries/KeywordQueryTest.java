package de.zabuza.lexisearch.queries;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.document.Document;
import de.zabuza.lexisearch.document.DocumentSet;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.ranking.Bm25Ranking;

/**
 * Test for {@link KeywordQuery}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public class KeywordQueryTest {

  /**
   * Test method for {@link KeywordQuery#KeywordQuery(Iterable)}.
   */
  @Test
  public void testKeywordQuery() {
    final String firstWord = "a";
    final String secondWord = "b";
    final Document document = new Document(0, firstWord, secondWord);
    final Document anotherDocument = new Document(1, "1", secondWord);

    final IKeyRecordSet<IKeyRecord<String>, String> documents =
        new DocumentSet();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<IKeyRecord<String>> keywordQuery =
        new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final List<Posting> firstResult = keywordQuery.searchAnd(firstQuery);
    Assert.assertEquals(1, firstResult.size());
    Assert.assertEquals(0, firstResult.get(0).getId());

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final List<Posting> secondResult = keywordQuery.searchAnd(secondQuery);
    Assert.assertEquals(2, secondResult.size());
    Assert.assertEquals(0, secondResult.get(0).getId());
    Assert.assertEquals(1, secondResult.get(1).getId());

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final List<Posting> thirdResult = keywordQuery.searchAnd(thirdQuery);
    Assert.assertEquals(1, thirdResult.size());
    Assert.assertEquals(0, thirdResult.get(0).getId());
  }

  /**
   * Test method for {@link KeywordQuery#searchAnd(Iterable, Optional)}.
   */
  @Test
  public void testSearchAnd() {
    final String firstWord = "a";
    final String secondWord = "b";
    final Document document = new Document(0, firstWord, secondWord);
    final Document anotherDocument = new Document(1, "1", secondWord);

    final IKeyRecordSet<IKeyRecord<String>, String> documents =
        new DocumentSet();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<IKeyRecord<String>> keywordQuery =
        new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final List<Posting> firstResult = keywordQuery.searchAnd(firstQuery);
    Assert.assertEquals(1, firstResult.size());
    Assert.assertEquals(0, firstResult.get(0).getId());

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final List<Posting> secondResult = keywordQuery.searchAnd(secondQuery);
    Assert.assertEquals(2, secondResult.size());
    Assert.assertEquals(0, secondResult.get(0).getId());
    Assert.assertEquals(1, secondResult.get(1).getId());

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final List<Posting> thirdResult = keywordQuery.searchAnd(thirdQuery);
    Assert.assertEquals(1, thirdResult.size());
    Assert.assertEquals(0, thirdResult.get(0).getId());
  }

  /**
   * Test method for {@link KeywordQuery#searchOr(Iterable, Optional)}.
   */
  @Test
  public void testSearchOr() {
    final String firstWord = "a";
    final String secondWord = "b";
    final Document document = new Document(0, firstWord, secondWord);
    final Document anotherDocument = new Document(1, "1", secondWord);

    final IKeyRecordSet<IKeyRecord<String>, String> documents =
        new DocumentSet();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<IKeyRecord<String>> keywordQuery =
        new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final List<Posting> firstResult = keywordQuery.searchOr(firstQuery);
    Assert.assertEquals(1, firstResult.size());
    Assert.assertEquals(0, firstResult.get(0).getId());

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final List<Posting> secondResult = keywordQuery.searchOr(secondQuery);
    Assert.assertEquals(2, secondResult.size());
    Assert.assertEquals(0, secondResult.get(0).getId());
    Assert.assertEquals(1, secondResult.get(1).getId());

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final List<Posting> thirdResult = keywordQuery.searchOr(thirdQuery);
    Assert.assertEquals(2, thirdResult.size());
    Assert.assertEquals(0, thirdResult.get(0).getId());
    Assert.assertEquals(1, thirdResult.get(1).getId());

    // Test required by lecture (case 2)
    final String[] testDocumentsText =
        "first\tdocum\nsecond\tsecond docum\nthird\tthird third docum"
            .split("\n");
    final IKeyRecordSet<IKeyRecord<String>, String> testDocuments =
        DocumentSet.buildFromTextIterator(
            Arrays.asList(testDocumentsText).iterator(), "\t");

    final KeywordQuery<IKeyRecord<String>> testKeywordQuery =
        new KeywordQuery<>(testDocuments, new Bm25Ranking<>());

    LinkedList<String> testQuery = new LinkedList<>();
    testQuery.add("docum");
    testQuery.add("third");
    final List<Posting> testResult = testKeywordQuery.searchOr(testQuery);

    Assert.assertEquals(3, testResult.size());

    // Note that we begin to give indices starting from zero instead of one
    Assert.assertEquals(2, testResult.get(0).getId());
    Assert.assertEquals(2.521, testResult.get(0).getScore(), 0.0005);

    Assert.assertEquals(0, testResult.get(1).getId());
    Assert.assertEquals(0, testResult.get(1).getScore(), 0);

    Assert.assertEquals(1, testResult.get(2).getId());
    Assert.assertEquals(0, testResult.get(2).getScore(), 0);
  }

}
