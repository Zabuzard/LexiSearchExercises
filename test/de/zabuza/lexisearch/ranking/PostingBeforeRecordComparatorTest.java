package de.zabuza.lexisearch.ranking;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.City;
import de.zabuza.lexisearch.model.city.CitySet;

/**
 * Test for {@link PostingBeforeRecordComparator}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PostingBeforeRecordComparatorTest {

  /**
   * Test method for
   * {@link PostingBeforeRecordComparator#compare(Posting, Posting)}.
   */
  @Test
  public void testCompare() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet cities = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    cities.add(city);
    cities.add(anotherCity);
    cities.add(yetAnotherCity);

    final Posting posting = new Posting(1, 1, 1);
    final Posting anotherPosting = new Posting(2, 1, 1);
    final Posting yetAnotherPosting = new Posting(3, 1, 1);
    final Posting differentPosting = new Posting(2, 1, 2);

    final PostingBeforeRecordComparator<IKeyRecord<String>, String> comp =
        new PostingBeforeRecordComparator<>(cities);

    Assert.assertTrue(comp.compare(posting, anotherPosting) > 0);
    Assert.assertTrue(comp.compare(posting, yetAnotherPosting) == 0);
    Assert.assertTrue(comp.compare(posting, differentPosting) < 0);
  }

  /**
   * Test method for
   * {@link PostingBeforeRecordComparator#PostingBeforeRecordComparator()}.
   */
  @Test
  public void testPostingBeforeRecordComparator() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);
    citySet.add(anotherCity);
    citySet.add(yetAnotherCity);

    final Posting posting = new Posting(1, 1, 1);
    final Posting anotherPosting = new Posting(2, 1, 1);
    final Posting yetAnotherPosting = new Posting(3, 1, 1);
    final Posting differentPosting = new Posting(2, 1, 2);

    final PostingBeforeRecordComparator<IKeyRecord<String>, String> comp =
        new PostingBeforeRecordComparator<>(citySet);

    Assert.assertTrue(comp.compare(posting, anotherPosting) > 0);
    Assert.assertTrue(comp.compare(posting, yetAnotherPosting) == 0);
    Assert.assertTrue(comp.compare(posting, differentPosting) < 0);
  }

  /**
   * Test method for
   * {@link PostingBeforeRecordComparator#setKeyRecords(IKeyRecordSet)}.
   */
  @Test
  public void testSetKeyRecords() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 2, provider);
    final City yetAnotherCity = new City(3, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);
    citySet.add(anotherCity);
    citySet.add(yetAnotherCity);

    final Posting posting = new Posting(1, 1, 1);
    final Posting anotherPosting = new Posting(2, 1, 1);
    final Posting yetAnotherPosting = new Posting(3, 1, 1);
    final Posting differentPosting = new Posting(2, 1, 2);

    final PostingBeforeRecordComparator<IKeyRecord<String>, String> comp =
        new PostingBeforeRecordComparator<>(new CitySet());
    comp.setKeyRecords(citySet);

    Assert.assertTrue(comp.compare(posting, anotherPosting) > 0);
    Assert.assertTrue(comp.compare(posting, yetAnotherPosting) == 0);
    Assert.assertTrue(comp.compare(posting, differentPosting) < 0);
  }

}
