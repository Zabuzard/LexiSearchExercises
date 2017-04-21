package de.zabuza.lexisearch.benchmarking;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Generic implementation of {@link IMeasureSet} which holds the elements with a
 * hash-structure.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public final class MeasureSet<K> implements IMeasureSet<K> {
  /**
   * A set containing all measures of this object.
   */
  private final HashSet<IMeasure<K>> mMeasures;

  /**
   * Creates a new empty set of measures.
   */
  public MeasureSet() {
    this.mMeasures = new HashSet<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.benchmarking.IMeasureSet#addMeasure(de.zabuza.
   * lexisearch.benchmarking.IMeasure)
   */
  @Override
  public void addMeasure(final IMeasure<K> measure) {
    this.mMeasures.add(measure);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.benchmarking.IMeasureSet#evaluateRelevance(java.util.
   * Collection, java.util.List, de.zabuza.lexisearch.benchmarking.IGroundTruth)
   */
  @Override
  public Map<IMeasure<K>, Double> evaluateRelevance(final Collection<K> keys,
      final List<Posting> postings, final IGroundTruth<K> groundTruth) {
    final HashMap<IMeasure<K>, Double> measureToRelevance = new HashMap<>();
    for (final IMeasure<K> measure : this.mMeasures) {
      final double relevance =
          measure.evaluateRelevance(keys, postings, groundTruth);
      measureToRelevance.put(measure, Double.valueOf(relevance));
    }
    return measureToRelevance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.benchmarking.IMeasureSet#getMeasures()
   */
  @Override
  public Iterable<IMeasure<K>> getMeasures() {
    return Collections.unmodifiableSet(this.mMeasures);
  }

}
