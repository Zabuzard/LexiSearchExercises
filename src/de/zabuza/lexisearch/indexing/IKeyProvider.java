package de.zabuza.lexisearch.indexing;

/**
 * Interface for key provider. They provide methods for {@link IKeyRecord}s.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          The key of the interface
 * @param <R>
 *          The record of the interface
 */
public interface IKeyProvider<K, R> {
  /**
   * /** Provides all keys contained by this record.
   * 
   * @param record
   *          The record in question
   * @return All keys contained by this record
   */
  K[] getKeys(R record);

  /**
   * Provides the size of this record.
   * 
   * @param record
   *          The record in question
   * @return The size of this record
   */
  int getSize(R record);
}
