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
    PriorityQueue<PostToIterCont> queue = new PriorityQueue<>();
    for (final AInvertedList list : lists) {
      final Iterator<Posting> postings = list.getPostings().iterator();
      if (postings.hasNext()) {
        final Posting firstPosting = postings.next();
        final PostToIterCont container =
            new PostToIterCont(firstPosting, postings);
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
      PostToIterCont smallestContainer = queue.poll();
      final int smallestRecordId = smallestContainer.getRecordId();
      // Put the next value of this list in the queue
      final Iterator<Posting> smallestIter =
          smallestContainer.getRemainingPostings();
      if (smallestIter.hasNext()) {
        queue.add(new PostToIterCont(smallestIter.next(), smallestIter));
      }

      // Check if all other lists currently also hold this record
      int amountOfListsMatching = 1;
      while (amountOfListsMatching != amountOfLists) {
        PostToIterCont nextSmallestContainer = queue.peek();
        if (nextSmallestContainer == null) {
          // The queue is empty, break
          break;
        }
        final int nextSmallestRecordId = nextSmallestContainer.getRecordId();
        if (nextSmallestRecordId == smallestRecordId) {
          // The list also holds this record, advance it
          amountOfListsMatching++;
          queue.poll();
          final Iterator<Posting> nextSmallestIter =
              nextSmallestContainer.getRemainingPostings();
          if (nextSmallestIter.hasNext()) {
            queue.add(
                new PostToIterCont(nextSmallestIter.next(), nextSmallestIter));
          }
        } else {
          // The list does not hold the record, break the loop as
          // the outcome is known
          break;
        }
      }

      if (amountOfListsMatching == amountOfLists) {
        // The record is hold by every list
        // TODO Aggregate the term frequency for the new record by adding from
        // all other containers
        resultingList.addPosting(smallestRecordId);
      } else {
        // The record is not hold by every list
        if (mode == EAggregateMode.UNION) {
          // TODO Aggregate the term frequency for the new record by adding from
          // all other containers
          resultingList.addPosting(smallestRecordId);
        }
      }
    }

    return resultingList;
  }

  /**
   * Adds a posting to the inverted list.
   * 
   * @param recordId
   *          Record to add
   * @return <tt>True</tt> if the posting was not already contained,
   *         <tt>false</tt> otherwise
   */
  public abstract boolean addPosting(final int recordId);

  /**
   * Adds a posting to the inverted list.
   * 
   * @param recordId
   *          Record to add
   * @param termFrequency
   *          The term frequency of the record
   * @return <tt>True</tt> if the posting was not already contained,
   *         <tt>false</tt> otherwise
   */
  public abstract boolean addPosting(final int recordId,
    final int termFrequency);

  /**
   * Returns whether the inverted list contains the given posting or not.
   * 
   * @param recordId
   *          The record in question
   * @return <tt>True</tt> if the posting is contained, <tt>false</tt> otherwise
   */
  public abstract boolean containsPosting(final int recordId);

  /**
   * Gets all postings of this inverted list. The order of how the records are
   * returned is ascending in their IDs at all time.
   * 
   * @return All postings of this inverted list in ascending order of their IDs
   */
  public abstract Iterable<Posting> getPostings();

  /**
   * Gets the size of this inverted list, i.e. the amount of postings it holds.
   * 
   * @return The size of this inverted list, i.e. the amount of postings it
   *         holds
   */
  public abstract int getSize();

  /**
   * Returns whether this inverted list is empty, i.e. the amount of postings it
   * holds is zero.
   * 
   * @return <tt>True</tt> if the list is empty, <tt>false</tt> otherwise
   */
  public abstract boolean isEmpty();
}
