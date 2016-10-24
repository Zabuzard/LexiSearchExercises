package de.zabuza.lexisearch.queries;

import de.zabuza.lexisearch.indexing.IInvertedList;

public interface Query<KEY> {
  public IInvertedList searchAnd(final Iterable<KEY> keys);

  public IInvertedList searchOr(final Iterable<KEY> keys);
}
