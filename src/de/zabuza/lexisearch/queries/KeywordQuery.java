package de.zabuza.lexisearch.queries;

import java.util.LinkedList;

import de.zabuza.lexisearch.indexing.EAggregateMode;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.AInvertedList;
import de.zabuza.lexisearch.indexing.IWordRecord;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;
import de.zabuza.lexisearch.indexing.InvertedList;

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
public final class KeywordQuery<T extends IWordRecord>
    implements IQuery<String> {
  /**
   * The inverted index representing the processed data to operate on.
   */
  private final IInvertedIndex<String> mInvertedIndex;

  /**
   * Creates a new keyword query object. After initialization it is able to
   * perform keyword queries by using the given methods.
   * 
   * @param wordRecords
   *          The set of records to operate on
   */
  public KeywordQuery(final Iterable<T> wordRecords) {
    mInvertedIndex = InvertedIndexUtil.createFromWords(wordRecords);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.Query#search(java.lang.Iterable)
   */
  @Override
  public AInvertedList searchAnd(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.INTERSECT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.Query#searchOr(java.lang.Iterable)
   */
  @Override
  public AInvertedList searchOr(final Iterable<String> keys) {
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
  private AInvertedList searchAggregate(final Iterable<String> keys,
    final EAggregateMode mode) {
    AInvertedList resultingList = new InvertedList();
    LinkedList<AInvertedList> recordsForKeys = new LinkedList<>();

    for (final String key : keys) {
      if (!mInvertedIndex.containsKey(key)) {
        // If key is not contained, return an empty list
        return resultingList;
      }
      recordsForKeys.add(mInvertedIndex.getRecords(key));
    }

    final int amountOfEntries = recordsForKeys.size();
    if (amountOfEntries == 0) {
      return resultingList;
    } else if (amountOfEntries == 1) {
      return recordsForKeys.getFirst();
    }

    if (mode == EAggregateMode.INTERSECT) {
      return AInvertedList.intersect(recordsForKeys);
    } else if (mode == EAggregateMode.UNION) {
      return AInvertedList.union(recordsForKeys);
    } else {
      throw new AssertionError();
    }
  }

}
