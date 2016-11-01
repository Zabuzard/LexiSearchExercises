package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

public final class AveragePrecision<K> implements IMeasure<K> {

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
    int amountOfRelevantRecordsFound = 0;
    final int amountOfRelevantRecordsTotal = relevantRecords.size();
    double totalPrecision = 0;
    while (postingsIter.hasNext()
        && amountOfRelevantRecordsFound < amountOfRelevantRecordsTotal) {
      counter++;
      final int currentRecordId = postingsIter.next().getId();
      if (relevantRecords != null
          && relevantRecords.contains(currentRecordId)) {
        amountOfRelevantRecordsFound++;
        final PrecisionAtK<K> precisionAtK = new PrecisionAtK<>(counter);
        totalPrecision +=
            precisionAtK.evaluateRelevance(keys, postings, groundTruth);
      }
    }

    final double averagePrecision;
    if (counter == 0) {
      averagePrecision = 0;
    } else {
      averagePrecision = (totalPrecision + 0.0) / amountOfRelevantRecordsTotal;
    }

    return averagePrecision;
  }

}
