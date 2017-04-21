package de.zabuza.lexisearch.indexing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of {@link IInvertedList} which holds its records sorted at all
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
    this.mPostings = postings;
    this.mIdToPosting = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#addRecord(int)
   */
  @Override
  public boolean addPosting(final int recordId) {
    final Integer recordIdAsInteger = Integer.valueOf(recordId);
    final boolean isContained =
        this.mIdToPosting.containsKey(recordIdAsInteger);
    if (!isContained) {
      final Posting posting = new Posting(recordId);
      this.mPostings.add(posting);
      this.mIdToPosting.put(recordIdAsInteger, posting);
    } else {
      // Increase the term frequency as this element is already contained
      final Posting posting = this.mIdToPosting.get(recordIdAsInteger);
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
    final Integer recordIdAsInteger = Integer.valueOf(recordId);
    final boolean isContained =
        this.mIdToPosting.containsKey(recordIdAsInteger);
    if (!isContained) {
      final Posting posting = new Posting(recordId, termFrequency);
      this.mPostings.add(posting);
      this.mIdToPosting.put(recordIdAsInteger, posting);
    } else {
      // Increase the term frequency as this element is already contained
      final Posting posting = this.mIdToPosting.get(recordIdAsInteger);
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
    final Integer recordIdAsInteger = Integer.valueOf(recordId);
    final boolean isContained =
        this.mIdToPosting.containsKey(recordIdAsInteger);
    if (!isContained) {
      final Posting posting = new Posting(recordId, termFrequency, score);
      this.mPostings.add(posting);
      this.mIdToPosting.put(recordIdAsInteger, posting);
    } else {
      // Increase the term frequency as this element is already contained
      final Posting posting = this.mIdToPosting.get(recordIdAsInteger);
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
    return this.mIdToPosting.containsKey(Integer.valueOf(recordId));
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.AInvertedList#getRecords()
   */
  @Override
  public Iterable<Posting> getPostings() {
    return Collections.unmodifiableSet(this.mPostings);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#getSize()
   */
  @Override
  public int getSize() {
    return this.mPostings.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IInvertedList#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return this.mPostings.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.mPostings.toString();
  }

}
