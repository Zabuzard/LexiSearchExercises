package de.zabuza.lexisearch.queries;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.City;
import de.zabuza.lexisearch.model.city.CitySet;
import de.zabuza.lexisearch.ranking.IRankingProvider;
import de.zabuza.lexisearch.ranking.PostingBeforeRecordRanking;

/**
 * Test for {@link FuzzyPrefixQuery}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class FuzzyPrefixQueryTest {

  /**
   * Test method for
   * {@link FuzzyPrefixQuery#FuzzyPrefixQuery(IKeyRecordSet, QGramProvider, IRankingProvider)}
   * .
   */
  @SuppressWarnings("static-method")
  @Test
  public void testFuzzyPrefixQueryWithIRankingProvider() {
    final QGramProvider provider = new QGramProvider(3, '$');
    final String firstCity = "Football\t3\t0\t0";
    final String secondCity = "foobar\t1\t0\t0";
    final String thirdCity = "Footsal\t2\t0\t0";
    final String fourthCity = "Foot Barca\t1\t0\t0";
    final List<String> content = new LinkedList<>();
    content.add(firstCity);
    content.add(secondCity);
    content.add(thirdCity);
    content.add(fourthCity);
    final CitySet citySet =
        CitySet.buildFromTextIterator(content.iterator(), "\t", provider);
    final FuzzyPrefixQuery<IKeyRecord<String>> query = new FuzzyPrefixQuery<>(
        citySet, provider, new PostingBeforeRecordRanking<>());

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("foot");
    final List<Posting> firstResults = query.searchAnd(firstQuery);
    Assert.assertEquals(4, firstResults.size());
    for (final Posting posting : firstResults) {
      final int cityId = posting.getId();
      final double prefixEditDistance = posting.getScore();
      final City city = (City) citySet.getKeyRecordById(cityId);
      final int score = city.getScore();

      if (cityId == 0) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(3, score);
      } else if (cityId == 1) {
        Assert.assertEquals(1, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else if (cityId == 2) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(2, score);
      } else if (cityId == 3) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else {
        Assert.fail();
      }
    }

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("woob");
    final List<Posting> secondResults = query.searchAnd(secondQuery);
    Assert.assertEquals(1, secondResults.size());
    final Posting posting = secondResults.iterator().next();
    final int cityId = posting.getId();
    final double prefixEditDistance = posting.getScore();
    final City city = (City) citySet.getKeyRecordById(cityId);
    final int score = city.getScore();

    Assert.assertEquals(1, cityId);
    Assert.assertEquals(1, prefixEditDistance, 0);
    Assert.assertEquals(1, score);
  }

  /**
   * Test method for
   * {@link FuzzyPrefixQuery#FuzzyPrefixQuery(IKeyRecordSet, QGramProvider)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testFuzzyPrefixQueryWithoutIRankingProvider() {
    final QGramProvider provider = new QGramProvider(3, '$');
    final String firstCity = "Football\t3\t0\t0";
    final String secondCity = "foobar\t1\t0\t0";
    final String thirdCity = "Footsal\t2\t0\t0";
    final String fourthCity = "Foot Barca\t1\t0\t0";
    final List<String> content = new LinkedList<>();
    content.add(firstCity);
    content.add(secondCity);
    content.add(thirdCity);
    content.add(fourthCity);
    final CitySet citySet =
        CitySet.buildFromTextIterator(content.iterator(), "\t", provider);
    final FuzzyPrefixQuery<IKeyRecord<String>> query =
        new FuzzyPrefixQuery<>(citySet, provider);

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("foot");
    final List<Posting> firstResults = query.searchAnd(firstQuery);
    Assert.assertEquals(4, firstResults.size());
    for (final Posting posting : firstResults) {
      final int cityId = posting.getId();
      final double prefixEditDistance = posting.getScore();
      final City city = (City) citySet.getKeyRecordById(cityId);
      final int score = city.getScore();

      if (cityId == 0) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(3, score);
      } else if (cityId == 1) {
        Assert.assertEquals(1, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else if (cityId == 2) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(2, score);
      } else if (cityId == 3) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else {
        Assert.fail();
      }
    }

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("woob");
    final List<Posting> secondResults = query.searchAnd(secondQuery);
    Assert.assertEquals(1, secondResults.size());
    final Posting posting = secondResults.iterator().next();
    final int cityId = posting.getId();
    final double prefixEditDistance = posting.getScore();
    final City city = (City) citySet.getKeyRecordById(cityId);
    final int score = city.getScore();

    Assert.assertEquals(1, cityId);
    Assert.assertEquals(1, prefixEditDistance, 0);
    Assert.assertEquals(1, score);
  }

  /**
   * Test method for {@link FuzzyPrefixQuery#searchAnd(Iterable)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testSearchAnd() {
    // Test required by lecture
    final QGramProvider provider = new QGramProvider(3, '$');
    final String firstCity = "Football\t3\t0\t0";
    final String secondCity = "foobar\t1\t0\t0";
    final String thirdCity = "Footsal\t2\t0\t0";
    final String fourthCity = "Foot Barca\t1\t0\t0";
    final List<String> content = new LinkedList<>();
    content.add(firstCity);
    content.add(secondCity);
    content.add(thirdCity);
    content.add(fourthCity);
    final CitySet citySet =
        CitySet.buildFromTextIterator(content.iterator(), "\t", provider);
    final FuzzyPrefixQuery<IKeyRecord<String>> query =
        new FuzzyPrefixQuery<>(citySet, provider);

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("foot");
    final List<Posting> firstResults = query.searchAnd(firstQuery);
    Assert.assertEquals(4, firstResults.size());
    for (final Posting posting : firstResults) {
      final int cityId = posting.getId();
      final double prefixEditDistance = posting.getScore();
      final City city = (City) citySet.getKeyRecordById(cityId);
      final int score = city.getScore();

      if (cityId == 0) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(3, score);
      } else if (cityId == 1) {
        Assert.assertEquals(1, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else if (cityId == 2) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(2, score);
      } else if (cityId == 3) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else {
        Assert.fail();
      }
    }

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("woob");
    final List<Posting> secondResults = query.searchAnd(secondQuery);
    Assert.assertEquals(1, secondResults.size());
    final Posting posting = secondResults.iterator().next();
    final int cityId = posting.getId();
    final double prefixEditDistance = posting.getScore();
    final City city = (City) citySet.getKeyRecordById(cityId);
    final int score = city.getScore();

    Assert.assertEquals(1, cityId);
    Assert.assertEquals(1, prefixEditDistance, 0);
    Assert.assertEquals(1, score);
  }

  /**
   * Test method for {@link FuzzyPrefixQuery#searchOr(Iterable)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testSearchOr() {
    final QGramProvider provider = new QGramProvider(3, '$');
    final String firstCity = "Football\t3\t0\t0";
    final String secondCity = "foobar\t1\t0\t0";
    final String thirdCity = "Footsal\t2\t0\t0";
    final String fourthCity = "Foot Barca\t1\t0\t0";
    final List<String> content = new LinkedList<>();
    content.add(firstCity);
    content.add(secondCity);
    content.add(thirdCity);
    content.add(fourthCity);
    final CitySet citySet =
        CitySet.buildFromTextIterator(content.iterator(), "\t", provider);
    final FuzzyPrefixQuery<IKeyRecord<String>> query =
        new FuzzyPrefixQuery<>(citySet, provider);

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("foot");
    final List<Posting> firstResults = query.searchOr(firstQuery);
    Assert.assertEquals(4, firstResults.size());
    for (final Posting posting : firstResults) {
      final int cityId = posting.getId();
      final double prefixEditDistance = posting.getScore();
      final City city = (City) citySet.getKeyRecordById(cityId);
      final int score = city.getScore();

      if (cityId == 0) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(3, score);
      } else if (cityId == 1) {
        Assert.assertEquals(1, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else if (cityId == 2) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(2, score);
      } else if (cityId == 3) {
        Assert.assertEquals(0, prefixEditDistance, 0);
        Assert.assertEquals(1, score);
      } else {
        Assert.fail();
      }
    }

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("woob");
    final List<Posting> secondResults = query.searchOr(secondQuery);
    Assert.assertEquals(1, secondResults.size());
    final Posting posting = secondResults.iterator().next();
    final int cityId = posting.getId();
    final double prefixEditDistance = posting.getScore();
    final City city = (City) citySet.getKeyRecordById(cityId);
    final int score = city.getScore();

    Assert.assertEquals(1, cityId);
    Assert.assertEquals(1, prefixEditDistance, 0);
    Assert.assertEquals(1, score);
  }

}
