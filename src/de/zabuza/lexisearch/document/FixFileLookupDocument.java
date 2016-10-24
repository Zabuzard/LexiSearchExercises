package de.zabuza.lexisearch.document;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;

public class FixFileLookupDocument extends Document {

  private static final String FLAG_READ_ONLY = "r";

  public static FixFileLookupDocument buildFromFixFileLineText(
    final Optional<Integer> id, final File file, final long lineStartPos,
    final String contentSeparator) throws IllegalArgumentException {
    if (id.isPresent()) {
      return new FixFileLookupDocument(id.get(), file, lineStartPos,
          contentSeparator);
    } else {
      return new FixFileLookupDocument(file, lineStartPos, contentSeparator);
    }
  }

  private final String mContentSeparator;
  private final boolean mDoFetchId;
  private final File mFile;
  private final int mId;
  private final long mLineStartPos;

  public FixFileLookupDocument(final File file, final long lineStartPos,
      final String contentSeparator) {
    this(NO_ID, file, lineStartPos, contentSeparator, true);
  }

  public FixFileLookupDocument(final int id, final File file,
      final long lineStartPos, final String contentSeparator) {
    this(id, file, lineStartPos, contentSeparator, false);
  }

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

  private String[] fetchContent() throws IOException, IllegalArgumentException {
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
