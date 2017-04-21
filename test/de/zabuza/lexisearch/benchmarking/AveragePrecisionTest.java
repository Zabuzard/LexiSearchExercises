package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Test for {@link AveragePrecision}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class AveragePrecisionTest {

  /**
   * Test method for
   * {@link AveragePrecision#evaluateRelevance(Collection, List, IGroundTruth)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testEvaluateRelevance() {
    final AveragePrecision<String> averagePrecision = new AveragePrecision<>();
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    final LinkedList<String> query = new LinkedList<>();
    query.add("first");
    query.add("query");
    final LinkedList<Integer> relevantRecords = new LinkedList<>();
    relevantRecords.add(Integer.valueOf(10));
    relevantRecords.add(Integer.valueOf(582));
    relevantRecords.add(Integer.valueOf(10003));
    final LinkedList<Posting> results = new LinkedList<>();
    results.add(new Posting(582));
    results.add(new Posting(17));
    results.add(new Posting(5666));
    results.add(new Posting(10003));
    results.add(new Posting(10));
    groundTruth.addRelevantRecords(query, relevantRecords);
    final double precision =
        averagePrecision.evaluateRelevance(query, results, groundTruth);

    Assert.assertEquals(0.7, precision, 0.0005);

    // Test required by lecture
    final AveragePrecision<String> testPrecision = new AveragePrecision<>();
    final GroundTruth<String> testGroundTruth = new GroundTruth<>();
    final LinkedList<String> testQuery = new LinkedList<>();
    testQuery.add("test");
    testQuery.add("query");
    final LinkedList<Integer> testRelevantRecords = new LinkedList<>();
    testRelevantRecords.add(Integer.valueOf(10));
    testRelevantRecords.add(Integer.valueOf(582));
    testRelevantRecords.add(Integer.valueOf(877));
    testRelevantRecords.add(Integer.valueOf(10003));
    final LinkedList<Posting> testResults = new LinkedList<>();
    testResults.add(new Posting(582));
    testResults.add(new Posting(17));
    testResults.add(new Posting(5666));
    testResults.add(new Posting(10003));
    testResults.add(new Posting(10));
    testGroundTruth.addRelevantRecords(testQuery, testRelevantRecords);
    final double testPrecisionValue = testPrecision.evaluateRelevance(testQuery,
        testResults, testGroundTruth);

    Assert.assertEquals(0.525, testPrecisionValue, 0.0005);
  }

}
