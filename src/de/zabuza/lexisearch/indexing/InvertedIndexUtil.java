package de.zabuza.lexisearch.indexing;

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
   * {@link IWordRecord}s which can, for example, be {@link IDocument}s.
   * 
   * @param wordRecords
   *          Iterable collection of word records the index should operate on
   * @return An {@link IInvertedIndex} which operates on the given collection of
   *         {@link IWordRecord}s
   */
  public static <T extends IWordRecord> IInvertedIndex<String> createFromWords(
    final Iterable<T> wordRecords) {
    final InvertedIndex<String> invertedIndex = new InvertedIndex<>();

    for (final T wordRecord : wordRecords) {
      final int recordId = wordRecord.getRecordId();
      final String[] words = wordRecord.getWords();

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
