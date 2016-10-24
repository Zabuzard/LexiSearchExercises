package de.zabuza.lexisearch.indexing;

public final class InvertedIndexUtil {

  /**
   * Utility class. No implementation.
   */
  private InvertedIndexUtil() {

  }

  public static <T extends IWordRecord> IInvertedIndex<String>
    createFromWordRecords(final Iterable<T> wordRecords) {
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
}
