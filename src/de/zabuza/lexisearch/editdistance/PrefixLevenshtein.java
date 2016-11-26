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
 * @param <K>
 *          Type of the objects to compare
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
    // TODO Implement
    return 0;
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
    // TODO Implement
    return 0;
  }
}