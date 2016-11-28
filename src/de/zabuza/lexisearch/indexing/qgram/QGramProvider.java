package de.zabuza.lexisearch.indexing.qgram;

import de.zabuza.lexisearch.indexing.IKeyProvider;

/**
 * Implementation of {@link IKeyProvider} which splits its record according to a
 * q-gram technique.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class QGramProvider implements IKeyProvider<String, String> {
  /**
   * Represents all patterns that will be removed while normalizing a record
   * with {@link QGramProvider#normalizeRecord(String)}.
   */
  private static final String NORMALIZE_REMOVE_PATTERN = "\\W";
  /**
   * The default character used for padding.
   */
  protected static final char DEFAULT_PADDING_CHARACTER = '$';

  /**
   * Normalizes the given record. This removes all special characters and lowers
   * the text.
   * 
   * @param record
   *          The record to normalize
   * @return The normalized record
   */
  public static String normalizeRecord(final String record) {
    return record.toLowerCase().replaceAll(NORMALIZE_REMOVE_PATTERN, "");
  }

  /**
   * Creates the text to use for padding according to the given parameters.
   * 
   * @param qParameter
   *          The q-Parameter to use
   * @param paddingCharacter
   *          The padding character to use
   * @return The text to use for padding according to the given parameters
   */
  private static String createPadding(final int qParameter,
      final char paddingCharacter) {
    final StringBuilder padding = new StringBuilder();
    for (int i = 0; i < qParameter - 1; i++) {
      padding.append(paddingCharacter);
    }
    return padding.toString();
  }

  /**
   * The text to use for padding.
   */
  private final String mPadding;

  /**
   * The q-Parameter to use.
   */
  private final int mQParameter;

  /**
   * Creates a new q-Gram provider with default padding.
   * 
   * @param qParameter
   *          The q-Parameter to use
   */
  public QGramProvider(final int qParameter) {
    this(qParameter, DEFAULT_PADDING_CHARACTER);
  }

  /**
   * Creates a new q-Gram provider with the given padding character.
   * 
   * @param qParameter
   *          The q-Parameter to use
   * @param padding
   *          The character to use for padding
   */
  public QGramProvider(final int qParameter, final char padding) {
    mQParameter = qParameter;
    mPadding = createPadding(qParameter, padding);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyProvider#getKeys(java.lang.Object)
   */
  @Override
  public String[] getKeys(final String record) {
    final String normalizedRecord = normalizeRecord(record);
    final String normalizedRecordWithPadding = mPadding + normalizedRecord;
    final String[] keys = new String[normalizedRecord.length()];
    for (int i = 0; i < normalizedRecordWithPadding.length() - mQParameter
        + 1; i++) {
      keys[i] = normalizedRecordWithPadding.substring(i, i + mQParameter);
    }
    return keys;
  }

  /**
   * Gets the qParameter.
   * 
   * @return The qParameter to get
   */
  public int getQParameter() {
    return mQParameter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyProvider#getSize(java.lang.Object)
   */
  @Override
  public int getSize(final String record) {
    return getKeys(record).length;
  }

}
