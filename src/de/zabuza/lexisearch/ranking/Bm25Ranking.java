package de.zabuza.lexisearch.ranking;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.zabuza.lexisearch.indexing.AInvertedList;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.util.MathUtil;

/**
 * Ranking algorithm which implements the BM25 ranking algorithm.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public final class Bm25Ranking<K> implements IRankingProvider<K> {
  /**
   * The default value for the b parameter of the BM25 algorithm to use.
   */
  public static final double DEFAULT_B_PARAMETER = 0.75;
  /**
   * The default value for the k parameter of the BM25 algorithm to use.
   */
  public static final double DEFAULT_K_PARAMETER = 1.75;

  /**
   * The amount of key records in total.
   */
  private int mAmountOfKeyRecords;
  /**
   * The b parameter of the BM25 algorithm.
   */
  private double mBParameter;
  /**
   * The current inverted index to use.
   */
  private IInvertedIndex<K> mInvertedIndex;
  /**
   * The current set of key records to use.
   */
  private IKeyRecordSet<IKeyRecord<K>, K> mKeyRecords;
  /**
   * Map which allows a fast access to the size of key records.
   */
  private final HashMap<IKeyRecord<K>, Integer> mKeyRecordToSize;
  /**
   * Map which allows a fast access to the key record frequency of keys.
   */
  private final HashMap<K, Integer> mKeyToKeyRecordFrequency;
  /**
   * The k parameter of the BM25 algorithm.
   */
  private double mKParameter;
  /**
   * The comparator to use which sorts postings by their ranking score in
   * descending order.
   */
  private final Comparator<Posting> mScoreComparator;
  /**
   * The total size of all key records.
   */
  private int mTotalSizeOfAllKeyRecords;

  /**
   * Creates a new BM25 ranking with default parameters. Use
   * {@link #takeSnapshot(IInvertedIndex, IKeyRecordSet)} as initialization and
   * then get rankings by {@link #getRankingScore(Object, Posting)} or
   * {@link #setRankingScoreToIndex()}.
   */
  public Bm25Ranking() {
    this(DEFAULT_K_PARAMETER, DEFAULT_B_PARAMETER);
  }

  /**
   * Creates a new BM25 ranking with given parameters. Use
   * {@link #takeSnapshot(IInvertedIndex, IKeyRecordSet)} as initialization and
   * then get rankings by {@link #getRankingScore(Object, Posting)} or
   * {@link #setRankingScoreToIndex()}.
   * 
   * @param kParameter
   *          The k parameter of the BM25 algorithm to use
   * @param bParameter
   *          The b parameter of the BM25 algorithm to use
   */
  public Bm25Ranking(final double kParameter, final double bParameter) {
    mKeyRecordToSize = new HashMap<>();
    mKeyToKeyRecordFrequency = new HashMap<>();
    mKParameter = kParameter;
    mBParameter = bParameter;
    mScoreComparator = new ScoreComparator().reversed();
  }

  /**
   * Gets the b parameter.
   * 
   * @return The b parameter to get
   */
  public double getBParameter() {
    return mBParameter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#getInvertedIndex()
   */
  @Override
  public IInvertedIndex<K> getInvertedIndex() {
    return mInvertedIndex;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#getKeyRecords()
   */
  @Override
  public IKeyRecordSet<IKeyRecord<K>, K> getKeyRecords() {
    return mKeyRecords;
  }

  /**
   * Gets the k parameter.
   * 
   * @return The k parameter to get
   */
  public double getKParameter() {
    return mKParameter;
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
    final int n = mAmountOfKeyRecords;
    final int df = mKeyToKeyRecordFrequency.get(key);
    final double idf = MathUtil.log2((n + 0.0) / df);

    final double tf = posting.getTermFrequency() + 0.0;
    final int dl =
        mKeyRecordToSize.get(mKeyRecords.getKeyRecordById(posting.getId()));
    final double avdl = (mTotalSizeOfAllKeyRecords + 0.0) / mAmountOfKeyRecords;

    final double k = mKParameter;
    final double b = mBParameter;

    final double tfModified =
        tf * (k + 1) / (k * (1 - b + b * (dl / avdl)) + tf);

    return tfModified * idf;
  }

  /**
   * Sets the b parameter for this algorithm.
   * 
   * @param bParameter
   *          The b parameter to set
   */
  public void setBParameter(final double bParameter) {
    mBParameter = bParameter;
  }

  /**
   * Sets the k parameter for this algorithm.
   * 
   * @param kParameter
   *          The k parameter to set
   */
  public void setKParameter(final double kParameter) {
    mKParameter = kParameter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#setRankingScoreToIndex()
   */
  @Override
  public void setRankingScoreToIndex() {
    // Iterate over the inverted index and set all ranking scores
    for (final K key : mInvertedIndex.getKeys()) {
      final AInvertedList invertedList = mInvertedIndex.getRecords(key);
      for (final Posting posting : invertedList.getPostings()) {
        posting.setScore(getRankingScore(key, posting));
      }
    }
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
    Collections.sort(postings, mScoreComparator);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.ranking.IRankingProvider#takeSnapshot(de.zabuza.
   * lexisearch.indexing.IInvertedIndex,
   * de.zabuza.lexisearch.indexing.IKeyRecordSet)
   */
  @Override
  public void takeSnapshot(final IInvertedIndex<K> invertedIndex,
    final IKeyRecordSet<IKeyRecord<K>, K> keyRecords) {
    mKeyRecordToSize.clear();
    mKeyToKeyRecordFrequency.clear();
    mInvertedIndex = invertedIndex;
    mKeyRecords = keyRecords;

    // Iterate over the key records and compute meta data
    int amountOfKeyRecords = 0;
    int totalSize = 0;
    for (final IKeyRecord<K> keyRecord : mKeyRecords) {
      amountOfKeyRecords++;
      final int size = keyRecord.getSize();
      mKeyRecordToSize.put(keyRecord, size);
      totalSize += size;
    }
    mAmountOfKeyRecords = amountOfKeyRecords;
    mTotalSizeOfAllKeyRecords = totalSize;

    // Iterate over the inverted index and compute meta data
    for (final K key : mInvertedIndex.getKeys()) {
      final int keyRecordFrequency = mInvertedIndex.getRecords(key).getSize();
      mKeyToKeyRecordFrequency.put(key, keyRecordFrequency);
    }
  }

}
