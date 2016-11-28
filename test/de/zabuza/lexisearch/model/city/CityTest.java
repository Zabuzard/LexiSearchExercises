package de.zabuza.lexisearch.model.city;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.qgram.QGramProvider;

/**
 * Test for {@link City}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class CityTest {

  /**
   * Test method for {@link City#buildFromText(String, String, IKeyProvider)}.
   */
  @Test
  public void testBuildFromText() {
    final QGramProvider provider = new QGramProvider(3);
    final City city =
        City.buildFromText("1\tBigCity\t50\t1.0\t2.0", "\t", provider);
    final City anotherCity =
        City.buildFromText("2\tSmallCity\t2\t6.0\t-11.0", "\t", provider);

    Assert.assertEquals(1, city.getId());
    Assert.assertEquals("BigCity", city.getName());
    Assert.assertEquals(1.0, city.getLatitude(), 0);
    Assert.assertEquals(2.0, city.getLongitude(), 0);
    Assert.assertEquals(50, city.getScore());

    Assert.assertEquals(2, anotherCity.getId());
    Assert.assertEquals("SmallCity", anotherCity.getName());
    Assert.assertEquals(6.0, anotherCity.getLatitude(), 0);
    Assert.assertEquals(-11.0, anotherCity.getLongitude(), 0);
    Assert.assertEquals(2, anotherCity.getScore());
  }

  /**
   * Test method for {@link City#City(int, String, float, float, IKeyProvider)}.
   */
  @Test
  public void testCityIntStringFloatFloatIKeyProviderOfStringString() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, provider);

    Assert.assertEquals(0, city.getId());
    Assert.assertEquals("BigCity", city.getName());
    Assert.assertEquals(1.0, city.getLatitude(), 0);
    Assert.assertEquals(2.0, city.getLongitude(), 0);
    Assert.assertEquals(City.DEFAULT_RELEVANCE_SCORE, city.getScore());
  }

  /**
   * Test method for
   * {@link City#City(int, String, float, float, int, IKeyProvider)}.
   */
  @Test
  public void testCityIntStringFloatFloatIntIKeyProviderOfStringString() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 5, provider);

    Assert.assertEquals(0, city.getId());
    Assert.assertEquals("BigCity", city.getName());
    Assert.assertEquals(1.0, city.getLatitude(), 0);
    Assert.assertEquals(2.0, city.getLongitude(), 0);
    Assert.assertEquals(5, city.getScore());
  }

  /**
   * Test method for {@link City#getId()}.
   */
  @Test
  public void testGetId() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 5, provider);
    final City anotherCity = new City(1, "SmallCity", 1.0f, 2.0f, 5, provider);

    Assert.assertEquals(0, city.getId());
    Assert.assertEquals(1, anotherCity.getId());
  }

  /**
   * Test method for {@link City#getKeys()}.
   */
  @Test
  public void testGetKeys() {
    final QGramProvider provider = new QGramProvider(3, '$');
    final City city = new City(0, "abc", 1.0f, 2.0f, 100, provider);
    final City anotherCity = new City(1, "abcdef", 1.0f, 6.0f, 5, provider);

    final String[] keys = city.getKeys();
    Assert.assertEquals(3, keys.length);
    Assert.assertEquals("$$a", keys[0]);
    Assert.assertEquals("$ab", keys[1]);
    Assert.assertEquals("abc", keys[2]);

    final String[] anotherKeys = anotherCity.getKeys();
    Assert.assertEquals(6, anotherKeys.length);
    Assert.assertEquals("$$a", anotherKeys[0]);
    Assert.assertEquals("$ab", anotherKeys[1]);
    Assert.assertEquals("abc", anotherKeys[2]);
    Assert.assertEquals("bcd", anotherKeys[3]);
    Assert.assertEquals("cde", anotherKeys[4]);
    Assert.assertEquals("def", anotherKeys[5]);
  }

  /**
   * Test method for {@link City#getLatitude()}.
   */
  @Test
  public void testGetLatitude() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 5, provider);
    final City anotherCity = new City(1, "SmallCity", 6.0f, 2.0f, 5, provider);

    Assert.assertEquals(1.0, city.getLatitude(), 0);
    Assert.assertEquals(6.0, anotherCity.getLatitude(), 0);
  }

  /**
   * Test method for {@link City#getLongitude()}.
   */
  @Test
  public void testGetLongitude() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 5, provider);
    final City anotherCity = new City(1, "SmallCity", 1.0f, 6.0f, 5, provider);

    Assert.assertEquals(2.0, city.getLongitude(), 0);
    Assert.assertEquals(6.0, anotherCity.getLongitude(), 0);
  }

  /**
   * Test method for {@link City#getName()}.
   */
  @Test
  public void testGetName() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 5, provider);
    final City anotherCity = new City(1, "SmallCity", 1.0f, 6.0f, 5, provider);

    Assert.assertEquals("BigCity", city.getName());
    Assert.assertEquals("SmallCity", anotherCity.getName());
  }

  /**
   * Test method for {@link City#getRecordId()}.
   */
  @Test
  public void testGetRecordId() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 5, provider);
    final City anotherCity = new City(1, "SmallCity", 1.0f, 6.0f, 5, provider);

    Assert.assertEquals(0, city.getRecordId());
    Assert.assertEquals(1, anotherCity.getRecordId());
  }

  /**
   * Test method for {@link City#getScore()}.
   */
  @Test
  public void testGetScore() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "BigCity", 1.0f, 2.0f, 100, provider);
    final City anotherCity = new City(1, "SmallCity", 1.0f, 6.0f, 5, provider);

    Assert.assertEquals(100, city.getScore());
    Assert.assertEquals(5, anotherCity.getScore());
  }

  /**
   * Test method for {@link City#getSize()}.
   */
  @Test
  public void testGetSize() {
    final QGramProvider provider = new QGramProvider(3);
    final City city = new City(0, "abc", 1.0f, 2.0f, 100, provider);
    final City anotherCity = new City(1, "abcdef", 1.0f, 6.0f, 5, provider);

    Assert.assertEquals(3, city.getSize());
    Assert.assertEquals(6, anotherCity.getSize());
  }

}
