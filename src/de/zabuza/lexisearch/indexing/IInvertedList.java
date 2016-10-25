package de.zabuza.lexisearch.indexing;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Interface for inverted lists. Such lists contain records and are used by
 * {@link IInvertedIndex} which maps content to lists of records.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public interface IInvertedList {

  /**
   * Aggregates the given lists by the given aggregation mode.
   * 
   * @param lists
   *          Lists to aggregate
   * @param mode
   *          Aggregation mode to use
   * @return A list containing the aggregated data of the given lists
   */
  public static IInvertedList aggregate(final Collection<IInvertedList> lists,
    final AggregateMode mode) {
    final int amountOfLists = lists.size();
    if (amountOfLists <= 1) {
      throw new IllegalArgumentException();
    }

    // Setup a priority queue containing all initial elements
    PriorityQueue<RecordIdToIteratorContainer> queue = new PriorityQueue<>();
    for (final IInvertedList list : lists) {
      final Iterator<Integer> records = list.getRecords().iterator();
      if (records.hasNext()) {
        final int firstRecord = records.next();
        final RecordIdToIteratorContainer container =
            new RecordIdToIteratorContainer(firstRecord, records);
        queue.add(container);
      }
    }

    final IInvertedList resultingList = new InvertedList();

    // Process all queue elements
    while (!queue.isEmpty()) {
      // Poll the smallest value of all lists
      RecordIdToIteratorContainer smallestContainer = queue.poll();
      final int smallestRecordId = smallestContainer.getRecordId();
      // Put the next value of this list in the queue
      final Iterator<Integer> smallestIter =
          smallestContainer.getRemainingRecordIds();
      if (smallestIter.hasNext()) {
        queue.add(
            new RecordIdToIteratorContainer(smallestIter.next(), smallestIter));
      }

      // Check if all other lists currently also hold this record
      int amountOfListsMatching = 1;
      while (amountOfListsMatching != amountOfLists) {
        RecordIdToIteratorContainer nextSmallestContainer = queue.peek();
        if (nextSmallestContainer == null) {
          // The queue is empty, break
          break;
        }
        final int nextSmallestRecordId = nextSmallestContainer.getRecordId();
        if (nextSmallestRecordId == smallestRecordId) {
          // The list also holds this record, advance it
          amountOfListsMatching++;
          queue.poll();
          final Iterator<Integer> nextSmallestIter =
              nextSmallestContainer.getRemainingRecordIds();
          if (nextSmallestIter.hasNext()) {
            queue.add(new RecordIdToIteratorContainer(nextSmallestIter.next(),
                nextSmallestIter));
          }
        } else {
          // The list does not hold the record, break the loop as
          // the outcome is known
          break;
        }
      }

      if (amountOfListsMatching == amountOfLists) {
        // The record is hold by every list
        resultingList.addRecord(smallestRecordId);
      } else {
        // The record is not hold by every list
        if (mode == AggregateMode.UNION) {
          resultingList.addRecord(smallestRecordId);
        }
      }
    }

    return resultingList;
  }

  /**
   * Intersects the given lists.
   * 
   * @param lists
   *          Lists to intersect
   * @return A list containing the data that all of the given lists have in
   *         common, i.e. the intersection
   */
  public static IInvertedList intersect(final Collection<IInvertedList> lists) {
    return IInvertedList.aggregate(lists, AggregateMode.INTERSECT);
  }

  /**
   * Intersects the given two lists.
   * 
   * @param firstList
   *          First list to intersect
   * @param secondList
   *          Second list to intersect
   * @return A list containing the data that both given lists have in common,
   *         i.e. the intersection
   */
  public static IInvertedList intersect(final IInvertedList firstList,
    final IInvertedList secondList) {
    final LinkedList<IInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return IInvertedList.intersect(operands);
  }

  /**
   * Builds the union of the given lists.
   * 
   * @param lists
   *          Lists to unite
   * @return A list containing the merged data of all the given lists, i.e. the
   *         union
   */
  public static IInvertedList union(final Collection<IInvertedList> lists) {
    return IInvertedList.aggregate(lists, AggregateMode.UNION);
  }

  /**
   * Builds the union of the given two lists.
   * 
   * @param firstList
   *          First list to unite
   * @param secondList
   *          Second list to unite
   * @return A list containing the merged data of both given lists, i.e. the
   *         union
   */
  public static IInvertedList union(final IInvertedList firstList,
    final IInvertedList secondList) {
    final LinkedList<IInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return IInvertedList.union(operands);
  }

  /**
   * Adds a record to the inverted list.
   * 
   * @param recordId
   *          Record to add
   * @return If the record was added, i.e. not already contained
   */
  public boolean addRecord(final int recordId);

  /**
   * Returns whether the inverted list contains the given record or not.
   * 
   * @param recordId
   *          The record in question
   * @return <tt>True</tt> if the record is contained, <tt>false</tt> otherwise
   */
  public boolean containsRecord(final int recordId);

  /**
   * Gets all records of this inverted list. The order of how the records are
   * returned is ascending at all time.
   * 
   * @return All records of this inverted list in ascending order
   */
  public Iterable<Integer> getRecords();

  /**
   * Gets the size of this inverted list, i.e. the amount of records it holds.
   * 
   * @return The size of this inverted list, i.e. the amount of records it holds
   */
  public int getSize();

  /**
   * Returns whether this inverted list is empty, i.e. the amount of records it
   * holds is zero.
   * 
   * @return <tt>True</tt> if the list is empty, <tt>false</tt> otherwise
   */
  public boolean isEmpty();
}
