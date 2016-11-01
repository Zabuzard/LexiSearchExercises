package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;

public interface IGroundTruth<K> {
  Collection<Collection<K>> getKeysForRelevantRecords();

  Collection<Integer> getRelevantRecords(Collection<K> keys);

  boolean hasRelevantRecords(Collection<K> keys);
}
