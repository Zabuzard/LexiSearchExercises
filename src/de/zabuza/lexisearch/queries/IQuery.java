package de.zabuza.lexisearch.queries;

import de.zabuza.lexisearch.indexing.IInvertedList;

/**
 * Interface for queries. A query consists of several keywords and returns an
 * {@link IInvertedList} which holds all records aggregated by the type of
 * search.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          The keys used by this query
 */
public interface IQuery<K> {
  /**
   * Searches by combining each given keyword with an logical <tt>and</tt>.
   * 
   * @param keys
   *          The keywords to search for
   * @return An {@link IInvertedList} containing all records where all of the
   *         given keywords occur
   */
  public IInvertedList searchAnd(final Iterable<K> keys);

  /**
   * Searches by combining each given keyword with an logical <tt>or</tt>.
   * 
   * @param keys
   *          The keywords to search for
   * @return An {@link IInvertedList} containing all records where at least one
   *         of the given keywords occurs
   */
  public IInvertedList searchOr(final Iterable<K> keys);
}
