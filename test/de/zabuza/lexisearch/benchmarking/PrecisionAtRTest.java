package de.zabuza.lexisearch.benchmarking;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Test for {@link PrecisionAtR}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PrecisionAtRTest {

  /**
   * Test method for
   * {@link PrecisionAtR#evaluateRelevance(Collection, List, IGroundTruth)}.
   */
  @Test
  public void testEvaluateRelevance() {
    final PrecisionAtR<String> precision = new PrecisionAtR<>();
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

    final double precisionValue =
        precision.evaluateRelevance(query, results, groundTruth);
    Assert.assertEquals(0.3333, precisionValue, 0.0005);
  }

}
