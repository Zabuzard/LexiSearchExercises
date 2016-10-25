package de.zabuza.lexisearch.queries;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.document.Document;
import de.zabuza.lexisearch.indexing.AInvertedList;

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

    LinkedList<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<Document> keywordQuery = new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final AInvertedList firstResult = keywordQuery.searchAnd(firstQuery);
    Assert.assertEquals(1, firstResult.getSize());
    Assert.assertTrue(firstResult.containsRecord(0));

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final AInvertedList secondResult = keywordQuery.searchAnd(secondQuery);
    Assert.assertEquals(2, secondResult.getSize());
    Assert.assertTrue(secondResult.containsRecord(0));
    Assert.assertTrue(secondResult.containsRecord(1));

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final AInvertedList thirdResult = keywordQuery.searchAnd(thirdQuery);
    Assert.assertEquals(1, thirdResult.getSize());
    Assert.assertTrue(secondResult.containsRecord(0));
  }

  /**
   * Test method for {@link KeywordQuery#searchAnd(Iterable)}.
   */
  @Test
  public void testSearchAnd() {
    final String firstWord = "a";
    final String secondWord = "b";
    final Document document = new Document(0, firstWord, secondWord);
    final Document anotherDocument = new Document(1, "1", secondWord);

    LinkedList<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<Document> keywordQuery = new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final AInvertedList firstResult = keywordQuery.searchAnd(firstQuery);
    Assert.assertEquals(1, firstResult.getSize());
    Assert.assertTrue(firstResult.containsRecord(0));

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final AInvertedList secondResult = keywordQuery.searchAnd(secondQuery);
    Assert.assertEquals(2, secondResult.getSize());
    Assert.assertTrue(secondResult.containsRecord(0));
    Assert.assertTrue(secondResult.containsRecord(1));

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final AInvertedList thirdResult = keywordQuery.searchAnd(thirdQuery);
    Assert.assertEquals(1, thirdResult.getSize());
    Assert.assertTrue(secondResult.containsRecord(0));
  }

  /**
   * Test method for {@link KeywordQuery#searchOr(Iterable)}.
   */
  @Test
  public void testSearchOr() {
    final String firstWord = "a";
    final String secondWord = "b";
    final Document document = new Document(0, firstWord, secondWord);
    final Document anotherDocument = new Document(1, "1", secondWord);

    LinkedList<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<Document> keywordQuery = new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final AInvertedList firstResult = keywordQuery.searchOr(firstQuery);
    Assert.assertEquals(1, firstResult.getSize());
    Assert.assertTrue(firstResult.containsRecord(0));

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final AInvertedList secondResult = keywordQuery.searchOr(secondQuery);
    Assert.assertEquals(2, secondResult.getSize());
    Assert.assertTrue(secondResult.containsRecord(0));
    Assert.assertTrue(secondResult.containsRecord(1));

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final AInvertedList thirdResult = keywordQuery.searchOr(thirdQuery);
    Assert.assertEquals(2, thirdResult.getSize());
    Assert.assertTrue(secondResult.containsRecord(0));
    Assert.assertTrue(secondResult.containsRecord(1));
  }

}
