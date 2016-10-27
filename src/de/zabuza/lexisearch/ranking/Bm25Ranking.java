package de.zabuza.lexisearch.ranking;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.util.MathUtil;

public final class Bm25Ranking<K> implements IRankingProvider<K> {

  private static final double DEFAULT_B_PARAMETER = 0.75;
  private static final double DEFAULT_K_PARAMETER = 1.75;

  private int mAmountOfKeyRecords;
  private double mBParameter;
  private final IInvertedIndex<K> mInvertedIndex;
  private final IKeyRecordSet<IKeyRecord<K>, K> mKeyRecords;
  private final HashMap<IKeyRecord<K>, Integer> mKeyRecordToSize;
  private final HashMap<K, Integer> mKeyToKeyRecordFrequency;
  private double mKParameter;
  private int mTotalSizeOfAllKeyRecords;

  public Bm25Ranking(final IInvertedIndex<K> invertedIndex,
      final IKeyRecordSet<IKeyRecord<K>, K> keyRecords) {
    mInvertedIndex = invertedIndex;
    mKeyRecords = keyRecords;
    mKeyRecordToSize = new HashMap<>();
    mKeyToKeyRecordFrequency = new HashMap<>();
    mKParameter = DEFAULT_K_PARAMETER;
    mBParameter = DEFAULT_B_PARAMETER;

    takeSnapshot();
  }

  /**
   * Gets the b parameter.
   * 
   * @return The b parameter to get
   */
  public double getbParameter() {
    return mBParameter;
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
  public void setbParameter(final double bParameter) {
    mBParameter = bParameter;
  }

  /**
   * Sets the k parameter for this algorithm.
   * 
   * @param kParameter
   *          The k parameter to set
   */
  public void setkParameter(final double kParameter) {
    mKParameter = kParameter;
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
    // TODO Where to get key?
    Collections.sort(postings, new Comparator<Posting>() {
      @Override
      public int compare(final Posting o1, final Posting o2) {
        return 0;
      }
    });
  }

  public void takeSnapshot() {
    mKeyRecordToSize.clear();
    mKeyToKeyRecordFrequency.clear();

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
