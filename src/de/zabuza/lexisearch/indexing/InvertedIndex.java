package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.HashMap;

public final class InvertedIndex<KEY> implements IInvertedIndex<KEY> {
  private final HashMap<KEY, IInvertedList> mKeyToRecordIds;

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
  public boolean addRecord(final KEY key, final int recordId) {
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
  public boolean containsKey(final KEY key) {
    return mKeyToRecordIds.containsKey(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedIndex#containsRecord(java.lang.
   * Object, int)
   */
  @Override
  public boolean containsRecord(final KEY key, final int recordId) {
    final IInvertedList records = mKeyToRecordIds.get(key);
    return records != null && records.containsRecord(recordId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedIndex#getKeys()
   */
  @Override
  public Iterable<KEY> getKeys() {
    return Collections.unmodifiableSet(mKeyToRecordIds.keySet());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.indexing.IInvertedIndex#getRecords(java.lang.Object)
   */
  @Override
  public IInvertedList getRecords(final KEY key) {
    return mKeyToRecordIds.get(key);
  }
}
