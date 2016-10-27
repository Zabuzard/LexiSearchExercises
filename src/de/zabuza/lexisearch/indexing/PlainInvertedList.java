package de.zabuza.lexisearch.indexing;

import java.util.LinkedHashSet;

/**
 * Implementation of {@link AInvertedList} which does not hold its records
 * sorted at all time. However it assumes they are inserted in sorted order
 * which must hold at all time. Therefore its inserting complexity is in
 * <tt>O(1)</tt>.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class PlainInvertedList extends InvertedList {

  /**
   * Creates a new empty inverted list.
   */
  public PlainInvertedList() {
    super(new LinkedHashSet<>());
  }
}
