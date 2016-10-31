package de.zabuza.lexisearch.ranking;

import java.util.List;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;

/**
 * Provides ranking scores for key-Posting pairs in {@link IInvertedIndex}
 * objects.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public interface IRankingProvider<K> {
  /**
   * Gets the current inverted index this object provides rankings for.
   * 
   * @return The current inverted index this object provides rankings for
   */
  IInvertedIndex<K> getInvertedIndex();

  /**
   * Gets the current key record set this object provides rankings for.
   * 
   * @return The current key record set this object provides rankings for
   */
  IKeyRecordSet<IKeyRecord<K>, K> getKeyRecords();

  /**
   * Computes and gets the ranking score of the given key-Posting pair according
   * to the implementing ranking algorithm.
   * 
   * @param key
   *          The key of the pair which must be contained in the current objects
   *          given by {@link #getKeyRecords()} and {@link #getInvertedIndex()}
   * @param posting
   *          The posting of the pair which must be contained in the current
   *          objects given by {@link #getKeyRecords()} and
   *          {@link #getInvertedIndex()}
   * @return The ranking score of the given key-Posting pair according to the
   *         implementing ranking algorithm
   */
  double getRankingScore(final K key, final Posting posting);

  /**
   * Computes and sets the ranking score to each key-Posting pair in the current
   * inverted index, given by {@link #getInvertedIndex()}, according to the
   * implementing ranking algorithm. The score is set by using
   * {@link Posting#setScore(double)}.
   */
  void setRankingScoreToIndex();

  /**
   * Sorts the given list of postings by their ranking score given by
   * {@link Posting#getScore()} in descending order. The sorting is directly
   * done in the given list.
   * 
   * @param postings
   *          The list of postings to sort
   */
  void sortPostingsByRank(final List<Posting> postings);

  /**
   * Takes a snapshot of the current given objects and initializes ranking
   * computation for the current state of those objects. Also sets the values
   * for {@link #getInvertedIndex()} and {@link #getKeyRecords()}. This method
   * should be called prior to {@link #getRankingScore(Object, Posting)}
   * whenever the underlying structures have changed.
   * 
   * @param invertedIndex
   *          The inverted index to take a snapshot of
   * @param keyRecords
   *          The set of key records to take a snapshot of
   */
  void takeSnapshot(final IInvertedIndex<K> invertedIndex,
    final IKeyRecordSet<IKeyRecord<K>, K> keyRecords);
}
