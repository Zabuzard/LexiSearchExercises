package de.zabuza.lexisearch.document;

/**
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public class Document implements IDocument {
  private static final String EMPTY_TEXT = "";
  private static final String TAB_VALUE = "\t";
  private static final String WORD_BOUNDARY_PATTERN = "\\W+";
  protected static final String MSG_WRONG_TEXT_FORMAT =
      "The given document as text is in the wrong format.";
  protected static final int NO_ID = -1;
  protected static final int TEXT_FORMAT_DESCRIPTION_INDEX = 2;
  protected static final int TEXT_FORMAT_ID_INDEX = 0;

  protected static final int TEXT_FORMAT_NAME_INDEX = 1;

  public static Document buildFromText(final String documentAsText,
    final String contentSeparator) throws IllegalArgumentException {
    final String[] content = documentAsText.split(contentSeparator);

    if (content.length == 2) {
      return new Document(Integer.parseInt(content[TEXT_FORMAT_ID_INDEX]),
          content[TEXT_FORMAT_NAME_INDEX]);
    } else if (content.length == 3) {
      return new Document(Integer.parseInt(content[TEXT_FORMAT_ID_INDEX]),
          content[TEXT_FORMAT_NAME_INDEX],
          content[TEXT_FORMAT_DESCRIPTION_INDEX]);
    } else {
      throw new IllegalArgumentException(MSG_WRONG_TEXT_FORMAT);
    }
  }

  private final String mDescription;

  private final int mId;

  private final String mName;

  public Document(final int id, final String name) {
    this(id, name, EMPTY_TEXT);
  }

  public Document(final int id, final String name, final String description) {
    mId = id;
    mName = name;
    mDescription = description;
  }

  protected Document() {
    mId = NO_ID;
    mName = EMPTY_TEXT;
    mDescription = EMPTY_TEXT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.IDocument#getDescription()
   */
  @Override
  public String getDescription() {
    return mDescription;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.IDocument#getId()
   */
  @Override
  public int getId() {
    return mId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.IDocument#getName()
   */
  @Override
  public String getName() {
    return mName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IWordRecord#getRecordId()
   */
  @Override
  public int getRecordId() {
    return getId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IWordRecord#getWords()
   */
  @Override
  public String[] getWords() {
    return (getName() + TAB_VALUE + getDescription())
        .split(WORD_BOUNDARY_PATTERN);
  }
}
