package de.zabuza.lexisearch.indexing;

import java.util.Iterator;

/**
 * Container which stores a posting and the remaining postings that belong to a
 * given {@link IInvertedList}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PostToIterCont implements Comparable<PostToIterCont> {

  /**
   * The posting of this container.
   */
  private final Posting mPosting;
  /**
   * The remaining postings of this container.
   */
  private final Iterator<Posting> mRemainingPostings;

  /**
   * Creates a new container which stores a given posting and the remaining
   * postings that belong to a given {@link IInvertedList}.
   * 
   * @param posting
   *          The posting to store
   * @param remainingPostings
   *          The remaining postings to store
   */
  public PostToIterCont(final Posting posting,
      final Iterator<Posting> remainingPostings) {
    this.mPosting = posting;
    this.mRemainingPostings = remainingPostings;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final PostToIterCont o) {
    return Integer.compare(getRecordId(), o.getRecordId());
  }

  /**
   * Gets the posting of this container.
   * 
   * @return The posting of this container
   */
  public Posting getPosting() {
    return this.mPosting;
  }

  /**
   * Gets the record id of this container.
   * 
   * @return The record id of this container
   */
  public int getRecordId() {
    return this.mPosting.getId();
  }

  /**
   * Gets the iterator over the remaining postings of this container.
   * 
   * @return The iterator over the remaining postings of this container
   */
  public Iterator<Posting> getRemainingPostings() {
    return this.mRemainingPostings;
  }

}
