package de.zabuza.lexisearch.indexing;

/**
 * Enumeration for different modes on how to aggregate data.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public enum EAggregateMode {
  /**
   * Build the intersection of all given data.
   */
  INTERSECT, /**
              * Build the union of all given data.
              */
  UNION
}
