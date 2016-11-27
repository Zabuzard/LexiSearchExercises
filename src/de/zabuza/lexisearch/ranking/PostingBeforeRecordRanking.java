package de.zabuza.lexisearch.ranking;

import java.util.Collections;
import java.util.List;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;

public final class PostingBeforeRecordRanking<K>
    implements IRankingProvider<K> {

  /**
   * The current inverted index to use.
   */
  private IInvertedIndex<K> mInvertedIndex;
  /**
   * The current set of key records to use.
   */
  private IKeyRecordSet<IKeyRecord<K>, K> mKeyRecords;
  /**
   * The comparator to use which sorts postings first by their ranking score in
   * ascending order and second by the score of their records, if present, in
   * descending order.
   */
  private PostingBeforeRecordComparator<IKeyRecord<K>, K> mScoreComparator;

  public PostingBeforeRecordRanking() {
    mScoreComparator = null;
  }

  @Override
  public IInvertedIndex<K> getInvertedIndex() {
    return mInvertedIndex;
  }

  @Override
  public IKeyRecordSet<IKeyRecord<K>, K> getKeyRecords() {
    return mKeyRecords;
  }

  @Override
  public double getRankingScore(final K key, final Posting posting) {
    return posting.getScore();
  }

  @Override
  public void setRankingScoreToIndex() {
    // Do nothing as the score already is set
  }

  @Override
  public void sortPostingsByRank(final List<Posting> postings) {
    Collections.sort(postings, mScoreComparator);
  }

  @Override
  public void takeSnapshot(IInvertedIndex<K> invertedIndex,
      IKeyRecordSet<IKeyRecord<K>, K> keyRecords) {
    mInvertedIndex = invertedIndex;
    mKeyRecords = keyRecords;
    if (mScoreComparator == null) {
      mScoreComparator = new PostingBeforeRecordComparator<>(mKeyRecords);
    } else {
      mScoreComparator.setKeyRecords(mKeyRecords);
    }
  }
}
