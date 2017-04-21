package de.zabuza.lexisearch.indexing;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link IInvertedList}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class IInvertedListTest {

  /**
   * Test method for {@link IInvertedList#intersect(Collection)}.
   */
  @SuppressWarnings("static-method")
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

    Collection<IInvertedList> collection = new LinkedList<>();
    collection.add(firstInvertedList);
    collection.add(secondInvertedList);
    collection.add(thirdInvertedList);

    final IInvertedList intersectionResult =
        IInvertedList.intersect(collection);
    Assert.assertEquals(2, intersectionResult.getSize());
    Assert.assertTrue(intersectionResult.containsPosting(0));
    Assert.assertTrue(intersectionResult.containsPosting(3));
  }

  /**
   * Test method for
   * {@link IInvertedList#intersect(IInvertedList, IInvertedList)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testIntersectIInvertedListIInvertedList() {
    final InvertedList invertedList = new InvertedList();
    invertedList.addPosting(0);
    invertedList.addPosting(1);
    invertedList.addPosting(2);
    invertedList.addPosting(3);

    final InvertedList anotherInvertedList = new InvertedList();
    anotherInvertedList.addPosting(0);
    anotherInvertedList.addPosting(3);

    final IInvertedList intersectionResult =
        IInvertedList.intersect(invertedList, anotherInvertedList);
    Assert.assertEquals(2, intersectionResult.getSize());
    Assert.assertTrue(intersectionResult.containsPosting(0));
    Assert.assertTrue(intersectionResult.containsPosting(3));
  }

  /**
   * Test method for {@link IInvertedList#union(IInvertedList, IInvertedList)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testUnionAInvertedListAInvertedList() {
    // Test required by lecture
    final InvertedList invertedList = new InvertedList();
    invertedList.addPosting(2);
    invertedList.addPosting(5);
    invertedList.addPosting(7);
    invertedList.addPosting(8);

    final InvertedList anotherInvertedList = new InvertedList();
    anotherInvertedList.addPosting(4);
    anotherInvertedList.addPosting(5);
    anotherInvertedList.addPosting(6);
    anotherInvertedList.addPosting(8);
    anotherInvertedList.addPosting(9);

    final IInvertedList unionResult =
        IInvertedList.union(invertedList, anotherInvertedList);
    Assert.assertEquals(7, unionResult.getSize());
    Assert.assertTrue(unionResult.containsPosting(2));
    Assert.assertTrue(unionResult.containsPosting(4));
    Assert.assertTrue(unionResult.containsPosting(5));
    Assert.assertTrue(unionResult.containsPosting(6));
    Assert.assertTrue(unionResult.containsPosting(7));
    Assert.assertTrue(unionResult.containsPosting(8));
    Assert.assertTrue(unionResult.containsPosting(9));
  }

  /**
   * Test method for {@link IInvertedList#union(Collection)}.
   */
  @SuppressWarnings("static-method")
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

    Collection<IInvertedList> collection = new LinkedList<>();
    collection.add(firstInvertedList);
    collection.add(secondInvertedList);
    collection.add(thirdInvertedList);

    final IInvertedList unionResult = IInvertedList.union(collection);
    Assert.assertEquals(6, unionResult.getSize());
    Assert.assertTrue(unionResult.containsPosting(0));
    Assert.assertTrue(unionResult.containsPosting(1));
    Assert.assertTrue(unionResult.containsPosting(2));
    Assert.assertTrue(unionResult.containsPosting(3));
    Assert.assertTrue(unionResult.containsPosting(4));
    Assert.assertTrue(unionResult.containsPosting(5));
  }

}
