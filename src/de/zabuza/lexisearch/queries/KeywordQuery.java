package de.zabuza.lexisearch.queries;

import java.util.LinkedList;

import de.zabuza.lexisearch.indexing.AggregateMode;
import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IInvertedList;
import de.zabuza.lexisearch.indexing.IWordRecord;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;
import de.zabuza.lexisearch.indexing.InvertedList;

public final class KeywordQuery<T extends IWordRecord>
    implements Query<String> {
  private final IInvertedIndex<String> mInvertedIndex;

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
