package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.HashMap;

public final class InvertedIndex<K> implements IInvertedIndex<K> {
  private final HashMap<K, IInvertedList> mKeyToRecordIds;

  public InvertedIndex() {
    mKeyToRecordIds = new HashMap<>();
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
    IInvertedList records = mKeyToRecordIds.get(key);
    if (records == null) {
      records = new InvertedList();
    }
    boolean wasAdded = records.addRecord(recordId);
    mKeyToRecordIds.put(key, records);

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
    return mKeyToRecordIds.containsKey(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedIndex#containsRecord(java.lang.
   * Object, int)
   */
  @Override
  public boolean containsRecord(final K key, final int recordId) {
    final IInvertedList records = mKeyToRecordIds.get(key);
    return records != null && records.containsRecord(recordId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedIndex#getKeys()
   */
  @Override
  public Iterable<K> getKeys() {
    return Collections.unmodifiableSet(mKeyToRecordIds.keySet());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.indexing.IInvertedIndex#getRecords(java.lang.Object)
   */
  @Override
  public IInvertedList getRecords(final K key) {
    return mKeyToRecordIds.get(key);
  }
}