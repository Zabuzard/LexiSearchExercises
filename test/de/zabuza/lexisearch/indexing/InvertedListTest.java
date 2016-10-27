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
   * Test method for {@link InvertedList#addPosting(int)}.
   */
  @Test
  public void testAddPostingInt() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addPosting(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#addPosting(int, int)}.
   */
  @Test
  public void testAddPostingIntInt() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1, 1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addPosting(2, 2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(2, 3);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(4, 4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#containsPosting(int)}.
   */
  @Test
  public void testContainsPosting() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1);
    Assert.assertTrue(invertedList.containsPosting(1));
    Assert.assertFalse(invertedList.containsPosting(2));
    invertedList.addPosting(2);
    Assert.assertTrue(invertedList.containsPosting(2));
    invertedList.addPosting(2);
    Assert.assertTrue(invertedList.containsPosting(2));
    invertedList.addPosting(4);
    Assert.assertTrue(invertedList.containsPosting(4));
    Assert.assertFalse(invertedList.containsPosting(3));
  }

  /**
   * Test method for {@link InvertedList#getPostings()}.
   */
  @Test
  public void testGetPostings() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1);
    invertedList.addPosting(4);
    invertedList.addPosting(2);

    final Iterator<Posting> records = invertedList.getPostings().iterator();

    Assert.assertEquals(1, records.next().getId());
    Assert.assertEquals(2, records.next().getId());
    Assert.assertEquals(4, records.next().getId());

    Assert.assertFalse(records.hasNext());
  }

  /**
   * Test method for {@link InvertedList#getSize()}.
   */
  @Test
  public void testGetSize() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addPosting(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#InvertedList()}.
   */
  @Test
  public void testInvertedList() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1);
    Assert.assertEquals(1, invertedList.getSize());
    invertedList.addPosting(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(2);
    Assert.assertEquals(2, invertedList.getSize());
    invertedList.addPosting(4);
    Assert.assertEquals(3, invertedList.getSize());
  }

  /**
   * Test method for {@link InvertedList#isEmpty()}.
   */
  @Test
  public void testIsEmpty() {
    final InvertedList invertedList = new InvertedList();
    Assert.assertTrue(invertedList.isEmpty());

    invertedList.addPosting(1);
    Assert.assertFalse(invertedList.isEmpty());
    invertedList.addPosting(2);
    Assert.assertFalse(invertedList.isEmpty());
    invertedList.addPosting(4);
    Assert.assertFalse(invertedList.isEmpty());
  }

}
