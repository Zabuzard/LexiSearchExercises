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
    invertedList.addRecord(0);
    invertedList.addRecord(1);
    invertedList.addRecord(2);
    invertedList.addRecord(3);

    final InvertedList anotherInvertedList = new InvertedList();
    anotherInvertedList.addRecord(0);
    anotherInvertedList.addRecord(3);

    final AInvertedList intersectionResult =
        AInvertedList.intersect(invertedList, anotherInvertedList);
    Assert.assertEquals(2, intersectionResult.getSize());
    Assert.assertTrue(intersectionResult.containsRecord(0));
    Assert.assertTrue(intersectionResult.containsRecord(3));
  }

  /**
   * Test method for {@link AInvertedList#intersect(Collection)}.
   */
  @Test
  public void testIntersectCollectionOfAInvertedList() {
    final InvertedList firstInvertedList = new InvertedList();
    firstInvertedList.addRecord(0);
    firstInvertedList.addRecord(1);
    firstInvertedList.addRecord(2);
    firstInvertedList.addRecord(3);

    final InvertedList secondInvertedList = new InvertedList();
    secondInvertedList.addRecord(0);
    secondInvertedList.addRecord(3);
    secondInvertedList.addRecord(4);

    final InvertedList thirdInvertedList = new InvertedList();
    thirdInvertedList.addRecord(0);
    thirdInvertedList.addRecord(3);
    thirdInvertedList.addRecord(5);

    Collection<AInvertedList> collection = new LinkedList<>();
    collection.add(firstInvertedList);
    collection.add(secondInvertedList);
    collection.add(thirdInvertedList);

    final AInvertedList intersectionResult =
        AInvertedList.intersect(collection);
    Assert.assertEquals(2, intersectionResult.getSize());
    Assert.assertTrue(intersectionResult.containsRecord(0));
    Assert.assertTrue(intersectionResult.containsRecord(3));
  }

  /**
   * Test method for {@link AInvertedList#union(AInvertedList, AInvertedList)}.
   */
  @Test
  public void testUnionAInvertedListAInvertedList() {
    final InvertedList invertedList = new InvertedList();
    invertedList.addRecord(0);
    invertedList.addRecord(1);
    invertedList.addRecord(2);
    invertedList.addRecord(3);

    final InvertedList anotherInvertedList = new InvertedList();
    anotherInvertedList.addRecord(0);
    anotherInvertedList.addRecord(3);
    anotherInvertedList.addRecord(4);

    final AInvertedList unionResult =
        AInvertedList.union(invertedList, anotherInvertedList);
    Assert.assertEquals(5, unionResult.getSize());
    Assert.assertTrue(unionResult.containsRecord(0));
    Assert.assertTrue(unionResult.containsRecord(1));
    Assert.assertTrue(unionResult.containsRecord(2));
    Assert.assertTrue(unionResult.containsRecord(3));
    Assert.assertTrue(unionResult.containsRecord(4));
  }

  /**
   * Test method for {@link AInvertedList#union(Collection)}.
   */
  @Test
  public void testUnionCollectionOfAInvertedList() {
    final InvertedList firstInvertedList = new InvertedList();
    firstInvertedList.addRecord(0);
    firstInvertedList.addRecord(1);
    firstInvertedList.addRecord(2);
    firstInvertedList.addRecord(3);

    final InvertedList secondInvertedList = new InvertedList();
    secondInvertedList.addRecord(0);
    secondInvertedList.addRecord(3);
    secondInvertedList.addRecord(4);

    final InvertedList thirdInvertedList = new InvertedList();
    thirdInvertedList.addRecord(0);
    thirdInvertedList.addRecord(3);
    thirdInvertedList.addRecord(5);

    Collection<AInvertedList> collection = new LinkedList<>();
    collection.add(firstInvertedList);
    collection.add(secondInvertedList);
    collection.add(thirdInvertedList);

    final AInvertedList unionResult = AInvertedList.union(collection);
    Assert.assertEquals(6, unionResult.getSize());
    Assert.assertTrue(unionResult.containsRecord(0));
    Assert.assertTrue(unionResult.containsRecord(1));
    Assert.assertTrue(unionResult.containsRecord(2));
    Assert.assertTrue(unionResult.containsRecord(3));
    Assert.assertTrue(unionResult.containsRecord(4));
    Assert.assertTrue(unionResult.containsRecord(5));
  }

}
