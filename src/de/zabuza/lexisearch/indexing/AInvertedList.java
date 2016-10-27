package de.zabuza.lexisearch.indexing;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Abstract class for inverted lists. Such lists contain records and are used by
 * {@link IInvertedIndex} which maps content to lists of records.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public abstract class AInvertedList {

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
  public static AInvertedList intersect(final AInvertedList firstList,
    final AInvertedList secondList) {
    final LinkedList<AInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return AInvertedList.intersect(operands);
  }

  /**
   * Intersects the given lists.
   * 
   * @param lists
   *          Lists to intersect
   * @return A list containing the data that all of the given lists have in
   *         common, i.e. the intersection
   */
  public static AInvertedList intersect(final Collection<AInvertedList> lists) {
    return AInvertedList.aggregate(lists, EAggregateMode.INTERSECT);
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
  public static AInvertedList union(final AInvertedList firstList,
    final AInvertedList secondList) {
    final LinkedList<AInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return AInvertedList.union(operands);
  }

  /**
   * Builds the union of the given lists.
   * 
   * @param lists
   *          Lists to unite
   * @return A list containing the merged data of all the given lists, i.e. the
   *         union
   */
  public static AInvertedList union(final Collection<AInvertedList> lists) {
    return AInvertedList.aggregate(lists, EAggregateMode.UNION);
  }

  /**
   * Aggregates the given lists by the given aggregation mode.
   * 
   * @param lists
   *          Lists to aggregate
   * @param mode
   *          Aggregation mode to use
   * @return A list containing the aggregated data of the given lists
   */
  private static AInvertedList aggregate(final Collection<AInvertedList> lists,
    final EAggregateMode mode) {
    int amountOfLists = lists.size();
    if (amountOfLists <= 1) {
      throw new IllegalArgumentException();
    }

    // Setup a priority queue containing all initial elements
    PriorityQueue<RecToIterCont> queue = new PriorityQueue<>();
    for (final AInvertedList list : lists) {
      final Iterator<Integer> records = list.getRecords().iterator();
      if (records.hasNext()) {
        final int firstRecord = records.next();
        final RecToIterCont container = new RecToIterCont(firstRecord, records);
        queue.add(container);
      }
    }

    // It is absolutely necessary that the insertion
    // order into this list is sorted, as the plain list itself does not sort
    // its elements.
    final AInvertedList resultingList = new PlainInvertedList();

    // Process all queue elements
    while (!queue.isEmpty()) {
      // Poll the smallest value of all lists
      RecToIterCont smallestContainer = queue.poll();
      final int smallestRecordId = smallestContainer.getRecordId();
      // Put the next value of this list in the queue
      final Iterator<Integer> smallestIter =
          smallestContainer.getRemainingRecordIds();
      if (smallestIter.hasNext()) {
        queue.add(new RecToIterCont(smallestIter.next(), smallestIter));
      }

      // Check if all other lists currently also hold this record
      int amountOfListsMatching = 1;
      while (amountOfListsMatching != amountOfLists) {
        RecToIterCont nextSmallestContainer = queue.peek();
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
            queue.add(
                new RecToIterCont(nextSmallestIter.next(), nextSmallestIter));
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
        if (mode == EAggregateMode.UNION) {
          resultingList.addRecord(smallestRecordId);
        }
      }
    }

    return resultingList;
  }

  /**
   * Adds a record to the inverted list.
   * 
   * @param recordId
   *          Record to add
   * @return If the record was added, i.e. not already contained
   */
  public abstract boolean addRecord(final int recordId);

  /**
   * Returns whether the inverted list contains the given record or not.
   * 
   * @param recordId
   *          The record in question
   * @return <tt>True</tt> if the record is contained, <tt>false</tt> otherwise
   */
  public abstract boolean containsRecord(final int recordId);

  /**
   * Gets all records of this inverted list. The order of how the records are
   * returned is ascending at all time.
   * 
   * @return All records of this inverted list in ascending order
   */
  public abstract Iterable<Integer> getRecords();

  /**
   * Gets the size of this inverted list, i.e. the amount of records it holds.
   * 
   * @return The size of this inverted list, i.e. the amount of records it holds
   */
  public abstract int getSize();

  /**
   * Returns whether this inverted list is empty, i.e. the amount of records it
   * holds is zero.
   * 
   * @return <tt>True</tt> if the list is empty, <tt>false</tt> otherwise
   */
  public abstract boolean isEmpty();
}
