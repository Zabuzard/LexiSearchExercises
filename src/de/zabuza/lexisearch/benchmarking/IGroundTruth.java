package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;

/**
 * Interface for ground truth data used for benchmarking keyword query results.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public interface IGroundTruth<K> {
  /**
   * Gets all keys for which the object holds relevant record data.
   * 
   * @return All keys for which the object holds relevant record data
   */
  Collection<Collection<K>> getKeysForRelevantRecords();

  /**
   * Gets all relevant records for the given keys according to this ground
   * truth.
   * 
   * @param keys
   *          The keys to get the records for
   * @return The IDs of all relevant records for the given keys according to
   *         this ground truth
   */
  Collection<Integer> getRelevantRecords(Collection<K> keys);

  /**
   * Returns whether this ground truth object holds relevant record data for the
   * given keys or not.
   * 
   * @param keys
   *          The keys in question
   * @return <tt>True</tt> if this ground truth object holds relevant record
   *         data for the given keys, <tt>false</tt> otherwise.
   */
  boolean hasRelevantRecords(Collection<K> keys);
}
