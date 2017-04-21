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
  static IInvertedList aggregate(final Collection<IInvertedList> lists,
      final EAggregateMode mode) {
    final int amountOfLists = lists.size();
    if (amountOfLists <= 1) {
      throw new IllegalArgumentException();
    }

    // Setup a priority queue containing all initial elements
    final PriorityQueue<PostToIterCont> queue = new PriorityQueue<>();
    for (final IInvertedList list : lists) {
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
    final IInvertedList resultingList = new PlainInvertedList();

    // Process all queue elements
    while (!queue.isEmpty()) {
      // Poll the smallest value of all lists
      final PostToIterCont smallestContainer = queue.poll();
      final int smallestRecordId = smallestContainer.getRecordId();
      // Put the next value of this list in the queue
      final Iterator<Posting> smallestIter =
          smallestContainer.getRemainingPostings();
      if (smallestIter.hasNext()) {
        queue.add(new PostToIterCont(smallestIter.next(), smallestIter));
      }

      int totalTermFrequency =
          smallestContainer.getPosting().getTermFrequency();
      double totalScore = smallestContainer.getPosting().getScore();

      // Check if all other lists currently also hold this record
      int amountOfListsMatching = 1;
      while (amountOfListsMatching != amountOfLists) {
        final PostToIterCont nextSmallestContainer = queue.peek();
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

          totalTermFrequency +=
              nextSmallestContainer.getPosting().getTermFrequency();
          totalScore += nextSmallestContainer.getPosting().getScore();
        } else {
          // The list does not hold the record, break the loop as
          // the outcome is known
          break;
        }
      }

      if (amountOfListsMatching == amountOfLists) {
        // The record is hold by every list
        resultingList.addPosting(smallestRecordId, totalTermFrequency,
            totalScore);
      } else {
        // The record is not hold by every list
        if (mode == EAggregateMode.UNION) {
          resultingList.addPosting(smallestRecordId, totalTermFrequency,
              totalScore);
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
  static IInvertedList intersect(final Collection<IInvertedList> lists) {
    return IInvertedList.aggregate(lists, EAggregateMode.INTERSECT);
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
  static IInvertedList intersect(final IInvertedList firstList,
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
  static IInvertedList union(final Collection<IInvertedList> lists) {
    return IInvertedList.aggregate(lists, EAggregateMode.UNION);
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
  static IInvertedList union(final IInvertedList firstList,
      final IInvertedList secondList) {
    final LinkedList<IInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return IInvertedList.union(operands);
  }

  /**
   * Adds a posting to the inverted list.
   * 
   * @param recordId
   *          Record to add
   * @return <tt>True</tt> if the posting was not already contained,
   *         <tt>false</tt> otherwise
   */
  boolean addPosting(int recordId);

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
  boolean addPosting(int recordId, int termFrequency);

  /**
   * Adds a posting to the inverted list.
   * 
   * @param recordId
   *          Record to add
   * @param termFrequency
   *          The term frequency of the record
   * @param score
   *          The score of the posting
   * @return <tt>True</tt> if the posting was not already contained,
   *         <tt>false</tt> otherwise
   */
  boolean addPosting(int recordId, int termFrequency, double score);

  /**
   * Returns whether the inverted list contains the given posting or not.
   * 
   * @param recordId
   *          The record in question
   * @return <tt>True</tt> if the posting is contained, <tt>false</tt> otherwise
   */
  boolean containsPosting(int recordId);

  /**
   * Gets all postings of this inverted list. The order of how the records are
   * returned is ascending in their IDs at all time.
   * 
   * @return All postings of this inverted list in ascending order of their IDs
   */
  Iterable<Posting> getPostings();

  /**
   * Gets the size of this inverted list, i.e. the amount of postings it holds.
   * 
   * @return The size of this inverted list, i.e. the amount of postings it
   *         holds
   */
  int getSize();

  /**
   * Returns whether this inverted list is empty, i.e. the amount of postings it
   * holds is zero.
   * 
   * @return <tt>True</tt> if the list is empty, <tt>false</tt> otherwise
   */
  boolean isEmpty();
}
