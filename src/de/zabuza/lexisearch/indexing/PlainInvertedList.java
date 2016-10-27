package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Implementation of {@link AInvertedList} which does not hold its records
 * sorted at all time. However it assumes they are inserted in sorted order
 * which must hold at all time. Therefore its inserting complexity is in
 * <tt>O(1)</tt>.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PlainInvertedList extends AInvertedList {
  /**
   * Unsorted set containing all records contained by this list. It assumes they
   * are inserted in sorted order.
   */
  private final LinkedHashSet<Integer> mRecords;

  /**
   * Creates a new empty inverted list.
   */
  public PlainInvertedList() {
    mRecords = new LinkedHashSet<Integer>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#addRecord(int)
   */
  @Override
  public boolean addRecord(final int recordId) {
    return mRecords.add(recordId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#containsRecord(int)
   */
  @Override
  public boolean containsRecord(final int recordId) {
    return mRecords.contains(recordId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#getRecords()
   */
  @Override
  public Iterable<Integer> getRecords() {
    return Collections.unmodifiableSet(mRecords);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#getSize()
   */
  @Override
  public int getSize() {
    return mRecords.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return mRecords.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return mRecords.toString();
  }

}
