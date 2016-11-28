package de.zabuza.lexisearch.benchmarking;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Test for {@link PrecisionAtK}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PrecisionAtKTest {

  /**
   * Test method for
   * {@link PrecisionAtK#evaluateRelevance(Collection, List, IGroundTruth)}.
   */
  @Test
  public void testEvaluateRelevance() {
    final int firstK = 2;
    final PrecisionAtK<String> firstPrecision = new PrecisionAtK<>(firstK);
    final int secondK = 5;
    final PrecisionAtK<String> secondPrecision = new PrecisionAtK<>(secondK);
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> query = new LinkedList<>();
    query.add("first");
    query.add("query");
    final LinkedList<Integer> relevantRecords = new LinkedList<>();
    relevantRecords.add(10);
    relevantRecords.add(582);
    relevantRecords.add(10003);
    final LinkedList<Posting> results = new LinkedList<>();
    results.add(new Posting(582));
    results.add(new Posting(17));
    results.add(new Posting(5666));
    results.add(new Posting(10003));
    results.add(new Posting(10));
    groundTruth.addRelevantRecords(query, relevantRecords);

    final double firstPrecisionValue =
        firstPrecision.evaluateRelevance(query, results, groundTruth);
    Assert.assertEquals(0.5, firstPrecisionValue, 0);
    final double secondPrecisionValue =
        secondPrecision.evaluateRelevance(query, results, groundTruth);
    Assert.assertEquals(0.6, secondPrecisionValue, 0);

    // Test required by lecture
    final int testK = 4;
    final PrecisionAtK<String> testPrecision = new PrecisionAtK<>(testK);
    final GroundTruth<String> testGroundTruth = new GroundTruth<>();
    final LinkedList<String> testQuery = new LinkedList<>();
    testQuery.add("test");
    testQuery.add("query");
    final LinkedList<Integer> testRelevantRecords = new LinkedList<>();
    testRelevantRecords.add(0);
    testRelevantRecords.add(2);
    testRelevantRecords.add(5);
    testRelevantRecords.add(6);
    testRelevantRecords.add(7);
    testRelevantRecords.add(8);
    final LinkedList<Posting> testResults = new LinkedList<>();
    testResults.add(new Posting(0));
    testResults.add(new Posting(1));
    testResults.add(new Posting(2));
    testResults.add(new Posting(5));
    testResults.add(new Posting(6));
    testGroundTruth.addRelevantRecords(testQuery, testRelevantRecords);
    final double testPrecisionValue = testPrecision.evaluateRelevance(testQuery,
        testResults, testGroundTruth);

    Assert.assertEquals(0.75, testPrecisionValue, 0.0005);
  }

  /**
   * Test method for {@link PrecisionAtK#getKParameter()}.
   */
  @Test
  public void testGetKParameter() {
    final int firstK = 1;
    final PrecisionAtK<String> firstPrecision = new PrecisionAtK<>(firstK);
    final int secondK = 2;
    final PrecisionAtK<String> secondPrecision = new PrecisionAtK<>(secondK);

    Assert.assertEquals(firstK, firstPrecision.getKParameter());
    Assert.assertEquals(secondK, secondPrecision.getKParameter());
  }

  /**
   * Test method for {@link PrecisionAtK#PrecisionAtK(int)}.
   */
  @Test
  public void testPrecisionAtK() {
    final int firstK = 1;
    final PrecisionAtK<String> firstPrecision = new PrecisionAtK<>(firstK);
    final int secondK = 2;
    final PrecisionAtK<String> secondPrecision = new PrecisionAtK<>(secondK);

    Assert.assertEquals(firstK, firstPrecision.getKParameter());
    Assert.assertEquals(secondK, secondPrecision.getKParameter());
  }

}
