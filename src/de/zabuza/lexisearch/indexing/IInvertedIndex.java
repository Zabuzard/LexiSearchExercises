package de.zabuza.lexisearch.indexing;

/**
 * Interface for inverted indices. Such indices list records by their content
 * instead of the other way around.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          The key which is content of records
 */
public interface IInvertedIndex<K> {

  /**
   * Adds a record to the given key.
   * 
   * @param key
   *          The key to add the record to
   * @param recordId
   *          The record to add
   * @return If the record was added, i.e. if it was not already contained
   */
  boolean addRecord(K key, int recordId);

  /**
   * Returns whether the inverted index contains the given key or not.
   * 
   * @param key
   *          The key in question
   * @return <tt>True</tt> if the key is contained, <tt>false</tt> otherwise
   */
  boolean containsKey(K key);

  /**
   * Returns whether the given key contains the given record or not.
   * 
   * @param key
   *          The key in question
   * @param recordId
   *          The record in question
   * @return <tt>True</tt> if the record is contained by the key, <tt>false</tt>
   *         otherwise
   */
  boolean containsRecord(K key, int recordId);

  /**
   * Gets all keys of this inverted index.
   * 
   * @return All keys of this inverted index
   */
  Iterable<K> getKeys();

  /**
   * Gets all records of the given key.
   * 
   * @param key
   *          The key to get its records
   * @return All records of the given key
   */
  IInvertedList getRecords(K key);

}
