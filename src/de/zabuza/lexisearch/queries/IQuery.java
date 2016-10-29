package de.zabuza.lexisearch.queries;

import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

/**
 * Interface for queries. A query consists of several keywords and returns a
 * list which holds all records aggregated by the type of search.
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
   * @return A list containing all records where all of the given keywords occur
   */
  List<Posting> searchAnd(final Iterable<K> keys);

  /**
   * Searches by combining each given keyword with an logical <tt>or</tt>.
   * 
   * @param keys
   *          The keywords to search for
   * @return A list containing all records where at least one of the given
   *         keywords occurs
   */
  List<Posting> searchOr(final Iterable<K> keys);
}
