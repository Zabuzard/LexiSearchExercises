package de.zabuza.lexisearch.document;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;

/**
 * Implementation for {@link IDocument} which does not hold parameters like id,
 * name and description in memory but their position in a given file. Thus its
 * getter-methods need to lookup the data each time which makes the class slow
 * but therefore memory efficient.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class FixFileLookupDocument extends Document {

  /**
   * The flag which indicates read-only mode in {@link RandomAccessFile}s.
   */
  private static final String FLAG_READ_ONLY = "r";

  /**
   * Builds an document representing the content given as text format.<br/>
   * The format is:
   * <tt>id{@literal <contentSeparator>}name{@literal <contentSeparator>}
   * description</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param id
   *          An optional id of the document. If not present then the method
   *          assumes the id is given in the text format.
   * @param file
   *          The file representing the document
   * @param lineStartPos
   *          The position in bytes from the start of the file where the content
   *          for this document begins
   * @param contentSeparator
   *          The separator of the content
   * @return The representing document object
   */
  public static FixFileLookupDocument buildFromFixFileLineText(
    final Optional<Integer> id, final File file, final long lineStartPos,
    final String contentSeparator) {
    if (id.isPresent()) {
      return new FixFileLookupDocument(id.get(), file, lineStartPos,
          contentSeparator);
    } else {
      return new FixFileLookupDocument(file, lineStartPos, contentSeparator);
    }
  }

  /**
   * The separator of the content.
   */
  private final String mContentSeparator;
  /**
   * Whether the method should fetch the id from the file or it is already
   * given.
   */
  private final boolean mDoFetchId;
  /**
   * The file representing the document.
   */
  private final File mFile;
  /**
   * The id of the document if it is already set or {@link Document#NO_ID NO_ID}
   * if it should get fetched from the content.
   */
  private final int mId;
  /**
   * The position in bytes from the start of the file where the content for this
   * document begins.
   */
  private final long mLineStartPos;

  /**
   * Creates a new fix file lookup document which does not hold parameters like
   * id, name and description in memory but their position in the given file.
   * Thus its getter-methods need to lookup the data each time which makes the
   * class slow but therefore memory efficient.<br/>
   * This constructor assumes the id of the document is in the file.
   * 
   * @param file
   *          The file representing the document
   * @param lineStartPos
   *          The position in bytes from the start of the file where the content
   *          for this document begins
   * @param contentSeparator
   *          The separator of the content
   */
  public FixFileLookupDocument(final File file, final long lineStartPos,
      final String contentSeparator) {
    this(NO_ID, file, lineStartPos, contentSeparator, true);
  }

  /**
   * Creates a new fix file lookup document which does not hold parameters like
   * id, name and description in memory but their position in the given file.
   * Thus its getter-methods need to lookup the data each time which makes the
   * class slow but therefore memory efficient.<br/>
   * This constructor assumes the id of the document is not in the file.
   * 
   * @param id
   *          The id of the document
   * @param file
   *          The file representing the document
   * @param lineStartPos
   *          The position in bytes from the start of the file where the content
   *          for this document begins
   * @param contentSeparator
   *          The separator of the content
   */
  public FixFileLookupDocument(final int id, final File file,
      final long lineStartPos, final String contentSeparator) {
    this(id, file, lineStartPos, contentSeparator, false);
  }

  /**
   * Creates a new fix file lookup document which does not hold parameters like
   * id, name and description in memory but their position in the given file.
   * Thus its getter-methods need to lookup the data each time which makes the
   * class slow but therefore memory efficient.
   * 
   * @param id
   *          The id of the document
   * @param file
   *          The file representing the document
   * @param lineStartPos
   *          The position in bytes from the start of the file where the content
   *          for this document begins
   * @param contentSeparator
   *          The separator of the content
   * @param doFetchId
   *          Whether the method should fetch the id from the file or it is
   *          already given
   */
  private FixFileLookupDocument(final int id, final File file,
      final long lineStartPos, final String contentSeparator,
      final boolean doFetchId) {
    mFile = file;
    mLineStartPos = lineStartPos;
    mContentSeparator = contentSeparator;
    mDoFetchId = doFetchId;
    mId = id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.Document#getDescription()
   */
  @Override
  public String getDescription() {
    String[] content;
    try {
      content = fetchContent();
      return content[TEXT_FORMAT_DESCRIPTION_INDEX];
    } catch (IllegalArgumentException | IOException e) {
      throw new IllegalStateException(MSG_WRONG_TEXT_FORMAT);
    }
  }

  /**
   * Gets the file object of this document.
   * 
   * @return The file object of this document to get
   */
  public File getFile() {
    return mFile;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.Document#getId()
   */
  @Override
  public int getId() {
    if (mDoFetchId) {
      String[] content;
      try {
        content = fetchContent();
        return Integer.parseInt(content[TEXT_FORMAT_ID_INDEX]);
      } catch (IllegalArgumentException | IOException e) {
        throw new IllegalStateException(MSG_WRONG_TEXT_FORMAT);
      }
    } else {
      return mId;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.document.Document#getName()
   */
  @Override
  public String getName() {
    String[] content;
    try {
      content = fetchContent();
      return content[TEXT_FORMAT_NAME_INDEX];
    } catch (IllegalArgumentException | IOException e) {
      throw new IllegalStateException(MSG_WRONG_TEXT_FORMAT);
    }
  }

  /**
   * Fetches the content of this document by looking up in the given file.
   * 
   * @return The content of this document as
   *         <ul>
   *         <li>[0] - id</li>
   *         <li>[1] - name</li>
   *         <li>[2] - description</li>
   *         </ul>
   * @throws IOException
   *           If an I/O-Exception occurred
   * @throws IllegalArgumentException
   *           If the document is in the wrong format
   */
  private String[] fetchContent() throws IOException {
    final RandomAccessFile raf = new RandomAccessFile(mFile, FLAG_READ_ONLY);
    raf.seek(mLineStartPos);
    final String line = raf.readLine();
    raf.close();
    final String[] content = line.split(mContentSeparator);

    if ((mDoFetchId && content.length != 3)
        || (!mDoFetchId && content.length != 2)) {
      throw new IllegalArgumentException(MSG_WRONG_TEXT_FORMAT);
    }

    final String[] resultingContent;
    if (mDoFetchId) {
      resultingContent = content;
    } else {
      resultingContent = new String[3];
      resultingContent[TEXT_FORMAT_ID_INDEX] = NO_ID + "";
      resultingContent[TEXT_FORMAT_NAME_INDEX] =
          content[TEXT_FORMAT_NAME_INDEX - 1];
      resultingContent[TEXT_FORMAT_DESCRIPTION_INDEX] =
          content[TEXT_FORMAT_DESCRIPTION_INDEX - 1];
    }

    return resultingContent;
  }

}
