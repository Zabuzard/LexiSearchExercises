package de.zabuza.lexisearch.ranking;

import java.util.Comparator;

import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;

/**
 * Compares postings first by their score, given with
 * {@link Posting#getScore()}, in ascending order and second by the score of
 * their records, if present, in descending order which are given by
 * {@link Posting#getId()} and {@link IKeyRecordSet#getKeyRecordById(int)}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <E>
 *          The type of records from the postings
 * @param <K>
 *          The type of the keys
 */
public final class PostingBeforeRecordComparator<E extends IKeyRecord<K>, K>
    implements Comparator<Posting> {
  /**
   * The set of key records the records listed by the postings belong to.
   */
  private IKeyRecordSet<E, K> mKeyRecords;

  /**
   * Creates a new posting before record comparator which operates on the given
   * set of records.
   * 
   * @param keyRecords
   *          The set of key records the records listed by the postings belong
   *          to
   */
  public PostingBeforeRecordComparator(final IKeyRecordSet<E, K> keyRecords) {
    this.mKeyRecords = keyRecords;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  @Override
  public int compare(final Posting o1, final Posting o2) {
    // First sort by the posting score ascending
    final int firstRelation = Double.compare(o1.getScore(), o2.getScore());
    if (firstRelation != 0) {
      // The posting score is already different
      return firstRelation;
    }

    // Compare by the record score descending, if present
    final E firstRecord = this.mKeyRecords.getKeyRecordById(o1.getId());
    final E secondRecord = this.mKeyRecords.getKeyRecordById(o2.getId());
    if (firstRecord instanceof IRecordScoreProvider
        && secondRecord instanceof IRecordScoreProvider) {
      final IRecordScoreProvider firstProvider =
          (IRecordScoreProvider) firstRecord;
      final IRecordScoreProvider secondProvider =
          (IRecordScoreProvider) secondRecord;
      // The order needs to be descending so we compare reversed
      return Integer.compare(secondProvider.getScore(),
          firstProvider.getScore());
    }

    // Record score is not present
    return firstRelation;
  }

  /**
   * Sets the set of key records the records listed by the postings belong to.
   * 
   * @param keyRecords
   *          The set of key records to set
   */
  public void setKeyRecords(final IKeyRecordSet<E, K> keyRecords) {
    this.mKeyRecords = keyRecords;
  }

}
