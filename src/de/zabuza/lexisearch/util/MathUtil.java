package de.zabuza.lexisearch.util;

import java.text.DecimalFormat;

/**
 * Offers mathematical utility methods.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class MathUtil {
  /**
   * Formatter to use for decimal numbers that should get truncated up to two
   * digits after the decimal.
   */
  private static final DecimalFormat mDecimalFormatterTwo =
      new DecimalFormat("0.00");

  /**
   * Formats a given decimal number up to two digits after the decimal using no
   * rounding.
   * 
   * @param decimalNumber
   *          The number to format
   * @return The given decimal number formatted up to two digits after the
   *         decimal using no rounding
   */
  public static String formatDecimalTwo(final double decimalNumber) {
    return mDecimalFormatterTwo.format(decimalNumber);
  }

  /**
   * Computes and returns the logarithmic of the given value to the base of two.
   * 
   * @param a
   *          The value to compute its logarithmic for
   * @return The logarithmic of the given value to the base of two
   */
  public static double log2(final double a) {
    return Math.log(a) / Math.log(2);
  }

  /**
   * Utility class. No implementation.
   */
  private MathUtil() {

  }
}
