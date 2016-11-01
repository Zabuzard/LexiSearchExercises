package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

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

}
