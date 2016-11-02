package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Implementation of {@link IMeasure} which measures the precision of the given
 * results up to a given length of elements. For example, if the resulting list
 * first contains a relevant, then an non-relevant, last a relevant record and
 * the length is 1 then the precision is <tt>1.0</tt>. If the length is 2 then
 * the precision is <tt>0.5</tt> and for 3 it is <tt>0.66</tt>.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public final class PrecisionAtK<K> implements IMeasure<K> {
  /**
   * The length of the list to consider.
   */
  private final int mKParameter;

  /**
   * Creates a new precision at k measure with a given value for k. It measures
   * the precision of the given results up to the given length of elements k.
   * For example, if the resulting list first contains a relevant, then an
   * non-relevant, last a relevant record and the length is 1 then the precision
   * is <tt>1.0</tt>. If the length is 2 then the precision is <tt>0.5</tt> and
   * for 3 it is <tt>0.66</tt>.
   * 
   * @param kParameter
   *          The length of the list to consider
   */
  public PrecisionAtK(final int kParameter) {
    mKParameter = kParameter;
  }

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
    final Collection<Integer> relevantRecords =
        groundTruth.getRelevantRecords(keys);
    final Iterator<Posting> postingsIter = postings.iterator();

    int counter = 0;
    int amountOfRelevantRecords = 0;
    while (postingsIter.hasNext() && counter < mKParameter) {
      final int currentRecordId = postingsIter.next().getId();
      if (relevantRecords != null
          && relevantRecords.contains(currentRecordId)) {
        amountOfRelevantRecords++;
      }

      counter++;
    }

    final double precisionAtK;
    if (counter == 0) {
      precisionAtK = 0;
    } else {
      precisionAtK = (amountOfRelevantRecords + 0.0) / counter;
    }

    return precisionAtK;
  }

  /**
   * Gets the k parameter of this measure which is the length of elements to
   * consider.
   * 
   * @return The k parameter of this measure which is the length of elements to
   *         consider
   */
  public int getKParameter() {
    return mKParameter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.benchmarking.IMeasure#getMeasureName()
   */
  @Override
  public String getMeasureName() {
    return "P@" + mKParameter;
  }

}
