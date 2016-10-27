package de.zabuza.lexisearch.indexing;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link AInvertedList}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class AInvertedListTest {

  /**
   * Test method for
   * {@link AInvertedList#intersect(AInvertedList, AInvertedList)}.
   */
  @Test
  public void testIntersectAInvertedListAInvertedList() {
    final InvertedList invertedList = new InvertedList();
    invertedList.addPosting(0);
    invertedList.addPosting(1);
    invertedList.addPosting(2);
    invertedList.addPosting(3);

    final InvertedList anotherInvertedList = new InvertedList();
    anotherInvertedList.addPosting(0);
    anotherInvertedList.addPosting(3);

    final AInvertedList intersectionResult =
        AInvertedList.intersect(invertedList, anotherInvertedList);
    Assert.assertEquals(2, intersectionResult.getSize());
    Assert.assertTrue(intersectionResult.containsPosting(0));
    Assert.assertTrue(intersectionResult.containsPosting(3));
  }

  /**
   * Test method for {@link AInvertedList#intersect(Collection)}.
   */
  @Test
  public void testIntersectCollectionOfAInvertedList() {
    final InvertedList firstInvertedList = new InvertedList();
    firstInvertedList.addPosting(0);
    firstInvertedList.addPosting(1);
    firstInvertedList.addPosting(2);
    firstInvertedList.addPosting(3);

    final InvertedList secondInvertedList = new InvertedList();
    secondInvertedList.addPosting(0);
    secondInvertedList.addPosting(3);
    secondInvertedList.addPosting(4);

    final InvertedList thirdInvertedList = new InvertedList();
    thirdInvertedList.addPosting(0);
    thirdInvertedList.addPosting(3);
    thirdInvertedList.addPosting(5);

    Collection<AInvertedList> collection = new LinkedList<>();
    collection.add(firstInvertedList);
    collection.add(secondInvertedList);
    collection.add(thirdInvertedList);

    final AInvertedList intersectionResult =
        AInvertedList.intersect(collection);
    Assert.assertEquals(2, intersectionResult.getSize());
    Assert.assertTrue(intersectionResult.containsPosting(0));
    Assert.assertTrue(intersectionResult.containsPosting(3));
  }

  /**
   * Test method for {@link AInvertedList#union(AInvertedList, AInvertedList)}.
   */
  @Test
  public void testUnionAInvertedListAInvertedList() {
    final InvertedList invertedList = new InvertedList();
    invertedList.addPosting(0);
    invertedList.addPosting(1);
    invertedList.addPosting(2);
    invertedList.addPosting(3);

    final InvertedList anotherInvertedList = new InvertedList();
    anotherInvertedList.addPosting(0);
    anotherInvertedList.addPosting(3);
    anotherInvertedList.addPosting(4);

    final AInvertedList unionResult =
        AInvertedList.union(invertedList, anotherInvertedList);
    Assert.assertEquals(5, unionResult.getSize());
    Assert.assertTrue(unionResult.containsPosting(0));
    Assert.assertTrue(unionResult.containsPosting(1));
    Assert.assertTrue(unionResult.containsPosting(2));
    Assert.assertTrue(unionResult.containsPosting(3));
    Assert.assertTrue(unionResult.containsPosting(4));
  }

  /**
   * Test method for {@link AInvertedList#union(Collection)}.
   */
  @Test
  public void testUnionCollectionOfAInvertedList() {
    final InvertedList firstInvertedList = new InvertedList();
    firstInvertedList.addPosting(0);
    firstInvertedList.addPosting(1);
    firstInvertedList.addPosting(2);
    firstInvertedList.addPosting(3);

    final InvertedList secondInvertedList = new InvertedList();
    secondInvertedList.addPosting(0);
    secondInvertedList.addPosting(3);
    secondInvertedList.addPosting(4);

    final InvertedList thirdInvertedList = new InvertedList();
    thirdInvertedList.addPosting(0);
    thirdInvertedList.addPosting(3);
    thirdInvertedList.addPosting(5);

    Collection<AInvertedList> collection = new LinkedList<>();
    collection.add(firstInvertedList);
    collection.add(secondInvertedList);
    collection.add(thirdInvertedList);

    final AInvertedList unionResult = AInvertedList.union(collection);
    Assert.assertEquals(6, unionResult.getSize());
    Assert.assertTrue(unionResult.containsPosting(0));
    Assert.assertTrue(unionResult.containsPosting(1));
    Assert.assertTrue(unionResult.containsPosting(2));
    Assert.assertTrue(unionResult.containsPosting(3));
    Assert.assertTrue(unionResult.containsPosting(4));
    Assert.assertTrue(unionResult.containsPosting(5));
  }

}
