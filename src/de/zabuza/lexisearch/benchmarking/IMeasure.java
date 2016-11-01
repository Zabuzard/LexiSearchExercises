package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

public interface IMeasure<K> {
  double evaluateRelevance(Collection<K> keys, List<Posting> postings,
      IGroundTruth<K> groundTruth);
}
