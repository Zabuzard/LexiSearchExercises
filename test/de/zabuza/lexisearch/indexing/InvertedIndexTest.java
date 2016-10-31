package de.zabuza.lexisearch.indexing;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link InvertedIndex}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class InvertedIndexTest {

  /**
   * Test method for {@link InvertedIndex#addRecord(Object, int)}.
   */
  @Test
  public void testAddRecord() {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();
    invertedIndex.addRecord("a", 1);
    invertedIndex.addRecord("a", 2);

    Assert.assertFalse(invertedIndex.containsKey("b"));
    invertedIndex.addRecord("b", 1);

    Assert.assertTrue(invertedIndex.containsKey("a"));
    Assert.assertTrue(invertedIndex.containsKey("b"));

    Assert.assertTrue(invertedIndex.containsRecord("a", 1));
    Assert.assertTrue(invertedIndex.containsRecord("a", 2));
    Assert.assertTrue(invertedIndex.containsRecord("b", 1));
    Assert.assertFalse(invertedIndex.containsRecord("b", 2));
  }

  /**
   * Test method for {@link InvertedIndex#containsKey(Object)}.
   */
  @Test
  public void testContainsKey() {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();
    invertedIndex.addRecord("a", 1);
    invertedIndex.addRecord("a", 2);

    Assert.assertFalse(invertedIndex.containsKey("b"));
    invertedIndex.addRecord("b", 1);

    Assert.assertTrue(invertedIndex.containsKey("a"));
    Assert.assertTrue(invertedIndex.containsKey("b"));

    Assert.assertTrue(invertedIndex.containsRecord("a", 1));
    Assert.assertTrue(invertedIndex.containsRecord("a", 2));
    Assert.assertTrue(invertedIndex.containsRecord("b", 1));
    Assert.assertFalse(invertedIndex.containsRecord("b", 2));
  }

  /**
   * Test method for {@link InvertedIndex#containsRecord(Object, int)}.
   */
  @Test
  public void testContainsRecord() {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();
    invertedIndex.addRecord("a", 1);
    invertedIndex.addRecord("a", 2);

    Assert.assertFalse(invertedIndex.containsKey("b"));
    invertedIndex.addRecord("b", 1);

    Assert.assertTrue(invertedIndex.containsKey("a"));
    Assert.assertTrue(invertedIndex.containsKey("b"));

    Assert.assertTrue(invertedIndex.containsRecord("a", 1));
    Assert.assertTrue(invertedIndex.containsRecord("a", 2));
    Assert.assertTrue(invertedIndex.containsRecord("b", 1));
    Assert.assertFalse(invertedIndex.containsRecord("b", 2));
  }

  /**
   * Test method for {@link InvertedIndex#getKeys()}.
   */
  @Test
  public void testGetKeys() {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();

    final String key = "a";
    final String anotherKey = "b";
    invertedIndex.addRecord(key, 1);
    invertedIndex.addRecord(key, 2);

    Assert.assertFalse(invertedIndex.containsKey(anotherKey));
    invertedIndex.addRecord(anotherKey, 1);

    Assert.assertTrue(invertedIndex.containsKey(key));
    Assert.assertTrue(invertedIndex.containsKey(anotherKey));

    final Iterator<String> keys = invertedIndex.getKeys().iterator();
    final String firstKey = keys.next();
    final String secondKey = keys.next();
    Assert.assertEquals(key, firstKey);
    Assert.assertEquals(anotherKey, secondKey);

    Assert.assertFalse(keys.hasNext());

  }

  /**
   * Test method for {@link InvertedIndex#getRecords(Object)}.
   */
  @Test
  public void testGetRecords() {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();

    final String key = "a";
    final String anotherKey = "b";
    invertedIndex.addRecord(key, 1);
    invertedIndex.addRecord(key, 2);

    Assert.assertFalse(invertedIndex.containsKey(anotherKey));
    invertedIndex.addRecord(anotherKey, 1);

    Assert.assertTrue(invertedIndex.containsKey(key));
    Assert.assertTrue(invertedIndex.containsKey(anotherKey));

    final IInvertedList invertedList = invertedIndex.getRecords(key);
    Assert.assertEquals(2, invertedList.getSize());

    final IInvertedList anotherInvertedList =
        invertedIndex.getRecords(anotherKey);
    Assert.assertEquals(1, anotherInvertedList.getSize());
  }

  /**
   * Test method for {@link InvertedIndex#InvertedIndex()}.
   */
  @Test
  public void testInvertedIndex() {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();
    invertedIndex.addRecord("a", 1);
    invertedIndex.addRecord("a", 2);

    Assert.assertFalse(invertedIndex.containsKey("b"));
    invertedIndex.addRecord("b", 1);

    Assert.assertTrue(invertedIndex.containsKey("a"));
    Assert.assertTrue(invertedIndex.containsKey("b"));

    Assert.assertTrue(invertedIndex.containsRecord("a", 1));
    Assert.assertTrue(invertedIndex.containsRecord("a", 2));
    Assert.assertTrue(invertedIndex.containsRecord("b", 1));
    Assert.assertFalse(invertedIndex.containsRecord("b", 2));
  }

}
