package de.zabuza.lexisearch.ranking;

import java.util.Collections;
import java.util.List;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;

/**
 * Generic implementation of {@link IRankingProvider} which ranks postings first
 * by their score, given with {@link Posting#getScore()}, in ascending order and
 * second by the score of their records, if present, in descending order which
 * are given by {@link Posting#getId()} and
 * {@link IKeyRecordSet#getKeyRecordById(int)}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          The type of the key
 */
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

  /**
   * Creates a new posting before record ranking object. Use
   * {@link #takeSnapshot(IInvertedIndex, IKeyRecordSet)} as initialization and
   * then get rankings by {@link #getRankingScore(Object, Posting)} or
   * {@link #setRankingScoreToIndex()}.
   */
  public PostingBeforeRecordRanking() {
    this.mScoreComparator = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#getInvertedIndex()
   */
  @Override
  public IInvertedIndex<K> getInvertedIndex() {
    return this.mInvertedIndex;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#getKeyRecords()
   */
  @Override
  public IKeyRecordSet<IKeyRecord<K>, K> getKeyRecords() {
    return this.mKeyRecords;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.ranking.IRankingProvider#getRankingScore(java.lang.
   * Object, de.zabuza.lexisearch.indexing.Posting)
   */
  @Override
  public double getRankingScore(final K key, final Posting posting) {
    return posting.getScore();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#setRankingScoreToIndex()
   */
  @Override
  public void setRankingScoreToIndex() {
    // Do nothing as the score already is set
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.ranking.IRankingProvider#sortPostingsByRank(java.util.
   * List)
   */
  @Override
  public void sortPostingsByRank(final List<Posting> postings) {
    Collections.sort(postings, this.mScoreComparator);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#takeSnapshot(de.zabuza.
   * lexisearch.indexing.IInvertedIndex,
   * de.zabuza.lexisearch.indexing.IKeyRecordSet)
   */
  @Override
  public void takeSnapshot(IInvertedIndex<K> invertedIndex,
      IKeyRecordSet<IKeyRecord<K>, K> keyRecords) {
    this.mInvertedIndex = invertedIndex;
    this.mKeyRecords = keyRecords;
    if (this.mScoreComparator == null) {
      this.mScoreComparator =
          new PostingBeforeRecordComparator<>(this.mKeyRecords);
    } else {
      this.mScoreComparator.setKeyRecords(this.mKeyRecords);
    }
  }
}
