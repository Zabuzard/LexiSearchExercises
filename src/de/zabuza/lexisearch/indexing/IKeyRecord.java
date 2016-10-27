package de.zabuza.lexisearch.indexing;

/**
 * Interface for key records. Such records have an ID and contain keys.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          The key of the interface
 */
public interface IKeyRecord<K> {

  /**
   * Gets all keys contained by this record.
   * 
   * @return All keys contained by this record
   */
  K[] getKeys();

  /**
   * Gets the id of this record.
   * 
   * @return The id of this record
   */
  int getRecordId();

  /**
   * Gets the size of this record.
   * 
   * @return The size of this record
   */
  int getSize();
}
