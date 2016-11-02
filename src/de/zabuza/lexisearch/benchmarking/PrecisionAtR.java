package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Implementation of {@link IMeasure} which measures the precision of the given
 * results up to a length of elements where the length is the amount of relevant
 * documents according to the ground truth. For example, if the resulting list
 * first contains a relevant, then an non-relevant and last a relevant record
 * then the length is 2 because there are two relevant records. The precision
 * then is <tt>0.5</tt> because there is one relevant and one non-relevant
 * record when only considering the first two elements.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public final class PrecisionAtR<K> implements IMeasure<K> {

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.benchmarking.IMeasure#evaluateRelevance(java.util.
   * Collection, java.util.List, de.zabuza.lexisearch.benchmarking.IGroundTruth)
   */
  @Override
  public double evaluateRelevance(final Collection<K> keys,
      final List<Posting> postings, final IGroundTruth<K> groundTruth) {
    final int amountOfRelevantRecords =
        groundTruth.getRelevantRecords(keys).size();
    final PrecisionAtK<K> precisionAtK =
        new PrecisionAtK<>(amountOfRelevantRecords);

    return precisionAtK.evaluateRelevance(keys, postings, groundTruth);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.benchmarking.IMeasure#getMeasureName()
   */
  @Override
  public String getMeasureName() {
    return "P@R";
  }

}
