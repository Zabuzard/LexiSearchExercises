package de.zabuza.lexisearch.model.city;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;

/**
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class CitySetTest {

  /**
   * Test method for {@link CitySet#add(IKeyRecord)}.
   */
  @Test
  public void testAdd() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    Assert.assertTrue(citySet.add(city));
    Assert.assertEquals(1, citySet.size());
    Assert.assertFalse(citySet.add(city));
    Assert.assertEquals(1, citySet.size());

    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    Assert.assertTrue(citySet.add(anotherCity));
    Assert.assertEquals(2, citySet.size());
  }

  /**
   * Test method for {@link CitySet#addAll(Collection)}.
   */
  @Test
  public void testAddAll() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);

    List<City> cities = new LinkedList<>();
    cities.add(city);
    cities.add(anotherCity);
    cities.add(city);

    Assert.assertTrue(citySet.addAll(cities));
    Assert.assertEquals(2, citySet.size());

    Assert.assertTrue(citySet.contains(city));
    Assert.assertTrue(citySet.contains(anotherCity));
  }

  /**
   * Test method for
   * {@link CitySet#buildFromTextIterator(Iterator, String, IKeyProvider)}.
   */
  @Test
  public void testBuildFromTextIterator() {
    final QGramProvider provider = new QGramProvider(3);

    final String firstCity = "city\t50\t1.0\t2.0";
    final String secondCity = "city\t50\t1.0\t2.0";
    final List<String> content = new LinkedList<>();
    content.add(firstCity);
    content.add(secondCity);

    final CitySet citySet =
        CitySet.buildFromTextIterator(content.iterator(), "\t", provider);
    Assert.assertEquals(2, citySet.size());
  }

  /**
   * Test method for {@link CitySet#CitySet()}.
   */
  @Test
  public void testCitySet() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);
    Assert.assertEquals(1, citySet.size());
    Assert.assertTrue(citySet.contains(city));
  }

  /**
   * Test method for {@link CitySet#clear()}.
   */
  @Test
  public void testClear() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);

    List<City> cities = new LinkedList<>();
    cities.add(city);
    cities.add(anotherCity);
    cities.add(city);

    Assert.assertTrue(citySet.addAll(cities));
    Assert.assertEquals(2, citySet.size());

    citySet.clear();

    Assert.assertEquals(0, citySet.size());
  }

  /**
   * Test method for {@link CitySet#contains(Object)}.
   */
  @Test
  public void testContains() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);

    List<City> cities = new LinkedList<>();
    cities.add(city);
    cities.add(anotherCity);
    cities.add(city);

    Assert.assertTrue(citySet.addAll(cities));
    Assert.assertEquals(2, citySet.size());

    Assert.assertTrue(citySet.contains(city));
    Assert.assertTrue(citySet.contains(anotherCity));
  }

  /**
   * Test method for {@link CitySet#containsAll(Collection)}.
   */
  @Test
  public void testContainsAll() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);

    List<City> cities = new LinkedList<>();
    cities.add(city);
    cities.add(anotherCity);
    cities.add(city);

    Assert.assertTrue(citySet.addAll(cities));
    Assert.assertEquals(2, citySet.size());

    Assert.assertTrue(citySet.containsAll(cities));
  }

  /**
   * Test method for {@link CitySet#createInvertedIndex()}.
   */
  @Test
  public void testCreateInvertedIndex() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);
    final City anotherCity = new City(2, "cityx", 1.0f, 2.0f, 1, provider);
    citySet.add(anotherCity);

    final IInvertedIndex<String> invertedIndex = citySet.createInvertedIndex();

    Assert.assertEquals(2, invertedIndex.getRecords("cit").getSize());
    Assert.assertEquals(1, invertedIndex.getRecords("tyx").getSize());

    // Test required by lecture
    final QGramProvider testProvider = new QGramProvider(3, '$');
    final String firstCity = "Football\t3\t0\t0";
    final String secondCity = "foobar\t1\t0\t0";
    final String thirdCity = "Footsal\t2\t0\t0";
    final String fourthCity = "Foot Barca\t1\t0\t0";
    final List<String> content = new LinkedList<>();
    content.add(firstCity);
    content.add(secondCity);
    content.add(thirdCity);
    content.add(fourthCity);
    final CitySet testCitySet =
        CitySet.buildFromTextIterator(content.iterator(), "\t", testProvider);
    final IInvertedIndex<String> testInvertedIndex =
        testCitySet.createInvertedIndex();
    int amountOfKeys = 0;
    final Iterator<String> keyIter = testInvertedIndex.getKeys().iterator();
    while (keyIter.hasNext()) {
      keyIter.next();
      amountOfKeys++;
    }
    Assert.assertEquals(16, amountOfKeys);
    Assert.assertEquals(4, testInvertedIndex.getRecords("$fo").getSize());
    Assert.assertEquals(3, testInvertedIndex.getRecords("oot").getSize());
    Assert.assertEquals(2, testInvertedIndex.getRecords("tba").getSize());
  }

  /**
   * Test method for {@link CitySet#getKeyRecordById(int)}.
   */
  @Test
  public void testGetKeyRecordById() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);

    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(anotherCity);

    Assert.assertEquals(2, citySet.size());

    Assert.assertEquals(city, citySet.getKeyRecordById(1));
    Assert.assertEquals(anotherCity, citySet.getKeyRecordById(2));
    Assert.assertNull(citySet.getKeyRecordById(3));
  }

  /**
   * Test method for {@link CitySet#isEmpty()}.
   */
  @Test
  public void testIsEmpty() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);

    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(anotherCity);
    Assert.assertFalse(citySet.isEmpty());

    citySet.remove(anotherCity);
    Assert.assertFalse(citySet.isEmpty());

    citySet.remove(city);
    Assert.assertTrue(citySet.isEmpty());
  }

  /**
   * Test method for {@link CitySet#iterator()}.
   */
  @Test
  public void testIterator() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);

    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(anotherCity);

    final Iterator<IKeyRecord<String>> iter = citySet.iterator();
    final IKeyRecord<String> firstCity = iter.next();
    final IKeyRecord<String> secondCity = iter.next();
    Assert.assertEquals(city, firstCity);
    Assert.assertEquals(anotherCity, secondCity);
    Assert.assertFalse(iter.hasNext());
  }

  /**
   * Test method for {@link CitySet#remove(Object)}.
   */
  @Test
  public void testRemove() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);

    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(anotherCity);
    Assert.assertEquals(2, citySet.size());

    Assert.assertTrue(citySet.remove(anotherCity));
    Assert.assertEquals(1, citySet.size());

    Assert.assertTrue(citySet.remove(city));
    Assert.assertEquals(0, citySet.size());

    Assert.assertFalse(citySet.remove(city));
  }

  /**
   * Test method for {@link CitySet#removeAll(Collection)}.
   */
  @Test
  public void testRemoveAll() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    List<City> cities = new LinkedList<>();
    cities.add(city);
    cities.add(anotherCity);
    cities.add(city);

    citySet.add(city);
    citySet.add(anotherCity);

    Assert.assertEquals(2, citySet.size());

    Assert.assertTrue(citySet.removeAll(cities));
    Assert.assertEquals(0, citySet.size());
  }

  /**
   * Test method for {@link CitySet#retainAll(Collection)}.
   */
  @Test
  public void testRetainAll() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    List<City> cities = new LinkedList<>();
    cities.add(city);
    cities.add(city);

    citySet.add(city);
    citySet.add(anotherCity);

    Assert.assertEquals(2, citySet.size());

    Assert.assertTrue(citySet.retainAll(cities));
    Assert.assertEquals(1, citySet.size());
    Assert.assertEquals(city, citySet.getKeyRecordById(1));
  }

  /**
   * Test method for {@link CitySet#size()}.
   */
  @Test
  public void testSize() {
    final QGramProvider provider = new QGramProvider(3);
    final CitySet citySet = new CitySet();
    Assert.assertEquals(0, citySet.size());

    final City city = new City(1, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(city);
    Assert.assertEquals(1, citySet.size());
    citySet.add(city);
    Assert.assertEquals(1, citySet.size());

    final City anotherCity = new City(2, "city", 1.0f, 2.0f, 1, provider);
    citySet.add(anotherCity);
    Assert.assertEquals(2, citySet.size());
  }

}
