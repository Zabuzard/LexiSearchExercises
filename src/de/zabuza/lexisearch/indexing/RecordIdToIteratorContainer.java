package de.zabuza.lexisearch.indexing;

import java.util.Iterator;

public final class RecordIdToIteratorContainer
    implements Comparable<RecordIdToIteratorContainer> {

  private final int mRecordId;
  private final Iterator<Integer> mRemainingRecordIds;

  public RecordIdToIteratorContainer(final int recordId,
      final Iterator<Integer> remainingRecordIds) {
    mRecordId = recordId;
    mRemainingRecordIds = remainingRecordIds;
  }

  @Override
  public int compareTo(final RecordIdToIteratorContainer o) {
    return Integer.compare(getRecordId(), o.getRecordId());
  }

  /**
   * Gets the record id of this container.
   * 
   * @return The record id of this container
   */
  public int getRecordId() {
    return mRecordId;
  }

  /**
   * Gets the iterator over the remaining records ids of this container.
   * 
   * @return The iterator over the remaining records ids of this container
   */
  public Iterator<Integer> getRemainingRecordIds() {
    return mRemainingRecordIds;
  }

}
