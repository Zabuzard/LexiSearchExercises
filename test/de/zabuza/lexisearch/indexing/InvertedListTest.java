package de.zabuza.lexisearch.indexing;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link InvertedList}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class InvertedListTest {

  /**
   * Test method for {@link InvertedList#addRecord(int)}.
   */
  @Test
  public void testAddRecord() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addRecord(1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addRecord(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addRecord(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addRecord(4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#containsRecord(int)}.
   */
  @Test
  public void testContainsRecord() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addRecord(1);
    Assert.assertTrue(invertedList.containsRecord(1));
    Assert.assertFalse(invertedList.containsRecord(2));
    invertedList.addRecord(2);
    Assert.assertTrue(invertedList.containsRecord(2));
    invertedList.addRecord(2);
    Assert.assertTrue(invertedList.containsRecord(2));
    invertedList.addRecord(4);
    Assert.assertTrue(invertedList.containsRecord(4));
    Assert.assertFalse(invertedList.containsRecord(3));
  }

  /**
   * Test method for {@link InvertedList#getRecords()}.
   */
  @Test
  public void testGetRecords() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addRecord(1);
    invertedList.addRecord(4);
    invertedList.addRecord(2);

    final Iterator<Integer> records = invertedList.getRecords().iterator();

    Assert.assertEquals(new Integer(1), records.next());
    Assert.assertEquals(new Integer(2), records.next());
    Assert.assertEquals(new Integer(4), records.next());

    Assert.assertFalse(records.hasNext());
  }

  /**
   * Test method for {@link InvertedList#getSize()}.
   */
  @Test
  public void testGetSize() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addRecord(1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addRecord(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addRecord(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addRecord(4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#InvertedList()}.
   */
  @Test
  public void testInvertedList() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addRecord(1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addRecord(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addRecord(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addRecord(4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#isEmpty()}.
   */
  @Test
  public void testIsEmpty() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addRecord(1);
    Assert.assertFalse(invertedList.isEmpty());
    invertedList.addRecord(2);
    Assert.assertFalse(invertedList.isEmpty());
    invertedList.addRecord(4);
    Assert.assertFalse(invertedList.isEmpty());
  }

}
