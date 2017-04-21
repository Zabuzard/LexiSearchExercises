package de.zabuza.lexisearch.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.zabuza.lexisearch.editdistance.IEditDistance;
import de.zabuza.lexisearch.editdistance.PrefixLevenshtein;
import de.zabuza.lexisearch.indexing.EAggregateMode;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IInvertedList;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;
import de.zabuza.lexisearch.indexing.InvertedList;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.ICity;
import de.zabuza.lexisearch.ranking.IRankingProvider;

/**
 * Generic implementation of {@link IQuery} which operates on
 * {@link IKeyRecord}<tt>{@literal <String>}</tt>. Thus it searches in a list
 * of, for example, {@link ICity} objects by using keywords. The search is a
 * prefix fuzzy search. Thus it searches for words where the keywords are
 * prefixes, also typing errors in the keywords are allowed.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <T>
 *          The class of objects to operate on which must extend
 *          {@link IKeyRecord}<tt>{@literal <String>}</tt>
 */
public final class FuzzyPrefixQuery<T extends IKeyRecord<String>>
    implements IQuery<String> {

  /**
   * Amount of how often prefix edit distances for the current query were
   * computed. This is used for debugging purpose.
   */
  private int mDebugPEDComputationAmount;
  /**
   * Object to use for computing the edit distance.
   */
  private final IEditDistance<String> mEditDistance;
  /**
   * The inverted index representing the processed data to operate on.
   */
  private final IInvertedIndex<String> mInvertedIndex;
  /**
   * The q-Gram provider to use.
   */
  private final QGramProvider mProvider;
  /**
   * If present, used to sort query results by ranking score.
   */
  private final Optional<IRankingProvider<String>> mRankingProvider;
  /**
   * The set of word records to use.
   */
  private final IKeyRecordSet<T, String> mWordRecords;

  /**
   * Creates a new fuzzy prefix query object. After initialization it is able to
   * perform queries by using the given methods.
   * 
   * @param wordRecords
   *          The set of records to operate on
   * @param provider
   *          The q-gram provider to use for the records
   */
  public FuzzyPrefixQuery(final IKeyRecordSet<T, String> wordRecords,
      final QGramProvider provider) {
    this(wordRecords, provider, Optional.empty());
  }

  /**
   * Creates a new fuzzy prefix query object with the given ranking provider.
   * After initialization it is able to perform queries by using the given
   * methods.
   * 
   * @param wordRecords
   *          The set of records to operate on
   * @param provider
   *          The q-gram provider to use for the records
   * @param rankingProvider
   *          The ranking provider to use
   */
  public FuzzyPrefixQuery(final IKeyRecordSet<T, String> wordRecords,
      final QGramProvider provider,
      final IRankingProvider<String> rankingProvider) {
    this(wordRecords, provider, Optional.of(rankingProvider));
  }

  /**
   * Creates a new fuzzy prefix query object with the given ranking provider, if
   * present. After initialization it is able to perform queries by using the
   * given methods.
   * 
   * @param wordRecords
   *          The set of records to operate on
   * @param provider
   *          The q-gram provider to use for the records
   * @param rankingProvider
   *          The ranking provider to use, if present
   */
  @SuppressWarnings("unchecked")
  private FuzzyPrefixQuery(final IKeyRecordSet<T, String> wordRecords,
      final QGramProvider provider,
      final Optional<IRankingProvider<String>> rankingProvider) {
    this.mInvertedIndex = InvertedIndexUtil.createFromWords(wordRecords);
    this.mProvider = provider;
    this.mWordRecords = wordRecords;
    this.mEditDistance = new PrefixLevenshtein();
    this.mDebugPEDComputationAmount = 0;

    this.mRankingProvider = rankingProvider;
    if (this.mRankingProvider.isPresent()) {
      final IRankingProvider<String> ranking = this.mRankingProvider.get();
      ranking.takeSnapshot(this.mInvertedIndex,
          (IKeyRecordSet<IKeyRecord<String>, String>) wordRecords);
      ranking.setRankingScoreToIndex();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.IQuery#searchAnd(java.lang.Iterable)
   */
  @Override
  public List<Posting> searchAnd(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.INTERSECT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.IQuery#searchOr(java.lang.Iterable)
   */
  @Override
  public List<Posting> searchOr(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.UNION);
  }

  /**
   * Estimates the prefix distance between the two given objects. If the amount
   * of q-grams both objects have in common is lower a special bound, the
   * computation can be estimated as it is already greater than the given delta.
   * In this case the method returns <tt>(delta + 1)</tt> as value.
   * 
   * @param first
   *          The first object
   * @param second
   *          The second object
   * @param qGramsInCommon
   *          The amount of q-grams both objects have in common
   * @param delta
   *          The delta bound. If it is already known that the result will be
   *          greater than this, the computation can be simplified
   * @return If the amount of q-grams both objects have in common is greater
   *         than the special bound, the prefix edit distance gets returned.
   *         Else <tt>(delta + 1)</tt> will be returned.
   */
  private int estimatedPrefixDistance(final String first, final String second,
      final int qGramsInCommon, final int delta) {
    final int q = this.mProvider.getQParameter();

    // Estimate the distance by using delta and the q-Grams both have in common
    final double bound = first.length() - 1 - q * delta;
    if (qGramsInCommon >= bound) {
      // Compute the exact prefix edit distance, it may be less than delta
      this.mDebugPEDComputationAmount++;
      return this.mEditDistance.estimatedDistance(first, second, delta);
    }
    // We already know that the distance must be greater than delta
    return delta + 1;
  }

  /**
   * Searches by combining each given keyword with an logical operator depending
   * on the given {@link EAggregateMode}.
   * 
   * @param keys
   *          The keywords to search for
   * @param mode
   *          The aggregation mode to use
   * @return An {@link IInvertedList} containing all records where the keywords
   *         occur depending on the given {@link EAggregateMode}.
   */
  private List<Posting> searchAggregate(final Iterable<String> keys,
      final EAggregateMode mode) {
    this.mDebugPEDComputationAmount = 0;
    final LinkedList<IInvertedList> recordsForKeys = new LinkedList<>();
    // Fetch all corresponding inverted indices
    for (final String key : keys) {
      recordsForKeys.add(searchPrefixesFuzzy(key));
    }

    // Care for the aggregation mode
    final IInvertedList resultingInvertedList;
    final int amountOfEntries = recordsForKeys.size();
    if (amountOfEntries == 0) {
      return Collections.emptyList();
    } else if (amountOfEntries == 1) {
      resultingInvertedList = recordsForKeys.getFirst();
    } else {
      if (mode == EAggregateMode.INTERSECT) {
        resultingInvertedList = IInvertedList.intersect(recordsForKeys);
      } else if (mode == EAggregateMode.UNION) {
        resultingInvertedList = IInvertedList.union(recordsForKeys);
      } else {
        throw new AssertionError();
      }
    }

    // Transform the result into a list
    final ArrayList<Posting> resultingList =
        new ArrayList<>(resultingInvertedList.getSize());
    for (final Posting posting : resultingInvertedList.getPostings()) {
      resultingList.add(posting);
    }

    // Use ranking if present
    if (this.mRankingProvider.isPresent()) {
      this.mRankingProvider.get().sortPostingsByRank(resultingList);
    }

    return resultingList;
  }

  /**
   * Searches all postings which have a prefix edit distance lower than a
   * special bound delta.
   * 
   * @param keyRecord
   *          Key to search for
   * @return An inverted list containing all postings which have a prefix edit
   *         distance lower than a special bound delta
   */
  private IInvertedList searchPrefixesFuzzy(final String keyRecord) {
    final String normalizedKeyRecord = QGramProvider.normalizeRecord(keyRecord);
    // Define the bound delta
    final int delta =
        (int) Math.floor((normalizedKeyRecord.length() + 0.0) / 4);

    // Fetch inverted lists of keys q-grams
    final String[] qGrams = this.mProvider.getKeys(keyRecord);
    final LinkedList<IInvertedList> qGramRecords = new LinkedList<>();
    for (final String qGram : qGrams) {
      final IInvertedList records = this.mInvertedIndex.getRecords(qGram);
      if (records != null) {
        qGramRecords.add(records);
      }
    }

    // Merge records and filter out every record with a prefix edit distance
    // greater than delta
    final IInvertedList mergedRecords;
    if (qGramRecords.size() < 1) {
      mergedRecords = new InvertedList();
    } else if (qGramRecords.size() == 1) {
      mergedRecords = qGramRecords.getFirst();
    } else {
      mergedRecords = IInvertedList.union(qGramRecords);
    }
    final IInvertedList resultingList = new InvertedList();
    for (final Posting posting : mergedRecords.getPostings()) {
      final int recordId = posting.getId();
      final int termFrequency = posting.getTermFrequency();
      final String record =
          this.mWordRecords.getKeyRecordById(recordId).getName();
      final String normalizedRecord = QGramProvider.normalizeRecord(record);

      // Estimate the distance, if it is greater than delta, discard the record
      final int estimatedDistance = estimatedPrefixDistance(normalizedKeyRecord,
          normalizedRecord, termFrequency, delta);
      if (estimatedDistance <= delta) {
        // Take the record, store the distance in its relevance-score field
        resultingList.addPosting(recordId, termFrequency, estimatedDistance);
      }
    }

    return resultingList;
  }
}
