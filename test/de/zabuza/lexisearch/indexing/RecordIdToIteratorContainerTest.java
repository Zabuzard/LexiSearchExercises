package de.zabuza.lexisearch.indexing;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link RecordIdToIteratorContainer}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class RecordIdToIteratorContainerTest {

  /**
   * Test method for
   * {@link RecordIdToIteratorContainer#compareTo(RecordIdToIteratorContainer)}.
   */
  @Test
  public void testCompareTo() {
    final int recordId = 0;
    final int anotherRecordId = 1;
    final LinkedList<Integer> remainingRecords = new LinkedList<>();
    remainingRecords.add(2);
    remainingRecords.add(3);
    final Iterator<Integer> remainignRecordsIter = remainingRecords.iterator();
    final RecordIdToIteratorContainer container =
        new RecordIdToIteratorContainer(recordId, remainignRecordsIter);
    final RecordIdToIteratorContainer anotherContainer =
        new RecordIdToIteratorContainer(anotherRecordId, remainignRecordsIter);

    Assert.assertTrue(container.compareTo(container) == 0);
    Assert.assertTrue(anotherContainer.compareTo(anotherContainer) == 0);
    Assert.assertTrue(container.compareTo(anotherContainer) < 0);
    Assert.assertTrue(anotherContainer.compareTo(container) > 0);
  }

  /**
   * Test method for {@link RecordIdToIteratorContainer#getRecordId()}.
   */
  @Test
  public void testGetRecordId() {
    final int recordId = 0;
    final int anotherRecordId = 1;
    final LinkedList<Integer> remainingRecords = new LinkedList<>();
    remainingRecords.add(2);
    remainingRecords.add(3);
    final Iterator<Integer> remainignRecordsIter = remainingRecords.iterator();
    final RecordIdToIteratorContainer container =
        new RecordIdToIteratorContainer(recordId, remainignRecordsIter);
    final RecordIdToIteratorContainer anotherContainer =
        new RecordIdToIteratorContainer(anotherRecordId, remainignRecordsIter);

    Assert.assertEquals(recordId, container.getRecordId());
    Assert.assertEquals(anotherRecordId, anotherContainer.getRecordId());
  }

  /**
   * Test method for
   * {@link RecordIdToIteratorContainer#getRemainingRecordIds()}.
   */
  @Test
  public void testGetRemainingRecordIds() {
    final int recordId = 0;
    final LinkedList<Integer> remainingRecords = new LinkedList<>();
    remainingRecords.add(2);
    remainingRecords.add(3);
    final Iterator<Integer> remainignRecordsIter = remainingRecords.iterator();

    final LinkedList<Integer> antoherRemainingRecords = new LinkedList<>();
    remainingRecords.add(4);
    remainingRecords.add(5);
    final Iterator<Integer> anotherRemainignRecordsIter =
        antoherRemainingRecords.iterator();

    final RecordIdToIteratorContainer container =
        new RecordIdToIteratorContainer(recordId, remainignRecordsIter);
    final RecordIdToIteratorContainer anotherContainer =
        new RecordIdToIteratorContainer(recordId, anotherRemainignRecordsIter);

    Assert.assertEquals(remainignRecordsIter,
        container.getRemainingRecordIds());
    Assert.assertEquals(anotherRemainignRecordsIter,
        anotherContainer.getRemainingRecordIds());
  }

  /**
   * Test method for
   * {@link RecordIdToIteratorContainer#RecordIdToIteratorContainer(int, Iterator)}
   * .
   */
  @Test
  public void testRecordIdToIteratorContainer() {
    final int recordId = 0;
    final LinkedList<Integer> remainingRecords = new LinkedList<>();
    remainingRecords.add(1);
    remainingRecords.add(2);
    final Iterator<Integer> remainignRecordsIter = remainingRecords.iterator();
    final RecordIdToIteratorContainer container =
        new RecordIdToIteratorContainer(recordId, remainignRecordsIter);

    Assert.assertEquals(recordId, container.getRecordId());
    Assert.assertEquals(remainignRecordsIter,
        container.getRemainingRecordIds());
  }

}
