package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Interface for measures. They evaluate the relevance of given keyword query
 * results compared to given ground truth data.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public interface IMeasure<K> {
  /**
   * Evaluates the relevance of the given results retrieved by a given keyword
   * query compared to given ground truth data.
   * 
   * @param keys
   *          The keyword query used to build the results
   * @param postings
   *          The resulting postings ordered by their relevance according to a
   *          specific algorithm
   * @param groundTruth
   *          The ground truth data which holds the true relevance ordering of
   *          the resulting list
   * @return A number representing the relevance of the given results compared
   *         to the ground truth data. The higher the number the closer are the
   *         results to the ground truth.
   */
  double evaluateRelevance(Collection<K> keys, List<Posting> postings,
      IGroundTruth<K> groundTruth);

  /**
   * Gets the name of this measure.
   * 
   * @return The name of this measure
   */
  String getMeasureName();
}
