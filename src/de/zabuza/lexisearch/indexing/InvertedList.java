package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public final class InvertedList implements IInvertedList {
  private final SortedSet<Integer> mRecords;

  public InvertedList() {
    mRecords = new TreeSet<Integer>();
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
    return Collections.unmodifiableSortedSet(mRecords);
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
