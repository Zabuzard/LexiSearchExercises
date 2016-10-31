package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of {@link AInvertedList} which holds its records sorted at all
 * time by using a {@link SortedSet}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public class InvertedList implements IInvertedList {
  /**
   * Map containing all posting ids that are contained by this list which
   * provides fast direct access to its elements by id.
   */
  private final HashMap<Integer, Posting> mIdToPosting;
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
    mIdToPosting = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#addRecord(int)
   */
  @Override
  public boolean addPosting(final int recordId) {
    final boolean isContained = mIdToPosting.containsKey(recordId);
    if (!isContained) {
      final Posting posting = new Posting(recordId);
      mPostings.add(posting);
      mIdToPosting.put(recordId, posting);
    } else {
      // Increase the term frequency as this element is already contained
      final Posting posting = mIdToPosting.get(recordId);
      posting.increaseTermFrequency();
    }
    return !isContained;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#addRecord(int, int)
   */
  @Override
  public boolean addPosting(final int recordId, final int termFrequency) {
    final boolean isContained = mIdToPosting.containsKey(recordId);
    if (!isContained) {
      final Posting posting = new Posting(recordId, termFrequency);
      mPostings.add(posting);
      mIdToPosting.put(recordId, posting);
    } else {
      // Increase the term frequency as this element is already contained
      final Posting posting = mIdToPosting.get(recordId);
      posting.increaseTermFrequency();
    }
    return !isContained;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#addPosting(int, int,
   * double)
   */
  @Override
  public boolean addPosting(final int recordId, final int termFrequency,
      final double score) {
    final boolean isContained = mIdToPosting.containsKey(recordId);
    if (!isContained) {
      final Posting posting = new Posting(recordId, termFrequency, score);
      mPostings.add(posting);
      mIdToPosting.put(recordId, posting);
    } else {
      // Increase the term frequency as this element is already contained
      final Posting posting = mIdToPosting.get(recordId);
      posting.increaseTermFrequency();
    }
    return !isContained;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#containsRecord(int)
   */
  @Override
  public boolean containsPosting(final int recordId) {
    return mIdToPosting.containsKey(recordId);
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
