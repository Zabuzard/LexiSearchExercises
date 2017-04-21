package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Test for {@link MeasureSet}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class MeasureSetTest {

  /**
   * Test method for {@link MeasureSet#addMeasure(IMeasure)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testAddMeasure() {
    final MeasureSet<String> measures = new MeasureSet<>();
    final IMeasure<String> firstMeasure = new AveragePrecision<>();
    final IMeasure<String> secondMeasure = new PrecisionAtR<>();

    Assert.assertFalse(measures.getMeasures().iterator().hasNext());

    measures.addMeasure(firstMeasure);
    measures.addMeasure(secondMeasure);

    final Iterator<IMeasure<String>> measuresIter =
        measures.getMeasures().iterator();
    final IMeasure<String> firstMeasureOfIter = measuresIter.next();
    final IMeasure<String> secondMeasureOfIter = measuresIter.next();
    if (firstMeasureOfIter.equals(firstMeasure)) {
      Assert.assertEquals(firstMeasure, firstMeasureOfIter);
      Assert.assertEquals(secondMeasure, secondMeasureOfIter);
    } else {
      Assert.assertEquals(secondMeasure, firstMeasureOfIter);
      Assert.assertEquals(firstMeasure, secondMeasureOfIter);
    }

    Assert.assertFalse(measuresIter.hasNext());
  }

  /**
   * Test method for
   * {@link MeasureSet#evaluateRelevance(Collection, List, IGroundTruth)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testEvaluateRelevance() {
    final MeasureSet<String> measures = new MeasureSet<>();
    final IMeasure<String> firstMeasure = new AveragePrecision<>();
    final IMeasure<String> secondMeasure = new PrecisionAtR<>();
    measures.addMeasure(firstMeasure);
    measures.addMeasure(secondMeasure);

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

    final Map<IMeasure<String>, Double> resultValues =
        measures.evaluateRelevance(query, results, groundTruth);
    Assert.assertEquals(0.7, resultValues.get(firstMeasure).doubleValue(),
        0.0005);
    Assert.assertEquals(0.3333, resultValues.get(secondMeasure).doubleValue(),
        0.0005);
  }

  /**
   * Test method for {@link MeasureSet#getMeasures()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetMeasures() {
    final MeasureSet<String> measures = new MeasureSet<>();
    final IMeasure<String> firstMeasure = new AveragePrecision<>();
    final IMeasure<String> secondMeasure = new PrecisionAtR<>();

    Assert.assertFalse(measures.getMeasures().iterator().hasNext());

    measures.addMeasure(firstMeasure);
    measures.addMeasure(secondMeasure);

    final Iterator<IMeasure<String>> measuresIter =
        measures.getMeasures().iterator();
    final IMeasure<String> firstMeasureOfIter = measuresIter.next();
    final IMeasure<String> secondMeasureOfIter = measuresIter.next();
    if (firstMeasureOfIter.equals(firstMeasure)) {
      Assert.assertEquals(firstMeasure, firstMeasureOfIter);
      Assert.assertEquals(secondMeasure, secondMeasureOfIter);
    } else {
      Assert.assertEquals(secondMeasure, firstMeasureOfIter);
      Assert.assertEquals(firstMeasure, secondMeasureOfIter);
    }

    Assert.assertFalse(measuresIter.hasNext());
  }

  /**
   * Test method for {@link MeasureSet#MeasureSet()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testMeasureSet() {
    final MeasureSet<String> measures = new MeasureSet<>();
    final IMeasure<String> firstMeasure = new AveragePrecision<>();
    final IMeasure<String> secondMeasure = new PrecisionAtR<>();

    Assert.assertFalse(measures.getMeasures().iterator().hasNext());

    measures.addMeasure(firstMeasure);
    measures.addMeasure(secondMeasure);

    final Iterator<IMeasure<String>> measuresIter =
        measures.getMeasures().iterator();
    final IMeasure<String> firstMeasureOfIter = measuresIter.next();
    final IMeasure<String> secondMeasureOfIter = measuresIter.next();
    if (firstMeasureOfIter.equals(firstMeasure)) {
      Assert.assertEquals(firstMeasure, firstMeasureOfIter);
      Assert.assertEquals(secondMeasure, secondMeasureOfIter);
    } else {
      Assert.assertEquals(secondMeasure, firstMeasureOfIter);
      Assert.assertEquals(firstMeasure, secondMeasureOfIter);
    }

    Assert.assertFalse(measuresIter.hasNext());
  }

}
