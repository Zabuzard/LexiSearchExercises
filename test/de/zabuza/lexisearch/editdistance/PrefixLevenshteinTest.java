package de.zabuza.lexisearch.editdistance;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link PrefixLevenshtein}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PrefixLevenshteinTest {

  /**
   * Test method for
   * {@link PrefixLevenshtein#debugTableToString(String, String, int[][])}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testDebugTableToString() {
    final String lineSeparator = System.lineSeparator();
    final String xHeader = "BLOED";
    final String yHeader = "DOOF";
    final int[][] table = new int[6][5];
    table[0][0] = 0;
    table[1][0] = 1;
    table[2][0] = 2;
    table[3][0] = 3;
    table[4][0] = 4;
    table[5][0] = 5;

    table[0][1] = 1;
    table[1][1] = 1;
    table[2][1] = 2;
    table[3][1] = 3;
    table[4][1] = 4;
    table[5][1] = 4;

    table[0][2] = 2;
    table[1][2] = 2;
    table[2][2] = 2;
    table[3][2] = 2;
    table[4][2] = 3;
    table[5][2] = 4;

    table[0][3] = 3;
    table[1][3] = 3;
    table[2][3] = 3;
    table[3][3] = 2;
    table[4][3] = 3;
    table[5][3] = 4;

    table[0][4] = 4;
    table[1][4] = 4;
    table[2][4] = 4;
    table[3][4] = 3;
    table[4][4] = 3;
    table[5][4] = 4;

    final String resultingText =
        "\t-\tB\tL\tO\tE\tD\t" + lineSeparator + "-\t0\t1\t2\t3\t4\t5\t"
            + lineSeparator + "D\t1\t1\t2\t3\t4\t4\t" + lineSeparator
            + "O\t2\t2\t2\t2\t3\t4\t" + lineSeparator + "O\t3\t3\t3\t2\t3\t4\t"
            + lineSeparator + "F\t4\t4\t4\t3\t3\t4\t" + lineSeparator;
    Assert.assertEquals(resultingText,
        PrefixLevenshtein.debugTableToString(yHeader, xHeader, table));
  }

  /**
   * Test method for {@link PrefixLevenshtein#distance(String, String)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testDistance() {
    final PrefixLevenshtein distanceProvider = new PrefixLevenshtein();

    Assert.assertEquals(0, distanceProvider.distance("-", "-"));
    Assert.assertEquals(3, distanceProvider.distance("---", "abc"));
    Assert.assertEquals(0, distanceProvider.distance("abc", "abcdefg"));
    Assert.assertEquals(1, distanceProvider.distance("acc", "abcdefg"));
    Assert.assertEquals(1, distanceProvider.distance("acb", "abcdefg"));
  }

  /**
   * Test method for
   * {@link PrefixLevenshtein#estimatedDistance(String, String, int)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testEstimatedDistance() {
    final PrefixLevenshtein distanceProvider = new PrefixLevenshtein();

    Assert.assertEquals(0, distanceProvider.estimatedDistance("-", "-", 2));
    Assert.assertEquals(3, distanceProvider.estimatedDistance("---", "abc", 2));
    Assert.assertEquals(0,
        distanceProvider.estimatedDistance("abc", "abcdefg", 2));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acc", "abcdefg", 2));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acb", "abcdefg", 2));

    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acb", "abcdefg", 0));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acb", "abcdefg", 1));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acb", "abcdefg", 2));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acb", "abcdefg", 3));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("acb", "abcdefg", 4));

    // Test required by lecture
    Assert.assertEquals(0, distanceProvider.estimatedDistance("foo", "foo", 0));
    Assert.assertEquals(0,
        distanceProvider.estimatedDistance("foo", "foo", 10));
    Assert.assertEquals(0,
        distanceProvider.estimatedDistance("foo", "foot", 0));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("foot", "foo", 1));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("foo", "fotbal", 1));
    Assert.assertEquals(3, distanceProvider.estimatedDistance("foo", "bar", 3));

    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("perf", "perv", 1));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("perv", "perf", 1));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("perf", "peff", 1));

    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("foot", "foo", 0));
    Assert.assertEquals(1,
        distanceProvider.estimatedDistance("foo", "fotbal", 0));
    Assert.assertEquals(3, distanceProvider.estimatedDistance("foo", "bar", 2));
  }

}
