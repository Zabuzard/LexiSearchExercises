package de.zabuza.lexisearch.indexing;

import de.zabuza.lexisearch.model.document.IDocument;

/**
 * Utility class for {@link IInvertedIndex}. Provides methods for building
 * indices that operate on different structures.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class InvertedIndexUtil {

  /**
   * Creates an {@link IInvertedIndex} which operates on a given collection of
   * {@link IKeyRecord}<tt>{@literal <String>}</tt> which can, for example, be
   * {@link IDocument}s.
   * 
   * @param <T>
   *          The exact type of records
   * @param wordRecords
   *          Iterable collection of word records the index should operate on
   * @return An {@link IInvertedIndex} which operates on the given collection of
   *         {@link IKeyRecord}s
   */
  public static <T extends IKeyRecord<String>> IInvertedIndex<String>
      createFromWords(final Iterable<T> wordRecords) {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();

    for (final T wordRecord : wordRecords) {
      final int recordId = wordRecord.getRecordId();
      final String[] words = wordRecord.getKeys();

      for (final String word : words) {
        invertedIndex.addRecord(word, recordId);
      }
    }

    return invertedIndex;
  }

  /**
   * Utility class. No implementation.
   */
  private InvertedIndexUtil() {

  }
}
