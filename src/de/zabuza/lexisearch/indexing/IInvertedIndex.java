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
  public boolean addRecord(final K key, final int recordId);

  /**
   * Returns whether the inverted index contains the given key or not.
   * 
   * @param key
   *          The key in question
   * @return <tt>True</tt> if the key is contained, <tt>false</tt> otherwise
   */
  public boolean containsKey(final K key);

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
  public boolean containsRecord(final K key, final int recordId);

  /**
   * Gets all keys of this inverted index.
   * 
   * @return All keys of this inverted index
   */
  public Iterable<K> getKeys();

  /**
   * Gets all records of the given key.
   * 
   * @param key
   *          The key to get its records
   * @return All records of the given key
   */
  public IInvertedList getRecords(final K key);

}
