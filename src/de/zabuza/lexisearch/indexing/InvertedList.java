package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of {@link AInvertedList} which holds its records sorted at all
 * time by using a {@link SortedSet}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public class InvertedList extends AInvertedList {
  /**
   * Set containing all posting ids that are contained by this list.
   */
  private final HashSet<Integer> mPostingIds;
  /**
   * Sorted set containing all postings contained by this list.
   */
  private final Set<Posting> mPostings;

  /**
   * Creates a new empty inverted list.
   */
  public InvertedList() {
    this(new TreeSet<>());
  }

  /**
   * Creates a new empty inverted list which stores postings in the given set.
   * 
   * @param postings
   *          The set to store postings in which must be empty
   */
  protected InvertedList(final Set<Posting> postings) {
    mPostings = postings;
    mPostingIds = new HashSet<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#addRecord(int)
   */
  @Override
  public boolean addPosting(final int recordId) {
    final boolean wasAdded = mPostingIds.add(recordId);
    if (wasAdded) {
      mPostings.add(new Posting(recordId));
    }
    return wasAdded;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#addRecord(int, int)
   */
  @Override
  public boolean addPosting(final int recordId, final int termFrequency) {
    final boolean wasAdded = mPostingIds.add(recordId);
    if (wasAdded) {
      mPostings.add(new Posting(recordId, termFrequency));
    }
    return wasAdded;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#containsRecord(int)
   */
  @Override
  public boolean containsPosting(final int recordId) {
    return mPostingIds.contains(recordId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#getRecords()
   */
  @Override
  public Iterable<Posting> getPostings() {
    return Collections.unmodifiableSet(mPostings);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#getSize()
   */
  @Override
  public int getSize() {
    return mPostings.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return mPostings.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return mPostings.toString();
  }

}
