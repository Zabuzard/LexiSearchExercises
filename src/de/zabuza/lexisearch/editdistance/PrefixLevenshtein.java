package de.zabuza.lexisearch.editdistance;

/**
 * Implementation of {@link IEditDistance} which computes the edit distance
 * between given {@link String}s by a prefix-levenshtein technique. For example,
 * the distance between <tt>Abc</tt> and <tt>Abcde</tt> is <tt>0</tt> since one
 * is a prefix of the other. In other cases the original levenshtein distance is
 * computed.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 * 
 */
public final class PrefixLevenshtein implements IEditDistance<String> {
  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.editdistance.IEditDistance#distance(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public int distance(final String first, final String second) {
    return computeDistanceWithTable(first, second);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.editdistance.IEditDistance#estimatedDistance(java.lang
   * .Object, java.lang.Object, int)
   */
  @Override
  public int estimatedDistance(final String first, final String second,
      final int bound) {
    // If the first object x is shorter, it's enough to compute only the first
    // |x| + bound + 1 columns.
    if (first.length() < second.length()) {
      return computeDistanceWithTable(first, second.substring(0,
          Math.min(first.length() + bound, second.length())));
    } else {
      // Fall back
      return computeDistanceWithTable(first, second);
    }
  }

  /**
   * Computes the prefix levenshtein distance between the two given objects
   * using a 2-dimensional array-table and an iterative approach.
   * 
   * @param first
   *          The first object
   * @param second
   *          The second object
   * @return The prefix levenshtein distance between the two given objects
   */
  private int computeDistanceWithTable(final String first,
      final String second) {
    // Build a 2-dim table where the headers are the objects
    final int[][] table = new int[second.length() + 1][first.length() + 1];

    // Initialize the table by filling the first row and column
    for (int x = 0; x < table.length; x++) {
      table[x][0] = x;
    }
    for (int y = 1; y < table[0].length; y++) {
      table[0][y] = y;
    }

    // Process the table row by row using a selective formula
    for (int y = 1; y < table[0].length; y++) {
      for (int x = 1; x < table.length; x++) {
        // Take the minimum of all candidates
        final int upperCandidate = table[x][y - 1] + 1;
        final int leftCandidate = table[x - 1][y] + 1;
        final int diagonalCandidate;
        if (first.charAt(y - 1) != second.charAt(x - 1)) {
          diagonalCandidate = table[x - 1][y - 1] + 1;
        } else {
          diagonalCandidate = table[x - 1][y - 1];
        }
        final int minimum = Math.min(Math.min(upperCandidate, leftCandidate),
            diagonalCandidate);
        table[x][y] = minimum;
      }
    }

    // The prefix distance is the smallest value in the last row
    int smallestKnownValue = Integer.MAX_VALUE;
    final int lastRowY = table[0].length - 1;
    for (int x = 0; x < table.length; x++) {
      final int candidate = table[x][lastRowY];
      if (candidate < smallestKnownValue) {
        smallestKnownValue = candidate;
      }
    }

    return smallestKnownValue;
  }

  /**
   * Method used for debugging which prints the given table with the given
   * objects as headers.
   * 
   * @param first
   *          The first object, header of the y-direction
   * @param second
   *          The second object, header of the x-direction
   * @param table
   *          The table to print
   */
  protected void debugPrintTable(final String first, final String second,
      final int[][] table) {
    for (int x = -1; x < table.length; x++) {
      if (x == -1) {
        System.out.print("\t");
      } else if (x == 0) {
        System.out.print("-\t");
      } else {
        System.out.print(second.charAt(x - 1) + "\t");
      }
    }
    System.out.println();
    for (int y = 0; y < table[0].length; y++) {
      for (int x = -1; x < table.length; x++) {
        if (x == -1) {
          if (y == 0) {
            System.out.print("-\t");
          } else {
            System.out.print(first.charAt(y - 1) + "\t");
          }
        } else {
          System.out.print(table[x][y] + "\t");
        }
      }
      System.out.println();
    }
  }
}