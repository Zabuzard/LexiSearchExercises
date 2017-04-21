package de.zabuza.lexisearch.model.document;

/**
 * Implementation for {@link IDocument} which holds parameters like id, name and
 * description in memory.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public class Document implements IDocument {
  /**
   * Constant for an empty text. Is used for empty descriptions.
   */
  private static final String EMPTY_TEXT = "";
  /**
   * Constant for a tab value. Is used to separate content in some formats.
   */
  private static final String TAB_VALUE = "\t";
  /**
   * Pattern used to split content into all its words.
   */
  private static final String WORD_BOUNDARY_PATTERN = "\\W+";
  /**
   * Message which is shown when content should be parsed from a text format
   * that has an formatting error.
   */
  protected static final String MSG_WRONG_TEXT_FORMAT =
      "The given document as text is in the wrong format.";
  /**
   * Constant for an illegal value for an id.
   */
  protected static final int NO_ID = -1;
  /**
   * Index of the internal content information array where the description gets
   * saved.
   */
  protected static final int TEXT_FORMAT_DESCRIPTION_INDEX = 2;
  /**
   * Index of the internal content information array where the id gets saved.
   */
  protected static final int TEXT_FORMAT_ID_INDEX = 0;
  /**
   * Index of the internal content information array where the name gets saved.
   */
  protected static final int TEXT_FORMAT_NAME_INDEX = 1;

  /**
   * Builds a document representing the content given as text format.<br/>
   * The format is:
   * <tt>id{@literal <contentSeparator>}name{@literal <contentSeparator>}
   * description</tt><br/>
   * where <tt>description</tt> is optional.
   * 
   * @param documentAsText
   *          The document to represent given in a text format
   * @param contentSeparator
   *          The separator of the content
   * @return The representing document object
   * @throws IllegalArgumentException
   *           If the document is in the wrong format
   */
  public static Document buildFromText(final String documentAsText,
      final String contentSeparator) {
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

  /**
   * The description of the document.
   */
  private final String mDescription;
  /**
   * The id of the document.
   */
  private final int mId;
  /**
   * The name of the document.
   */
  private final String mName;

  /**
   * Creates a new document with a given id and name. The description is empty.
   * 
   * @param id
   *          The id of the document
   * @param name
   *          The name of the document
   */
  public Document(final int id, final String name) {
    this(id, name, EMPTY_TEXT);
  }

  /**
   * Creates a new document with a given id, name and description.
   * 
   * @param id
   *          The id of the document
   * @param name
   *          The name of the document
   * @param description
   *          The description of the document
   */
  public Document(final int id, final String name, final String description) {
    this.mId = id;
    this.mName = name;
    this.mDescription = description;
  }

  /**
   * Creates a new document with no valid id, an empty name and description.
   * This constructor should only be used by extending classes that override the
   * field-getter methods and maintain id, name and description by themselves.
   */
  protected Document() {
    this.mId = NO_ID;
    this.mName = EMPTY_TEXT;
    this.mDescription = EMPTY_TEXT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.IDocument#getDescription()
   */
  @Override
  public String getDescription() {
    return this.mDescription;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.IDocument#getId()
   */
  @Override
  public int getId() {
    return this.mId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IWordRecord#getWords()
   */
  @Override
  public String[] getKeys() {
    return (getName().toLowerCase() + TAB_VALUE
        + getDescription().toLowerCase()).split(WORD_BOUNDARY_PATTERN);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyRecord#getName()
   */
  @Override
  public String getName() {
    return this.mName;
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
   * @see de.zabuza.lexisearch.indexing.IKeyRecord#getSize()
   */
  @Override
  public int getSize() {
    return getKeys().length;
  }
}
