package de.zabuza.lexisearch.indexing;

public interface IInvertedIndex<KEY> {

  public boolean addRecord(final KEY key, final int recordId);

  public boolean containsKey(final KEY key);

  public boolean containsRecord(final KEY key, final int recordId);

  public IInvertedList getRecords(final KEY key);

  public Iterable<KEY> getKeys();

}
