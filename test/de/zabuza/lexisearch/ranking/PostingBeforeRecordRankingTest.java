package de.zabuza.lexisearch.ranking;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IInvertedList;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.City;
import de.zabuza.lexisearch.model.city.CitySet;

/**
 * Test for {@link PostingBeforeRecordRanking}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PostingBeforeRecordRankingTest {

  /**
   * Test method for {@link PostingBeforeRecordRanking#getInvertedIndex()}.
   */
  @Test
  public void testGetInvertedIndex() {
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    ranking.takeSnapshot(invertedIndex, cities);

    Assert.assertEquals(invertedIndex, ranking.getInvertedIndex());
  }

  /**
   * Test method for {@link PostingBeforeRecordRanking#getKeyRecords()}.
   */
  @Test
  public void testGetKeyRecords() {
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    ranking.takeSnapshot(invertedIndex, cities);

    Assert.assertEquals(cities, ranking.getKeyRecords());
  }

  /**
   * Test method for
   * {@link PostingBeforeRecordRanking#getRankingScore(Object, Posting)}.
   */
  @Test
  public void testGetRankingScore() {
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    final Posting firstPosting =
        invertedIndex.getRecords("cit").getPostings().iterator().next();
    firstPosting.setScore(50);
    final Posting secondPosting =
        invertedIndex.getRecords("ity").getPostings().iterator().next();
    secondPosting.setScore(100);

    ranking.takeSnapshot(invertedIndex, cities);

    // The method must should simply return the posting score
    Assert.assertEquals(50, ranking.getRankingScore("cit", firstPosting), 0);
    Assert.assertEquals(100, ranking.getRankingScore("ity", secondPosting), 0);
  }

  /**
   * Test method for
   * {@link PostingBeforeRecordRanking#PostingBeforeRecordRanking()}.
   */
  @Test
  public void testPostingBeforeRecordRanking() {
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    ranking.takeSnapshot(invertedIndex, cities);

    Assert.assertEquals(cities, ranking.getKeyRecords());
    Assert.assertEquals(invertedIndex, ranking.getInvertedIndex());
  }

  /**
   * Test method for
   * {@link PostingBeforeRecordRanking#setRankingScoreToIndex()}.
   */
  @Test
  public void testSetRankingScoreToIndex() {
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    final Posting firstPosting =
        invertedIndex.getRecords("cit").getPostings().iterator().next();
    firstPosting.setScore(50);
    final Posting secondPosting =
        invertedIndex.getRecords("ity").getPostings().iterator().next();
    secondPosting.setScore(100);

    ranking.takeSnapshot(invertedIndex, cities);
    ranking.setRankingScoreToIndex();

    // The method must should not change any posting scores
    final Posting firstPostingAfterMethod =
        invertedIndex.getRecords("cit").getPostings().iterator().next();
    final Posting secondPostingAfterMethod =
        invertedIndex.getRecords("ity").getPostings().iterator().next();

    Assert.assertEquals(firstPosting, firstPostingAfterMethod);
    Assert.assertEquals(secondPosting, secondPostingAfterMethod);
    Assert.assertEquals(50, firstPostingAfterMethod.getScore(), 0);
    Assert.assertEquals(100, secondPostingAfterMethod.getScore(), 0);
  }

  /**
   * Test method for
   * {@link PostingBeforeRecordRanking#sortPostingsByRank(List)}.
   */
  @Test
  public void testSortPostingsByRank() {
    // Test required by lecture
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    // Setup the data set
    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City firstCity = new City(1, "city", 1.0f, 2.0f, 3, provider);
    final City secondCity = new City(2, "city", 1.0f, 2.0f, 4, provider);
    final City thirdCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    final City fourthCity = new City(4, "city", 1.0f, 2.0f, 2, provider);
    final City fifthCity = new City(5, "city", 1.0f, 2.0f, 5, provider);
    cities.add(firstCity);
    cities.add(secondCity);
    cities.add(thirdCity);
    cities.add(fourthCity);
    cities.add(fifthCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    // Set posting score (edit distance)
    final IInvertedList iListBefore = invertedIndex.getRecords("cit");
    for (final Posting posting : iListBefore.getPostings()) {
      if (posting.getId() == 1) {
        posting.setScore(3);
      } else if (posting.getId() == 2) {
        posting.setScore(2);
      } else if (posting.getId() == 3) {
        posting.setScore(6);
      } else if (posting.getId() == 4) {
        posting.setScore(3);
      } else if (posting.getId() == 5) {
        posting.setScore(6);
      } else {
        Assert.fail();
      }
    }

    ranking.takeSnapshot(invertedIndex, cities);

    final IInvertedList iList = invertedIndex.getRecords("cit");
    final ArrayList<Posting> postings = new ArrayList<>(iList.getSize());
    for (final Posting posting : iList.getPostings()) {
      postings.add(posting);
    }

    // Sort the postings
    ranking.sortPostingsByRank(postings);

    // Check the sorting
    final Iterator<Posting> postingIter = postings.iterator();
    Assert.assertEquals(secondCity,
        cities.getKeyRecordById(postingIter.next().getId()));
    Assert.assertEquals(firstCity,
        cities.getKeyRecordById(postingIter.next().getId()));
    Assert.assertEquals(fourthCity,
        cities.getKeyRecordById(postingIter.next().getId()));
    Assert.assertEquals(fifthCity,
        cities.getKeyRecordById(postingIter.next().getId()));
    Assert.assertEquals(thirdCity,
        cities.getKeyRecordById(postingIter.next().getId()));
  }

  /**
   * Test method for {@link PostingBeforeRecordRanking#takeSnapshot()}.
   */
  @Test
  public void testTakeSnapshot() {
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();

    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);
    final IInvertedIndex<String> invertedIndex =
        InvertedIndexUtil.createFromWords(cities);

    ranking.takeSnapshot(invertedIndex, cities);

    Assert.assertEquals(cities, ranking.getKeyRecords());
    Assert.assertEquals(invertedIndex, ranking.getInvertedIndex());
  }

}
