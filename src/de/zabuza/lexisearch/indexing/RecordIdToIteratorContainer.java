package de.zabuza.lexisearch.indexing;

import java.util.Iterator;

/**
 * Container which stores a record id and the remaining record IDs that belong
 * to a given {@link AInvertedList}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class RecordIdToIteratorContainer
    implements Comparable<RecordIdToIteratorContainer> {

  /**
   * The record id of this container.
   */
  private final int mRecordId;
  /**
   * The remaining record IDs of this container.
   */
  private final Iterator<Integer> mRemainingRecordIds;

  /**
   * Creates a new container which stores a given record id and the remaining
   * record IDs that belong to a given {@link AInvertedList}.
   * 
   * @param recordId
   *          The record id to store
   * @param remainingRecordIds
   *          The remaining record IDs to store
   */
  public RecordIdToIteratorContainer(final int recordId,
      final Iterator<Integer> remainingRecordIds) {
    mRecordId = recordId;
    mRemainingRecordIds = remainingRecordIds;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
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
