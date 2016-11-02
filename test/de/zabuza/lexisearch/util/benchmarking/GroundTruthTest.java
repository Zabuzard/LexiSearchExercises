package de.zabuza.lexisearch.util.benchmarking;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.benchmarking.GroundTruth;

/**
 * Test for {@link GroundTruth}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class GroundTruthTest {

  /**
   * Test method for
   * {@link GroundTruth#addRelevantRecords(Collection, Collection)}.
   */
  @Test
  public void testAddRelevantRecords() {
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("first");
    firstQuery.add("query");
    final HashSet<Integer> firstResults = new HashSet<>();
    firstResults.add(0);
    firstResults.add(1);
    firstResults.add(4);
    firstResults.add(2);
    groundTruth.addRelevantRecords(firstQuery, firstResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(firstQuery));
    final Collection<Integer> firstRelevantRecords =
        groundTruth.getRelevantRecords(firstQuery);
    Assert.assertEquals(firstResults, firstRelevantRecords);

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("second");
    secondQuery.add("query");
    final HashSet<Integer> secondResults = new HashSet<>();
    secondResults.add(99);
    secondResults.add(11);
    groundTruth.addRelevantRecords(secondQuery, secondResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(secondQuery));
    final Collection<Integer> secondRelevantRecords =
        groundTruth.getRelevantRecords(secondQuery);
    Assert.assertEquals(secondResults, secondRelevantRecords);
  }

  /**
   * Test method for
   * {@link GroundTruth#buildFromTextIterator(Iterator, String, String)}.
   */
  @Test
  public void testBuildFromTextIterator() {
    final LinkedList<String> textList = new LinkedList<>();
    textList.add("first query\t1 2 5 3");
    textList.add("second query\t100 12");
    final GroundTruth<String> groundTruth =
        GroundTruth.buildFromTextIterator(textList.iterator(), "\t", " ");

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("first");
    firstQuery.add("query");
    final HashSet<Integer> firstResults = new HashSet<>();
    firstResults.add(0);
    firstResults.add(1);
    firstResults.add(4);
    firstResults.add(2);
    Assert.assertTrue(groundTruth.hasRelevantRecords(firstQuery));
    final Collection<Integer> firstRelevantRecords =
        groundTruth.getRelevantRecords(firstQuery);
    Assert.assertEquals(firstResults, firstRelevantRecords);

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("second");
    secondQuery.add("query");
    final HashSet<Integer> secondResults = new HashSet<>();
    secondResults.add(99);
    secondResults.add(11);
    Assert.assertTrue(groundTruth.hasRelevantRecords(secondQuery));
    final Collection<Integer> secondRelevantRecords =
        groundTruth.getRelevantRecords(secondQuery);
    Assert.assertEquals(secondResults, secondRelevantRecords);
  }

  /**
   * Test method for {@link GroundTruth#getKeysForRelevantRecords()}.
   */
  @Test
  public void testGetKeysForRelevantRecords() {
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("first");
    firstQuery.add("query");
    final HashSet<Integer> firstResults = new HashSet<>();
    firstResults.add(0);
    firstResults.add(1);
    firstResults.add(4);
    firstResults.add(2);
    groundTruth.addRelevantRecords(firstQuery, firstResults);

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("second");
    secondQuery.add("query");
    final HashSet<Integer> secondResults = new HashSet<>();
    secondResults.add(99);
    secondResults.add(11);
    groundTruth.addRelevantRecords(secondQuery, secondResults);

    final Collection<Collection<String>> keys = new HashSet<>();
    keys.add(firstQuery);
    keys.add(secondQuery);

    Assert.assertEquals(keys, groundTruth.getKeysForRelevantRecords());
  }

  /**
   * Test method for {@link GroundTruth#getRelevantRecords(Collection)}.
   */
  @Test
  public void testGetRelevantRecords() {
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("first");
    firstQuery.add("query");
    final HashSet<Integer> firstResults = new HashSet<>();
    firstResults.add(0);
    firstResults.add(1);
    firstResults.add(4);
    firstResults.add(2);
    groundTruth.addRelevantRecords(firstQuery, firstResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(firstQuery));
    final Collection<Integer> firstRelevantRecords =
        groundTruth.getRelevantRecords(firstQuery);
    Assert.assertEquals(firstResults, firstRelevantRecords);

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("second");
    secondQuery.add("query");
    final HashSet<Integer> secondResults = new HashSet<>();
    secondResults.add(99);
    secondResults.add(11);
    groundTruth.addRelevantRecords(secondQuery, secondResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(secondQuery));
    final Collection<Integer> secondRelevantRecords =
        groundTruth.getRelevantRecords(secondQuery);
    Assert.assertEquals(secondResults, secondRelevantRecords);
  }

  /**
   * Test method for {@link GroundTruth#GroundTruth()}.
   */
  @Test
  public void testGroundTruth() {
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("first");
    firstQuery.add("query");
    final HashSet<Integer> firstResults = new HashSet<>();
    firstResults.add(0);
    firstResults.add(1);
    firstResults.add(4);
    firstResults.add(2);
    groundTruth.addRelevantRecords(firstQuery, firstResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(firstQuery));
    final Collection<Integer> firstRelevantRecords =
        groundTruth.getRelevantRecords(firstQuery);
    Assert.assertEquals(firstResults, firstRelevantRecords);

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("second");
    secondQuery.add("query");
    final HashSet<Integer> secondResults = new HashSet<>();
    secondResults.add(99);
    secondResults.add(11);
    groundTruth.addRelevantRecords(secondQuery, secondResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(secondQuery));
    final Collection<Integer> secondRelevantRecords =
        groundTruth.getRelevantRecords(secondQuery);
    Assert.assertEquals(secondResults, secondRelevantRecords);
  }

  /**
   * Test method for {@link GroundTruth#hasRelevantRecords(Collection)}.
   */
  @Test
  public void testHasRelevantRecords() {
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> firstQuery = new LinkedList<>();
    firstQuery.add("first");
    firstQuery.add("query");
    final HashSet<Integer> firstResults = new HashSet<>();
    firstResults.add(0);
    firstResults.add(1);
    firstResults.add(4);
    firstResults.add(2);
    groundTruth.addRelevantRecords(firstQuery, firstResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(firstQuery));
    final Collection<Integer> firstRelevantRecords =
        groundTruth.getRelevantRecords(firstQuery);
    Assert.assertEquals(firstResults, firstRelevantRecords);

    final LinkedList<String> secondQuery = new LinkedList<>();
    secondQuery.add("second");
    secondQuery.add("query");
    final HashSet<Integer> secondResults = new HashSet<>();
    secondResults.add(99);
    secondResults.add(11);
    groundTruth.addRelevantRecords(secondQuery, secondResults);
    Assert.assertTrue(groundTruth.hasRelevantRecords(secondQuery));
    final Collection<Integer> secondRelevantRecords =
        groundTruth.getRelevantRecords(secondQuery);
    Assert.assertEquals(secondResults, secondRelevantRecords);
  }

}
