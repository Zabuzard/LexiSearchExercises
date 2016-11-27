package de.zabuza.lexisearch.ranking;

import java.util.Comparator;

import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;

public final class PostingBeforeRecordComparator<E extends IKeyRecord<K>, K>
    implements Comparator<Posting> {

  private IKeyRecordSet<E, K> mKeyRecords;

  public PostingBeforeRecordComparator(final IKeyRecordSet<E, K> keyRecords) {
    mKeyRecords = keyRecords;
  }

  @Override
  public int compare(final Posting o1, final Posting o2) {
    // First sort by the posting score ascending
    int firstRelation = Double.compare(o1.getScore(), o2.getScore());
    if (firstRelation != 0) {
      // The posting score is already different
      return firstRelation;
    } else {
      // Compare by the record score descending, if present
      final E firstRecord = mKeyRecords.getKeyRecordById(o1.getId());
      final E secondRecord = mKeyRecords.getKeyRecordById(o2.getId());
      if (firstRecord instanceof IRecordScoreProvider
          && secondRecord instanceof IRecordScoreProvider) {
        final IRecordScoreProvider firstProvider =
            (IRecordScoreProvider) firstRecord;
        final IRecordScoreProvider secondProvider =
            (IRecordScoreProvider) secondRecord;
        // The order needs to be descending so we compare reversed
        return Integer.compare(secondProvider.getScore(),
            firstProvider.getScore());
      } else {
        // Record score is not present
        return firstRelation;
      }
    }
  }

  public void setKeyRecords(final IKeyRecordSet<E, K> keyRecords) {
    mKeyRecords = keyRecords;
  }

}
