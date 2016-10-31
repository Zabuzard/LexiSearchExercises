package de.zabuza.lexisearch.indexing;

import java.util.Set;

/**
 * Set implementation which contain elements of type {@link IKeyRecord}. Offers
 * some additional methods like access to a record by its id.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <E>
 *          Type which is contained by this set, must extend {@link IKeyRecord}
 * @param <K>
 *          Type of the key contained by {@link IKeyRecord}.
 */
public interface IKeyRecordSet<E extends IKeyRecord<K>, K> extends Set<E> {
  /**
   * Gets a key record contained by this set by its id.
   * 
   * @param recordId
   *          The id of the record to get
   * @return The key record which has the given id or <tt>null</tt> if such a
   *         record is not contained in the set
   */
  E getKeyRecordById(int recordId);
}
