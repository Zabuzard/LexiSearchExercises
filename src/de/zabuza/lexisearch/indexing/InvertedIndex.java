package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.HashMap;

/**
 * Generic implementation of {@link IInvertedIndex}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          The key which is content of records
 */
public final class InvertedIndex<K> implements IInvertedIndex<K> {
  /**
   * Data structure that maps content to their inverted lists.
   */
  private final HashMap<K, IInvertedList> mKeyToInvertedLists;

  /**
   * Creates a new empty inverted index.
   */
  public InvertedIndex() {
    this.mKeyToInvertedLists = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.indexing.IInvertedIndex#addRecord(java.lang.Object,
   * int)
   */
  @Override
  public boolean addRecord(final K key, final int recordId) {
    IInvertedList records = this.mKeyToInvertedLists.get(key);
    if (records == null) {
      records = new InvertedList();
    }
    final boolean wasAdded = records.addPosting(recordId);
    this.mKeyToInvertedLists.put(key, records);

    return wasAdded;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.indexing.IInvertedIndex#containsKey(java.lang.Object)
   */
  @Override
  public boolean containsKey(final K key) {
    return this.mKeyToInvertedLists.containsKey(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedIndex#containsRecord(java.lang.
   * Object, int)
   */
  @Override
  public boolean containsRecord(final K key, final int recordId) {
    final IInvertedList records = this.mKeyToInvertedLists.get(key);
    return records != null && records.containsPosting(recordId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedIndex#getKeys()
   */
  @Override
  public Iterable<K> getKeys() {
    return Collections.unmodifiableSet(this.mKeyToInvertedLists.keySet());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.indexing.IInvertedIndex#getRecords(java.lang.Object)
   */
  @Override
  public IInvertedList getRecords(final K key) {
    return this.mKeyToInvertedLists.get(key);
  }
}
