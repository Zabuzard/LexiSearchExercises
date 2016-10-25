package de.zabuza.lexisearch.queries;

import java.util.LinkedList;

import de.zabuza.lexisearch.document.IDocument;
import de.zabuza.lexisearch.indexing.AggregateMode;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IInvertedList;
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
    mInvertedIndex = InvertedIndexUtil.createFromWordRecords(wordRecords);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.Query#search(java.lang.Iterable)
   */
  @Override
  public IInvertedList searchAnd(final Iterable<String> keys) {
    return searchAggregate(keys, AggregateMode.INTERSECT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.queries.Query#searchOr(java.lang.Iterable)
   */
  @Override
  public IInvertedList searchOr(final Iterable<String> keys) {
    return searchAggregate(keys, AggregateMode.UNION);
  }

  /**
   * Searches by combining each given keyword with an logical operator depending
   * on the given {@link AggregateMode}.
   * 
   * @param keys
   *          The keywords to search for
   * @param mode
   *          The aggregation mode to use
   * @return An {@link IInvertedList} containing all records where the keywords
   *         occur depending on the given {@link AggregateMode}.
   */
  private IInvertedList searchAggregate(final Iterable<String> keys,
    final AggregateMode mode) {
    IInvertedList resultingList = new InvertedList();
    LinkedList<IInvertedList> recordsForKeys = new LinkedList<>();

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

    if (mode == AggregateMode.INTERSECT) {
      return IInvertedList.intersect(recordsForKeys);
    } else if (mode == AggregateMode.UNION) {
      return IInvertedList.union(recordsForKeys);
    } else {
      throw new AssertionError();
    }
  }

}
