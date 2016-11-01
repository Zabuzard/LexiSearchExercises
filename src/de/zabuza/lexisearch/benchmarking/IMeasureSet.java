package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.zabuza.lexisearch.indexing.Posting;

public interface IMeasureSet<K> {
  void addMeasure(IMeasure<K> measure);

  Map<IMeasure<K>, Double> evaluateRelevance(Collection<K> keys,
      List<Posting> postings, IGroundTruth<K> groundTruth);

  Iterable<IMeasure<K>> getMeasures();
}
