package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Interface for sets of measures. Can evaluate keyword query results on all
 * added measures at once.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public interface IMeasureSet<K> {
  /**
   * Adds the given measure to the set.
   * 
   * @param measure
   *          The measure to add
   */
  void addMeasure(IMeasure<K> measure);

  /**
   * Evaluates the relevance of the given results retrieved by a given keyword
   * query compared to given ground truth data for all added measures.
   * 
   * @param keys
   *          The keyword query used to build the results
   * @param postings
   *          The resulting postings ordered by their relevance according to a
   *          specific algorithm
   * @param groundTruth
   *          The ground truth data which holds the true relevance ordering of
   *          the resulting list
   * @return A structure which maps all added measures to their computed
   *         resulting relevance value, defined by
   *         {@link IMeasure#evaluateRelevance(Collection, List, IGroundTruth)}.
   */
  Map<IMeasure<K>, Double> evaluateRelevance(Collection<K> keys,
      List<Posting> postings, IGroundTruth<K> groundTruth);

  /**
   * Gets all measures which where added to this set.
   * 
   * @return All measures of this set
   */
  Iterable<IMeasure<K>> getMeasures();
}
