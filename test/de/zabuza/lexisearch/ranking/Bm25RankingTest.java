package de.zabuza.lexisearch.ranking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.document.Document;
import de.zabuza.lexisearch.document.DocumentSet;
import de.zabuza.lexisearch.indexing.AInvertedList;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;
import de.zabuza.lexisearch.indexing.Posting;

/**
 * Test for {@link Bm25Ranking}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class Bm25RankingTest {

  /**
   * Test method for {@link Bm25Ranking#Bm25Ranking()}.
   */
  @Test
  public void testBm25Ranking() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    Assert.assertEquals(Bm25Ranking.DEFAULT_B_PARAMETER,
        ranking.getBParameter(), 0);
    Assert.assertEquals(Bm25Ranking.DEFAULT_K_PARAMETER,
        ranking.getKParameter(), 0);
  }

  /**
   * Test method for {@link Bm25Ranking#Bm25Ranking(double, double)}.
   */
  @Test
  public void testBm25RankingDoubleDouble() {
    final double bParameter = 1f;
    final double kParameter = 2f;
    final Bm25Ranking<String> ranking =
        new Bm25Ranking<>(kParameter, bParameter);

    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);
  }

  /**
   * Test method for {@link Bm25Ranking#getBParameter()}.
   */
  @Test
  public void testGetBParameter() {
    final double bParameter = 1f;
    final double kParameter = 2f;
    final Bm25Ranking<String> ranking =
        new Bm25Ranking<>(kParameter, bParameter);

    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);
  }

  /**
   * Test method for {@link Bm25Ranking#getInvertedIndex()}.
   */
  @Test
  public void testGetInvertedIndex() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    final Document firstDocument = new Document(1, "a", "b");
    final Document secondDocument = new Document(2, "c", "d");
    final DocumentSet documents = new DocumentSet();
    documents.add(firstDocument);
    documents.add(secondDocument);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    ranking.takeSnapshot(invertedIndex, documents);

    Assert.assertEquals(documents, ranking.getKeyRecords());
    Assert.assertEquals(invertedIndex, ranking.getInvertedIndex());
  }

  /**
   * Test method for {@link Bm25Ranking#getKeyRecords()}.
   */
  @Test
  public void testGetKeyRecords() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    final Document firstDocument = new Document(1, "a", "b");
    final Document secondDocument = new Document(2, "c", "d");
    final DocumentSet documents = new DocumentSet();
    documents.add(firstDocument);
    documents.add(secondDocument);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    ranking.takeSnapshot(invertedIndex, documents);

    Assert.assertEquals(documents, ranking.getKeyRecords());
    Assert.assertEquals(invertedIndex, ranking.getInvertedIndex());
  }

  /**
   * Test method for {@link Bm25Ranking#getKParameter()}.
   */
  @Test
  public void testGetKParameter() {
    final double bParameter = 1f;
    final double kParameter = 2f;
    final Bm25Ranking<String> ranking =
        new Bm25Ranking<>(kParameter, bParameter);

    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);
  }

  /**
   * Test method for {@link Bm25Ranking#getRankingScore(Object, Posting)}.
   */
  @Test
  public void testGetRankingScore() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    final int firstId = 1;
    final int secondId = 2;
    final String firstKey = "a";
    final String secondKey = "h";
    final String thirdKey = "k";
    final Document firstDocument = new Document(firstId, firstKey, "b c");
    final Document secondDocument =
        new Document(secondId, secondKey, "i " + thirdKey + " k l");
    final DocumentSet documents = new DocumentSet();
    documents.add(firstDocument);
    documents.add(secondDocument);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    ranking.takeSnapshot(invertedIndex, documents);

    final AInvertedList firstList = invertedIndex.getRecords(firstKey);
    final Iterator<Posting> firstPostings = firstList.getPostings().iterator();
    final Posting firstPosting = firstPostings.next();
    Assert.assertEquals(1.1354, ranking.getRankingScore(firstKey, firstPosting),
        0.0001);

    final AInvertedList secondList = invertedIndex.getRecords(secondKey);
    final Iterator<Posting> secondPostings =
        secondList.getPostings().iterator();
    final Posting secondPosting = secondPostings.next();
    Assert.assertEquals(0.8934,
        ranking.getRankingScore(secondKey, secondPosting), 0.0001);

    final AInvertedList thirdList = invertedIndex.getRecords(thirdKey);
    final Iterator<Posting> thirdPostings = thirdList.getPostings().iterator();
    final Posting thirdPosting = thirdPostings.next();
    Assert.assertEquals(1.3486, ranking.getRankingScore(thirdKey, thirdPosting),
        0.0001);
  }

  /**
   * Test method for {@link Bm25Ranking#setBParameter(double)}.
   */
  @Test
  public void testSetBParameter() {
    double bParameter = 1f;
    double kParameter = 2f;
    final Bm25Ranking<String> ranking =
        new Bm25Ranking<>(kParameter, bParameter);

    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);

    kParameter++;
    bParameter++;
    Assert.assertNotEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertNotEquals(kParameter, ranking.getKParameter(), 0);

    ranking.setBParameter(bParameter);
    ranking.setKParameter(kParameter);
    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);
  }

  /**
   * Test method for {@link Bm25Ranking#setKParameter(double)}.
   */
  @Test
  public void testSetKParameter() {
    double bParameter = 1f;
    double kParameter = 2f;
    final Bm25Ranking<String> ranking =
        new Bm25Ranking<>(kParameter, bParameter);

    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);

    kParameter++;
    bParameter++;
    Assert.assertNotEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertNotEquals(kParameter, ranking.getKParameter(), 0);

    ranking.setBParameter(bParameter);
    ranking.setKParameter(kParameter);
    Assert.assertEquals(bParameter, ranking.getBParameter(), 0);
    Assert.assertEquals(kParameter, ranking.getKParameter(), 0);
  }

  /**
   * Test method for {@link Bm25Ranking#setRankingScoreToIndex()}.
   */
  @Test
  public void testSetRankingScoreToIndex() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    final int firstId = 1;
    final int secondId = 2;
    final Document firstDocument = new Document(firstId, "a", "b c");
    final Document secondDocument = new Document(secondId, "h", "i j k l");
    final DocumentSet documents = new DocumentSet();
    documents.add(firstDocument);
    documents.add(secondDocument);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    ranking.takeSnapshot(invertedIndex, documents);
    ranking.setRankingScoreToIndex();

    LinkedList<AInvertedList> lists = new LinkedList<>();
    for (final String key : invertedIndex.getKeys()) {
      final AInvertedList list = invertedIndex.getRecords(key);
      lists.add(list);
    }

    final AInvertedList unionOfAll = AInvertedList.union(lists);
    // Transform the result into a list
    ArrayList<Posting> resultingList =
        new ArrayList<Posting>(unionOfAll.getSize());
    for (final Posting posting : unionOfAll.getPostings()) {
      resultingList.add(posting);
    }
    ranking.sortPostingsByRank(resultingList);

    Assert.assertEquals(secondId, resultingList.get(0).getId());
    Assert.assertEquals(firstId, resultingList.get(1).getId());

    // Test required by lecture
    final String[] testDocumentsText =
        "first\tdocum\nsecond\tsecond docum\nthird\tthird third docum"
            .split("\n");
    final IKeyRecordSet<IKeyRecord<String>, String> testDocuments =
        DocumentSet.buildFromTextIterator(
            Arrays.asList(testDocumentsText).iterator(), "\t");
    final IInvertedIndex<String> testInvertedIndex =
        InvertedIndexUtil.createFromWords(testDocuments);

    final Bm25Ranking<String> testRanking = new Bm25Ranking<>(1.75, 0.75);
    testRanking.takeSnapshot(testInvertedIndex, testDocuments);
    testRanking.setRankingScoreToIndex();

    // Note that we begin to give indices starting from zero instead of one
    Iterator<Posting> documPostings =
        testInvertedIndex.getRecords("docum").getPostings().iterator();
    final Posting firstDocumPosting = documPostings.next();
    final Posting secondDocumPosting = documPostings.next();
    final Posting thirdDocumPosting = documPostings.next();
    Assert.assertEquals(0, firstDocumPosting.getId());
    Assert.assertEquals(0, firstDocumPosting.getScore(), 0);
    Assert.assertEquals(1, secondDocumPosting.getId());
    Assert.assertEquals(0, secondDocumPosting.getScore(), 0);
    Assert.assertEquals(2, thirdDocumPosting.getId());
    Assert.assertEquals(0, thirdDocumPosting.getScore(), 0);

    Iterator<Posting> firstPostings =
        testInvertedIndex.getRecords("first").getPostings().iterator();
    final Posting firstPosting = firstPostings.next();
    Assert.assertEquals(0, firstPosting.getId());
    Assert.assertEquals(1.885, firstPosting.getScore(), 0.0005);

    Iterator<Posting> secondPostings =
        testInvertedIndex.getRecords("second").getPostings().iterator();
    final Posting secondPosting = secondPostings.next();
    Assert.assertEquals(1, secondPosting.getId());
    Assert.assertEquals(2.325, secondPosting.getScore(), 0.0005);

    Iterator<Posting> thirdPostings =
        testInvertedIndex.getRecords("third").getPostings().iterator();
    final Posting thirdPosting = thirdPostings.next();
    Assert.assertEquals(2, thirdPosting.getId());
    Assert.assertEquals(2.521, thirdPosting.getScore(), 0.0005);
  }

  /**
   * Test method for {@link Bm25Ranking#sortPostingsByRank(List)}.
   */
  @Test
  public void testSortPostingsByRank() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    final int firstId = 1;
    final int secondId = 2;
    final Document firstDocument = new Document(firstId, "a", "b c");
    final Document secondDocument = new Document(secondId, "h", "i j k l");
    final DocumentSet documents = new DocumentSet();
    documents.add(firstDocument);
    documents.add(secondDocument);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    ranking.takeSnapshot(invertedIndex, documents);
    ranking.setRankingScoreToIndex();

    LinkedList<AInvertedList> lists = new LinkedList<>();
    for (final String key : invertedIndex.getKeys()) {
      final AInvertedList list = invertedIndex.getRecords(key);
      lists.add(list);
    }

    final AInvertedList unionOfAll = AInvertedList.union(lists);
    // Transform the result into a list
    ArrayList<Posting> resultingList =
        new ArrayList<Posting>(unionOfAll.getSize());
    for (final Posting posting : unionOfAll.getPostings()) {
      resultingList.add(posting);
    }
    ranking.sortPostingsByRank(resultingList);

    Assert.assertEquals(secondId, resultingList.get(0).getId());
    Assert.assertEquals(firstId, resultingList.get(1).getId());
  }

  /**
   * Test method for
   * {@link Bm25Ranking#takeSnapshot(IInvertedIndex, IKeyRecordSet)}.
   */
  @Test
  public void testTakeSnapshot() {
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();

    final Document firstDocument = new Document(1, "a", "b");
    final Document secondDocument = new Document(2, "c", "d");
    final DocumentSet documents = new DocumentSet();
    documents.add(firstDocument);
    documents.add(secondDocument);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(documents);

    ranking.takeSnapshot(invertedIndex, documents);

    Assert.assertEquals(documents, ranking.getKeyRecords());
    Assert.assertEquals(invertedIndex, ranking.getInvertedIndex());
  }

}
