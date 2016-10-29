package de.zabuza.lexisearch.queries;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.document.Document;
import de.zabuza.lexisearch.document.DocumentSet;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.IWordRecord;
import de.zabuza.lexisearch.indexing.Posting;

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

    final IKeyRecordSet<IWordRecord, String> documents = new DocumentSet();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<IWordRecord> keywordQuery =
        new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final List<Posting> firstResult = keywordQuery.searchAnd(firstQuery);
    Assert.assertEquals(1, firstResult.size());
    Assert.assertTrue(firstResult.contains(document));

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final List<Posting> secondResult = keywordQuery.searchAnd(secondQuery);
    Assert.assertEquals(2, secondResult.size());
    Assert.assertTrue(secondResult.contains(document));
    Assert.assertTrue(secondResult.contains(anotherDocument));

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final List<Posting> thirdResult = keywordQuery.searchAnd(thirdQuery);
    Assert.assertEquals(1, thirdResult.size());
    Assert.assertTrue(secondResult.contains(document));
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

    final IKeyRecordSet<IWordRecord, String> documents = new DocumentSet();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<IWordRecord> keywordQuery =
        new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final List<Posting> firstResult = keywordQuery.searchAnd(firstQuery);
    Assert.assertEquals(1, firstResult.size());
    Assert.assertTrue(firstResult.contains(document));

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final List<Posting> secondResult = keywordQuery.searchAnd(secondQuery);
    Assert.assertEquals(2, secondResult.size());
    Assert.assertTrue(secondResult.contains(document));
    Assert.assertTrue(secondResult.contains(anotherDocument));

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final List<Posting> thirdResult = keywordQuery.searchAnd(thirdQuery);
    Assert.assertEquals(1, thirdResult.size());
    Assert.assertTrue(secondResult.contains(document));
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

    final IKeyRecordSet<IWordRecord, String> documents = new DocumentSet();
    documents.add(document);
    documents.add(anotherDocument);

    final KeywordQuery<IWordRecord> keywordQuery =
        new KeywordQuery<>(documents);

    LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add(firstWord);
    final List<Posting> firstResult = keywordQuery.searchOr(firstQuery);
    Assert.assertEquals(1, firstResult.size());
    Assert.assertTrue(firstResult.contains(document));

    LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add(secondWord);
    final List<Posting> secondResult = keywordQuery.searchOr(secondQuery);
    Assert.assertEquals(2, secondResult.size());
    Assert.assertTrue(secondResult.contains(document));
    Assert.assertTrue(secondResult.contains(anotherDocument));

    LinkedList<String> thirdQuery = new LinkedList<>();
    thirdQuery.add(firstWord);
    thirdQuery.add(secondWord);
    final List<Posting> thirdResult = keywordQuery.searchOr(thirdQuery);
    Assert.assertEquals(2, thirdResult.size());
    Assert.assertTrue(secondResult.contains(document));
    Assert.assertTrue(secondResult.contains(anotherDocument));
  }

}
