package de.zabuza.lexisearch.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

public final class FuzzyPrefixQuery<T extends IKeyRecord<String>>
    implements IQuery<String> {

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
   * The set of word records to use.
   */
  private final IKeyRecordSet<T, String> mWordRecords;

  public FuzzyPrefixQuery(final IKeyRecordSet<T, String> wordRecords,
      final QGramProvider provider) {
    mInvertedIndex = InvertedIndexUtil.createFromWords(wordRecords);
    mProvider = provider;
    mWordRecords = wordRecords;
    mEditDistance = new PrefixLevenshtein();
  }

  @Override
  public List<Posting> searchAnd(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.INTERSECT);
  }

  @Override
  public List<Posting> searchOr(final Iterable<String> keys) {
    return searchAggregate(keys, EAggregateMode.UNION);
  }

  private int estimatedPrefixDistance(final String first, final String second,
      final int qGramsInCommon, final int delta) {
    final int q = mProvider.getQParameter();

    // Estimate the distance by using delta and the q-Grams both have in common
    final double bound =
        Math.max(first.length(), second.length()) - 1 - (delta - 1) * q;
    if (qGramsInCommon >= bound) {
      // Compute the exact prefix edit distance, it may be less than delta
      return mEditDistance.estimatedDistance(first, second, delta);
    } else {
      // We already know that the distance must be greater than delta
      return delta + 1;
    }
  }

  private List<Posting> searchAggregate(final Iterable<String> keys,
      final EAggregateMode mode) {

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
    ArrayList<Posting> resultingList =
        new ArrayList<Posting>(resultingInvertedList.getSize());
    for (final Posting posting : resultingInvertedList.getPostings()) {
      resultingList.add(posting);
    }

    return resultingList;
  }

  private IInvertedList searchPrefixesFuzzy(final String keyRecord) {
    final String normalizedKeyRecord = QGramProvider.normalizeRecord(keyRecord);
    final int delta =
        (int) Math.floor((normalizedKeyRecord.length() + 0.0) / 4);

    // Fetch inverted lists of keys q-grams
    final String[] qGrams = mProvider.getKeys(keyRecord);
    final LinkedList<IInvertedList> qGramRecords = new LinkedList<>();
    for (final String qGram : qGrams) {
      qGramRecords.add(mInvertedIndex.getRecords(qGram));
    }

    // Merge records and filter out every record with a prefix edit distance
    // greater than delta
    final IInvertedList mergedRecords = IInvertedList.union(qGramRecords);
    final IInvertedList resultingList = new InvertedList();
    for (final Posting posting : mergedRecords.getPostings()) {
      final int recordId = posting.getId();
      final int termFrequency = posting.getTermFrequency();
      final String record = mWordRecords.getKeyRecordById(recordId).getName();
      final String normalizedRecord = QGramProvider.normalizeRecord(record);

      // Estimate the distance, if it is greater than delta, discard the record
      final int estimatedDistance = estimatedPrefixDistance(normalizedKeyRecord,
          normalizedRecord, termFrequency, delta);
      if (estimatedDistance <= delta) {
        resultingList.addPosting(recordId, termFrequency, posting.getScore());
      }
    }

    return resultingList;
  }
}
