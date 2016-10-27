package de.zabuza.lexisearch.indexing;

import java.util.Set;

public interface IKeyRecordSet<E extends IKeyRecord<K>, K> extends Set<E> {
  E getKeyRecordById(final int recordId);
}
