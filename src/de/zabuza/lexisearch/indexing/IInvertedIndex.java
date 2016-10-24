package de.zabuza.lexisearch.indexing;

public interface IInvertedIndex<K> {

  public boolean addRecord(final K key, final int recordId);

  public boolean containsKey(final K key);

  public boolean containsRecord(final K key, final int recordId);

  public IInvertedList getRecords(final K key);

  public Iterable<K> getKeys();

}
