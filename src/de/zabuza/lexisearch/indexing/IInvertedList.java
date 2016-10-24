package de.zabuza.lexisearch.indexing;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public interface IInvertedList {
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

  public static IInvertedList intersect(final Collection<IInvertedList> lists) {
    return IInvertedList.aggregate(lists, AggregateMode.INTERSECT);
  }

  public static IInvertedList intersect(final IInvertedList firstList,
    final IInvertedList secondList) {
    final LinkedList<IInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return IInvertedList.intersect(operands);
  }

  public static IInvertedList union(final Collection<IInvertedList> lists) {
    return IInvertedList.aggregate(lists, AggregateMode.UNION);
  }

  public static IInvertedList union(final IInvertedList firstList,
    final IInvertedList secondList) {
    final LinkedList<IInvertedList> operands = new LinkedList<>();
    operands.add(firstList);
    operands.add(secondList);
    return IInvertedList.union(operands);
  }

  public boolean addRecord(final int recordId);

  public boolean containsRecord(final int recordId);

  public Iterable<Integer> getRecords();

  public int getSize();

  public boolean isEmpty();
}
