package de.zabuza.lexisearch.editdistance;

/**
 * Interface for edit distances. Offers methods to compute the edit distance
 * between given objects.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the objects to compare
 */
public interface IEditDistance<K> {
  /**
   * Computes the edit distance between the given objects according to the
   * implementing technique.
   * 
   * @param first
   *          The first object
   * @param second
   *          The second object
   * @return The edit distance between the given objects according to the
   *         implementing technique
   */
  int distance(K first, K second);

  /**
   * Estimates the edit distance between the given objects according to the
   * implementing technique. The method only computes whether the distance is
   * greater or lower equals the given bound.
   * 
   * @param first
   *          The first object
   * @param second
   *          The second object
   * @param bound
   *          The bound for the estimation
   * @return The estimated edit distance between the given objects according to
   *         the implementing technique.
   */
  int estimatedDistance(K first, K second, int bound);
}
