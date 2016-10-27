package de.zabuza.lexisearch.indexing;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link PostToIterCont}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PostToIterContTest {

  /**
   * Test method for {@link PostToIterCont#compareTo(PostToIterCont)}.
   */
  @Test
  public void testCompareTo() {
    final int recordId = 0;
    final int anotherRecordId = 1;
    final LinkedList<Posting> remainingPostings = new LinkedList<>();
    remainingPostings.add(new Posting(2));
    remainingPostings.add(new Posting(3));
    final Iterator<Posting> remainignPostingsIter =
        remainingPostings.iterator();
    final PostToIterCont container =
        new PostToIterCont(new Posting(recordId), remainignPostingsIter);
    final PostToIterCont anotherContainer =
        new PostToIterCont(new Posting(anotherRecordId), remainignPostingsIter);

    Assert.assertTrue(container.compareTo(container) == 0);
    Assert.assertTrue(anotherContainer.compareTo(anotherContainer) == 0);
    Assert.assertTrue(container.compareTo(anotherContainer) < 0);
    Assert.assertTrue(anotherContainer.compareTo(container) > 0);
  }

  /**
   * Test method for {@link PostToIterCont#getRecordId()}.
   */
  @Test
  public void testGetRecordId() {
    final int recordId = 0;
    final int anotherRecordId = 1;
    final LinkedList<Posting> remainingPostings = new LinkedList<>();
    remainingPostings.add(new Posting(2));
    remainingPostings.add(new Posting(3));
    final Iterator<Posting> remainignPostingsIter =
        remainingPostings.iterator();
    final PostToIterCont container =
        new PostToIterCont(new Posting(recordId), remainignPostingsIter);
    final PostToIterCont anotherContainer =
        new PostToIterCont(new Posting(anotherRecordId), remainignPostingsIter);

    Assert.assertEquals(recordId, container.getRecordId());
    Assert.assertEquals(anotherRecordId, anotherContainer.getRecordId());
  }

  /**
   * Test method for {@link PostToIterCont#getRemainingPostings()}.
   */
  @Test
  public void testGetRemainingPostings() {
    final int recordId = 0;
    final LinkedList<Posting> remainingPostings = new LinkedList<>();
    remainingPostings.add(new Posting(2));
    remainingPostings.add(new Posting(3));
    final Iterator<Posting> remainignPostingsIter =
        remainingPostings.iterator();

    final LinkedList<Posting> antoherRemainingPostings = new LinkedList<>();
    remainingPostings.add(new Posting(4));
    remainingPostings.add(new Posting(5));
    final Iterator<Posting> anotherRemainignPostingsIter =
        antoherRemainingPostings.iterator();

    final PostToIterCont container =
        new PostToIterCont(new Posting(recordId), remainignPostingsIter);
    final PostToIterCont anotherContainer =
        new PostToIterCont(new Posting(recordId), anotherRemainignPostingsIter);

    Assert.assertEquals(remainignPostingsIter,
        container.getRemainingPostings());
    Assert.assertEquals(anotherRemainignPostingsIter,
        anotherContainer.getRemainingPostings());
  }

  /**
   * Test method for {@link PostToIterCont#PostToIterCont(Posting, Iterator)} .
   */
  @Test
  public void testPostToIterCont() {
    final int recordId = 0;
    final LinkedList<Posting> remainingPostings = new LinkedList<>();
    remainingPostings.add(new Posting(1));
    remainingPostings.add(new Posting(2));
    final Iterator<Posting> remainignPostingsIter =
        remainingPostings.iterator();
    final PostToIterCont container =
        new PostToIterCont(new Posting(recordId), remainignPostingsIter);

    Assert.assertEquals(recordId, container.getRecordId());
    Assert.assertEquals(remainignPostingsIter,
        container.getRemainingPostings());
  }

}
