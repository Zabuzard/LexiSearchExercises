package de.zabuza.lexisearch.ranking;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Test for {@link ScoreComparator}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class ScoreComparatorTest {

  /**
   * Test method for {@link ScoreComparator#compare(Posting, Posting)}.
   */
  @Test
  public void testCompare() {
    final Posting posting = new Posting(1, 1, 1);
    final Posting anotherPosting = new Posting(2, 2, 2);
    final ScoreComparator comparator = new ScoreComparator();

    Assert.assertTrue(comparator.compare(posting, anotherPosting) < 0);
    Assert.assertTrue(comparator.compare(anotherPosting, posting) > 0);
    Assert.assertTrue(comparator.compare(posting, posting) == 0);
    Assert.assertTrue(comparator.compare(anotherPosting, anotherPosting) == 0);
  }

}
