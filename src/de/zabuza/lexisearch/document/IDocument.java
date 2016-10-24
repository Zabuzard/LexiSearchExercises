package de.zabuza.lexisearch.document;

import de.zabuza.lexisearch.indexing.IWordRecord;

/**
 * Interface for documents.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public interface IDocument extends IWordRecord {
  /**
   * Gets the description of the document.
   * 
   * @return The description of the document to get
   */
  public String getDescription();

  /**
   * Gets the id of the document.
   * 
   * @return The id of the document to get
   */
  public int getId();

  /**
   * Gets the name of the document.
   * 
   * @return The name of the document to get
   */
  public String getName();
}
