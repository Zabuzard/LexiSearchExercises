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
import java.util.Set;
import java.util.stream.Stream;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;

public final class DocumentSet implements Set<IDocument> {
  private static boolean alwaysSelfAssign = true;
  private static final String DOCUMENT_ID_PATTERN = "\\d+";
  private static final String TAB_VALUE = "\t";

  public static DocumentSet buildFromTextFile(final File textFile,
    final Charset charset, final String contentSeparator) throws IOException {
    Stream<String> stream = Files.lines(textFile.toPath(), charset);
    DocumentSet documents =
        buildFromTextIterator(stream.iterator(), contentSeparator);
    stream.close();
    return documents;
  }

  public static DocumentSet buildFromTextFileUtf8(final File textFile,
    final String contentSeparator) throws IOException {
    return buildFromTextFile(textFile, StandardCharsets.UTF_8,
        contentSeparator);
  }

  public static DocumentSet buildFromTextFileUtf8Tab(final File textFile)
    throws IOException {
    return buildFromTextFileUtf8(textFile, TAB_VALUE);
  }

  public static DocumentSet buildFromTextIterator(
    final Iterator<String> textIterator, final String contentSeparator) {
    final DocumentSet documents = new DocumentSet();

    int nextDocumentId = 0;
    while (textIterator.hasNext()) {
      String documentAsText = textIterator.next();
      final int idBegin = 0;
      final int idEnd = documentAsText.indexOf(contentSeparator);
      String potentialId = documentAsText.substring(idBegin, idEnd);
      if (alwaysSelfAssign || !potentialId.matches(DOCUMENT_ID_PATTERN)) {
        documentAsText = nextDocumentId + contentSeparator + documentAsText;
        nextDocumentId++;
      }

      documents.add(Document.buildFromText(documentAsText, contentSeparator));
    }

    return documents;
  }

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
      if (alwaysSelfAssign || !potentialId.matches(DOCUMENT_ID_PATTERN)) {
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

  public static DocumentSet buildLookupsFromFixTextFileUtf8(final File textFile,
    final String contentSeparator, final int newLineLength) throws IOException {
    return buildLookupsFromFixTextFile(textFile, StandardCharsets.UTF_8,
        contentSeparator, newLineLength);
  }

  public static DocumentSet buildLookupsFromFixTextFileUtf8Tab(
    final File textFile, final int newLineLength) throws IOException {
    return buildLookupsFromFixTextFileUtf8(textFile, TAB_VALUE, newLineLength);
  }

  private final HashMap<Integer, IDocument> mIdToDocument;

  public DocumentSet() {
    mIdToDocument = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#add(java.lang.Object)
   */
  @Override
  public boolean add(final IDocument e) {
    final int id = e.getId();
    final IDocument valueBefore = mIdToDocument.get(id);
    mIdToDocument.put(id, e);

    return valueBefore == null || !valueBefore.equals(e);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#addAll(java.util.Collection)
   */
  @Override
  public boolean addAll(final Collection<? extends IDocument> c) {
    boolean hasChanged = false;
    for (final IDocument document : c) {
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
      IDocument currentValue = mIdToDocument.get(((IDocument) o).getId());
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

  public IInvertedIndex<String> createInvertedIndex() {
    return InvertedIndexUtil.createFromWordRecords(mIdToDocument.values());
  }

  public IDocument getDocumentById(final int documentId) {
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
  public Iterator<IDocument> iterator() {
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
