package de.zabuza.lexisearch.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.zabuza.lexisearch.indexing.EAggregateMode;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IInvertedList;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.ranking.IRankingProvider;

/**
 * Generic implementation of {@link IQuery} which operates on
 * {@link IWordRecord}s. Thus it searches in a list of, for example,
 * {@link IDocument}s by using keywords.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <T>
 *          The class of documents to operate on which must extend
 *          {@link IWordRecord}
 */
public final class KeywordQuery<T extends IKeyRecord<String>>
    implements IQuery<String> {
  /**
   * The inverted index representing the processed data to operate on.
   */
  private final IInvertedIndex<String> mInvertedIndex;
  /**
   * If present, used to sort query results by ranking score.
   */
  private final Optional<IRankingProvider<String>> mRankingProvider;

  /**
   * Creates a new keyword query object. After initialization it is able to
   * perform keyword queries by using the given methods.
   * 
   * @param wordRecords
   *          The set of records to operate on
   */
  public KeywordQuery(final IKeyRecordSet<T, String> wordRecords) {
    this(wordRecords, Optional.empty());
  }

  public KeywordQuery(final IKeyRecordSet<T, String> wordRecords,
      final IRankingProvider<String> rankingProvider) {
    this(wordRecords, Optional.of(rankingProvider));
  }

  @SuppressWarnings("unchecked")
  private KeywordQuery(final IKeyRecordSet<T, String> wordRecords,
      final Optional<IRankingProvider<String>> rankingProvider) {
    mInvertedIndex = InvertedIndexUtil.createFromWords(wordRecords);
    mRankingProvider = rankingProvider;

    if (mRankingProvider.isPresent()) {
      final IRankingProvider<String> ranking = mRankingProvider.get();
      ranking.takeSnapshot(mInvertedIndex,
          (IKeyRecordSet<IKeyRecord<String>, String>) wordRecords);
      ranking.setRankingScoreToIndex();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.IQuery#searchAnd(java.lang.Iterable,
   * java.util.Optional)
   */
  @Override
  public List<Posting> searchAnd(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.INTERSECT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.IQuery#searchOr(java.lang.Iterable,
   * java.util.Optional)
   */
  @Override
  public List<Posting> searchOr(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.UNION);
  }

  /**
   * Searches by combining each given keyword with an logical operator depending
   * on the given {@link EAggregateMode}.
   * 
   * @param keys
   *          The keywords to search for
   * @param mode
   *          The aggregation mode to use
   * @return An {@link AInvertedList} containing all records where the keywords
   *         occur depending on the given {@link EAggregateMode}.
   */
  private List<Posting> searchAggregate(final Iterable<String> keys,
      final EAggregateMode mode) {
    LinkedList<IInvertedList> recordsForKeys = new LinkedList<>();

    // Fetch all corresponding inverted indices
    for (final String key : keys) {
      if (!mInvertedIndex.containsKey(key)) {
        if (mode == EAggregateMode.INTERSECT) {
          // If key is not contained, return an empty list
          return Collections.emptyList();
        }
      } else {
        recordsForKeys.add(mInvertedIndex.getRecords(key));
      }
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
    ArrayList<Posting> resultingList =
        new ArrayList<Posting>(resultingInvertedList.getSize());
    for (final Posting posting : resultingInvertedList.getPostings()) {
      resultingList.add(posting);
    }

    // Use ranking if present
    if (mRankingProvider.isPresent()) {
      mRankingProvider.get().sortPostingsByRank(resultingList);
    }

    return resultingList;
  }

}
