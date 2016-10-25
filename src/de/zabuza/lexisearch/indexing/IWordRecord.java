package de.zabuza.lexisearch.indexing;

/**
 * Interface for word records. Such records have an ID and contain words.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public interface IWordRecord {

  /**
   * Gets the id of this record.
   * 
   * @return The id of this record
   */
  int getRecordId();

  /**
   * Gets all words contained by this record.
   * 
   * @return All words contained by this record
   */
  String[] getWords();
}
