package de.zabuza.lexisearch.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;

/**
 * Implementation of {@link Set} which holds {@link IDocument}s. It provides
 * fast access to its elements via their id, much like a {@link Map}. It also
 * provides static methods for building it from files in different formats.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class DocumentSet
    implements IKeyRecordSet<IKeyRecord<String>, String> {
  /**
   * Whether the static {@link DocumentSet} building methods should always self
   * assign IDs to the documents or they should use the IDs the documents have
   * specified, if they have.
   */
  private static boolean alwaysSelfAssignIds = true;
  /**
   * The pattern which matches a valid document id.
   */
  private static final String DOCUMENT_ID_PATTERN = "\\d+";
  /**
   * Constant for a tab value. Is used to separate content in some formats.
   */
  private static final String TAB_VALUE = "\t";

  /**
   * Builds a {@link DocumentSet} from a text file. It needs to list documents
   * line per line where the format for a document is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <contentSeparator>}
   * description</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textFile
   *          The text file to build the set from
   * @param charset
   *          The charset to use for decoding the text file
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @return The set of documents build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static DocumentSet buildFromTextFile(final File textFile,
      final Charset charset, final String contentSeparator) throws IOException {
    final Stream<String> stream = Files.lines(textFile.toPath(), charset);
    final DocumentSet documents =
        buildFromTextIterator(stream.iterator(), contentSeparator);
    stream.close();
    return documents;
  }

  /**
   * Builds a {@link DocumentSet} from a text file. The method assumes the text
   * file is encoded in UTF-8. It needs to list documents line per line where
   * the format for a document is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <contentSeparator>}
   * description</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textFile
   *          The text file to build the set from, assumed to be encoded in
   *          UTF-8
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @return The set of documents build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static DocumentSet buildFromTextFileUtf8(final File textFile,
      final String contentSeparator) throws IOException {
    return buildFromTextFile(textFile, StandardCharsets.UTF_8,
        contentSeparator);
  }

  /**
   * Builds a {@link DocumentSet} from a text file. The method assumes the text
   * file is encoded in UTF-8. It needs to list documents line per line where
   * the format for a document is:<br/>
   * <tt>id{@literal <\t>}name{@literal <\t>}description</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textFile
   *          The text file to build the set from, assumed to be encoded in
   *          UTF-8
   * @return The set of documents build from the given file
   * @throws IOException
   *           IOException If an I/O-Exception occurred.
   */
  public static DocumentSet buildFromTextFileUtf8Tab(final File textFile)
      throws IOException {
    return buildFromTextFileUtf8(textFile, TAB_VALUE);
  }

  /**
   * Builds a {@link DocumentSet} from a text iterator. It needs to list
   * documents as elements in the iterator where the format for a document is:
   * <br/>
   * <tt>id{@literal <\t>}name{@literal <\t>}description</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textIterator
   *          The text iterator to build the set from
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @return The set of documents build from the given iterator
   */
  public static DocumentSet buildFromTextIterator(
      final Iterator<String> textIterator, final String contentSeparator) {
    final DocumentSet documents = new DocumentSet();

    int nextDocumentId = 0;
    while (textIterator.hasNext()) {
      String documentAsText = textIterator.next();
      final int idBegin = 0;
      final int idEnd = documentAsText.indexOf(contentSeparator);
      final String potentialId = documentAsText.substring(idBegin, idEnd);
      if (alwaysSelfAssignIds || !potentialId.matches(DOCUMENT_ID_PATTERN)) {
        documentAsText = nextDocumentId + contentSeparator + documentAsText;
        nextDocumentId++;
      }

      documents.add(Document.buildFromText(documentAsText, contentSeparator));
    }

    return documents;
  }

  /**
   * Builds a {@link DocumentSet} from a text file. It needs to list documents
   * line per line where the format for a document is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <contentSeparator>}
   * description</tt><br/>
   * where <tt>id</tt> is optional.<br/>
   * This method uses {@link FixFileLookupDocument}s to represent the documents.
   * Those do not save the document information but their position in the file.
   * Thus they are slow but only consume very low memory.
   * 
   * @param textFile
   *          The text file to build the set from
   * @param charset
   *          The charset to use for decoding the text file
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @param newLineLength
   *          The length of the new line symbol in bytes
   * @return The set of documents build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static DocumentSet buildLookupsFromFixTextFile(final File textFile,
      final Charset charset, final String contentSeparator,
      final int newLineLength) throws IOException {
    final DocumentSet documents = new DocumentSet();

    final FileInputStream fileInputStream = new FileInputStream(textFile);
    final BufferedReader singleCharBufferedReader =
        new BufferedReader(new InputStreamReader(fileInputStream, charset));

    int nextDocumentId = 0;
    BigInteger offsetPosToLineStart = BigInteger.ZERO;
    while (singleCharBufferedReader.ready()) {
      String documentAsText = singleCharBufferedReader.readLine();
      if (documentAsText == null) {
        break;
      }

      final int idBegin = 0;
      final int idEnd = documentAsText.indexOf(contentSeparator);
      String potentialId = documentAsText.substring(idBegin, idEnd);
      int documentId = -1;
      if (alwaysSelfAssignIds || !potentialId.matches(DOCUMENT_ID_PATTERN)) {
        documentId = nextDocumentId;
        nextDocumentId++;
      }

      if (documentId == -1) {
        documents.add(new FixFileLookupDocument(textFile,
            offsetPosToLineStart.longValueExact(), contentSeparator));
      } else {
        documents.add(new FixFileLookupDocument(documentId, textFile,
            offsetPosToLineStart.longValueExact(), contentSeparator));
      }

      offsetPosToLineStart = offsetPosToLineStart.add(BigInteger
          .valueOf(documentAsText.getBytes(charset).length + newLineLength));
    }

    singleCharBufferedReader.close();
    return documents;
  }

  /**
   * Builds a {@link DocumentSet} from a text file. The method assumes the text
   * file as encoded in UTF-8. It needs to list documents line per line where
   * the format for a document is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <contentSeparator>}
   * description</tt><br/>
   * where <tt>id</tt> is optional.<br/>
   * This method uses {@link FixFileLookupDocument}s to represent the documents.
   * Those do not save the document information but their position in the file.
   * Thus they are slow but only consume very low memory.
   * 
   * @param textFile
   *          The text file to build the set from
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @param newLineLength
   *          The length of the new line symbol in bytes
   * @return The set of documents build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static DocumentSet buildLookupsFromFixTextFileUtf8(final File textFile,
      final String contentSeparator, final int newLineLength)
          throws IOException {
    return buildLookupsFromFixTextFile(textFile, StandardCharsets.UTF_8,
        contentSeparator, newLineLength);
  }

  /**
   * Builds a {@link DocumentSet} from a text file. The method assumes the text
   * file as encoded in UTF-8. It needs to list documents line per line where
   * the format for a document is:<br/>
   * <tt>id{@literal <\t>}name{@literal <\t>}description</tt> <br/>
   * where <tt>id</tt> is optional.<br/>
   * This method uses {@link FixFileLookupDocument}s to represent the documents.
   * Those do not save the document information but their position in the file.
   * Thus they are slow but only consume very low memory.
   * 
   * @param textFile
   *          The text file to build the set from
   * @param newLineLength
   *          The length of the new line symbol in bytes
   * @return The set of documents build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static DocumentSet buildLookupsFromFixTextFileUtf8Tab(
      final File textFile, final int newLineLength) throws IOException {
    return buildLookupsFromFixTextFileUtf8(textFile, TAB_VALUE, newLineLength);
  }

  /**
   * Structure that allows a fast access to documents by their id.
   */
  private final HashMap<Integer, IKeyRecord<String>> mIdToDocument;

  /**
   * Creates a new empty document set.
   */
  public DocumentSet() {
    mIdToDocument = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#add(java.lang.Object)
   */
  @Override
  public boolean add(final IKeyRecord<String> e) {
    final int id = e.getRecordId();
    final IKeyRecord<String> valueBefore = mIdToDocument.get(id);
    mIdToDocument.put(id, e);

    return valueBefore == null || !valueBefore.equals(e);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#addAll(java.util.Collection)
   */
  @Override
  public boolean addAll(final Collection<? extends IKeyRecord<String>> c) {
    boolean hasChanged = false;
    for (final IKeyRecord<String> document : c) {
      if (add(document)) {
        hasChanged = true;
      }
    }
    return hasChanged;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#clear()
   */
  @Override
  public void clear() {
    mIdToDocument.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#contains(java.lang.Object)
   */
  @Override
  public boolean contains(final Object o) {
    if (o instanceof IDocument) {
      IKeyRecord<String> currentValue =
          mIdToDocument.get(((IDocument) o).getId());
      return currentValue != null && currentValue.equals(o);
    } else {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#containsAll(java.util.Collection)
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Creates an {@link IInvertedIndex} from this set of documents.
   * 
   * @return The inverted index working on this set of documents
   */
  public IInvertedIndex<String> createInvertedIndex() {
    return InvertedIndexUtil.createFromWords(mIdToDocument.values());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyRecordSet#getKeyRecordById(int)
   */
  @Override
  public IKeyRecord<String> getKeyRecordById(final int documentId) {
    return mIdToDocument.get(documentId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return mIdToDocument.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#iterator()
   */
  @Override
  public Iterator<IKeyRecord<String>> iterator() {
    return mIdToDocument.values().iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#remove(java.lang.Object)
   */
  @Override
  public boolean remove(final Object o) {
    if (contains(o)) {
      mIdToDocument.remove(((IDocument) o).getId());
      return true;
    } else {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean hasChanged = false;
    for (final Object document : c) {
      if (remove(document)) {
        hasChanged = true;
      }
    }
    return hasChanged;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#retainAll(java.util.Collection)
   */
  @Override
  public boolean retainAll(final Collection<?> c) {
    return mIdToDocument.values().retainAll(c);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#size()
   */
  @Override
  public int size() {
    return mIdToDocument.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#toArray()
   */
  @Override
  public Object[] toArray() {
    return mIdToDocument.values().toArray();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#toArray(java.lang.Object[])
   */
  @Override
  public <T> T[] toArray(final T[] a) {
    return mIdToDocument.values().toArray(a);
  }

}
