package de.zabuza.lexisearch.util;

import java.text.DecimalFormat;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link MathUtil}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class MathUtilTest {

  /**
   * Test method for {@link MathUtil#formatDecimalTwo(double)}.
   */
  @Test
  public void testFormatDecimalTwo() {
    final DecimalFormat format = new DecimalFormat();
    final char decimal = format.getDecimalFormatSymbols().getDecimalSeparator();
    Assert.assertEquals("0" + decimal + "12",
        MathUtil.formatDecimalTwo(0.1234f));
    Assert.assertEquals("0" + decimal + "13",
        MathUtil.formatDecimalTwo(0.1299f));
    Assert.assertEquals("0" + decimal + "10", MathUtil.formatDecimalTwo(0.1f));
    Assert.assertEquals("0" + decimal + "00", MathUtil.formatDecimalTwo(0f));
  }

  /**
   * Test method for {@link MathUtil#log2(double)}.
   */
  @Test
  public void testLog2() {
    Assert.assertEquals(1, MathUtil.log2(2), 0);
    Assert.assertEquals(2, MathUtil.log2(4), 0);
    Assert.assertEquals(3, MathUtil.log2(8), 0);
    Assert.assertEquals(4, MathUtil.log2(16), 0);
  }

}
