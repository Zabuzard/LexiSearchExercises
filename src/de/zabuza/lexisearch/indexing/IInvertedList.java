package de.zabuza.lexisearch.indexing;

public interface IInvertedList {
  public boolean addRecord(final int recordId);

  public boolean containsRecord(final int recordId);

  public Iterable<Integer> getRecords();

  public int getSize();
}
