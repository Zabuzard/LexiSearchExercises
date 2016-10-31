package de.zabuza.lexisearch.document;

import de.zabuza.lexisearch.indexing.IKeyRecord;

/**
 * Interface for documents.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public interface IDocument extends IKeyRecord<String> {
  /**
   * Gets the description of the document.
   * 
   * @return The description of the document to get
   */
  String getDescription();

  /**
   * Gets the id of the document.
   * 
   * @return The id of the document to get
   */
  int getId();

  /**
   * Gets the name of the document.
   * 
   * @return The name of the document to get
   */
  String getName();
}
