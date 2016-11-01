package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

public final class PrecisionAtK<K> implements IMeasure<K> {
  private final int mKParameter;

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
   * Gets the k parameter of this measure.
   * 
   * @return The k parameter of this measure
   */
  public int getKParameter() {
    return mKParameter;
  }

}
