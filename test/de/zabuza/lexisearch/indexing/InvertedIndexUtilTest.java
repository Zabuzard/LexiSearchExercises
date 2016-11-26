package de.zabuza.lexisearch.indexing;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.model.document.Document;

/**
 * Test for {@link InvertedIndexUtil}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class InvertedIndexUtilTest {

  /**
   * Test method for {@link InvertedIndexUtil#createFromWords(Iterable)}.
   */
  @Test
  public void testCreateFromWords() {
    final int firstId = 1;
    final int secondId = 2;
    final String firstKey = "a";
    final String secondKey = "1";
    final String thirdKey = "b";
    final String fourthKey = "2";

    final Document firstDocument = new Document(firstId, firstKey, secondKey);
    final Document secondDocument = new Document(secondId, thirdKey, fourthKey);
    final LinkedList<Document> documents = new LinkedList<>();
    documents.add(firstDocument);
    documents.add(secondDocument);

    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    Assert.assertTrue(invertedIndex.containsRecord(firstKey, firstId));
    Assert.assertTrue(invertedIndex.containsRecord(secondKey, firstId));
    Assert.assertTrue(invertedIndex.containsRecord(thirdKey, secondId));
    Assert.assertTrue(invertedIndex.containsRecord(fourthKey, secondId));
  }

}
