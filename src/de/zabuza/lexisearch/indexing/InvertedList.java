package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of {@link IInvertedList} which holds its records sorted at all
 * time by using a {@link SortedSet}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class InvertedList implements IInvertedList {
  /**
   * Sorted set containing all records contained by this list.
   */
  private final SortedSet<Integer> mRecords;

  /**
   * Creates a new empty inverted list.
   */
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
