package de.zabuza.lexisearch.model.document;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;

/**
 * Test for {@link DocumentSet}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class DocumentSetTest {

  /**
   * Test method for {@link DocumentSet#add(IKeyRecord)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testAdd() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    Assert.assertTrue(documentSet.add(document));
    Assert.assertEquals(1, documentSet.size());
    Assert.assertFalse(documentSet.add(document));
    Assert.assertEquals(1, documentSet.size());

    final Document anotherDocument = new Document(2, "1", "2");
    Assert.assertTrue(documentSet.add(anotherDocument));
    Assert.assertEquals(2, documentSet.size());
  }

  /**
   * Test method for {@link DocumentSet#addAll(Collection)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testAddAll() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    final Document anotherDocument = new Document(2, "1", "2");

    List<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);
    documents.add(document);

    Assert.assertTrue(documentSet.addAll(documents));
    Assert.assertEquals(2, documentSet.size());

    Assert.assertTrue(documentSet.contains(document));
    Assert.assertTrue(documentSet.contains(anotherDocument));
  }

  /**
   * Test method for
   * {@link DocumentSet#buildFromTextIterator(Iterator, String)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testBuildFromTextIterator() {
    final String separator = "\t";
    final String firstDocument = "a" + separator + "b";
    final String secondDocument = "1" + separator + "b";
    final List<String> content = new LinkedList<>();
    content.add(firstDocument);
    content.add(secondDocument);

    final DocumentSet documentSet =
        DocumentSet.buildFromTextIterator(content.iterator(), separator);
    Assert.assertEquals(2, documentSet.size());
  }

  /**
   * Test method for {@link DocumentSet#clear()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testClear() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    final Document anotherDocument = new Document(2, "1", "2");

    List<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);
    documents.add(document);

    Assert.assertTrue(documentSet.addAll(documents));
    Assert.assertEquals(2, documentSet.size());

    documentSet.clear();

    Assert.assertEquals(0, documentSet.size());
  }

  /**
   * Test method for {@link DocumentSet#contains(Object)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testContains() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    final Document anotherDocument = new Document(2, "1", "2");

    List<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);
    documents.add(document);

    Assert.assertTrue(documentSet.addAll(documents));
    Assert.assertEquals(2, documentSet.size());

    Assert.assertTrue(documentSet.contains(document));
    Assert.assertTrue(documentSet.contains(anotherDocument));
  }

  /**
   * Test method for {@link DocumentSet#containsAll(Collection)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testContainsAll() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    final Document anotherDocument = new Document(2, "1", "2");

    List<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);
    documents.add(document);

    Assert.assertTrue(documentSet.addAll(documents));
    Assert.assertEquals(2, documentSet.size());

    Assert.assertTrue(documentSet.containsAll(documents));
  }

  /**
   * Test method for {@link DocumentSet#createInvertedIndex()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testCreateInvertedIndex() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);
    final Document anotherDocument = new Document(2, "1", "b");
    documentSet.add(anotherDocument);

    final IInvertedIndex<String> invertedIndex =
        documentSet.createInvertedIndex();

    Assert.assertEquals(1, invertedIndex.getRecords("a").getSize());
    Assert.assertEquals(2, invertedIndex.getRecords("b").getSize());
  }

  /**
   * Test method for {@link DocumentSet#DocumentSet()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testDocumentSet() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);
    Assert.assertEquals(1, documentSet.size());
    Assert.assertTrue(documentSet.contains(document));
  }

  /**
   * Test method for {@link DocumentSet#getKeyRecordById(int)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetKeyRecordById() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);

    final Document anotherDocument = new Document(2, "1", "2");
    documentSet.add(anotherDocument);

    Assert.assertEquals(2, documentSet.size());

    Assert.assertEquals(document, documentSet.getKeyRecordById(1));
    Assert.assertEquals(anotherDocument, documentSet.getKeyRecordById(2));
    Assert.assertNull(documentSet.getKeyRecordById(3));
  }

  /**
   * Test method for {@link DocumentSet#isEmpty()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testIsEmpty() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertTrue(documentSet.isEmpty());

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);

    final Document anotherDocument = new Document(2, "1", "2");
    documentSet.add(anotherDocument);
    Assert.assertFalse(documentSet.isEmpty());

    documentSet.remove(anotherDocument);
    Assert.assertFalse(documentSet.isEmpty());

    documentSet.remove(document);
    Assert.assertTrue(documentSet.isEmpty());
  }

  /**
   * Test method for {@link DocumentSet#iterator()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testIterator() {
    final DocumentSet documentSet = new DocumentSet();

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);

    final Document anotherDocument = new Document(2, "1", "2");
    documentSet.add(anotherDocument);

    final Iterator<IKeyRecord<String>> iter = documentSet.iterator();
    final IKeyRecord<String> firstDocument = iter.next();
    final IKeyRecord<String> secondDocument = iter.next();
    Assert.assertEquals(document, firstDocument);
    Assert.assertEquals(anotherDocument, secondDocument);
    Assert.assertFalse(iter.hasNext());
  }

  /**
   * Test method for {@link DocumentSet#remove(Object)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testRemove() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);

    final Document anotherDocument = new Document(2, "1", "2");
    documentSet.add(anotherDocument);
    Assert.assertEquals(2, documentSet.size());

    Assert.assertTrue(documentSet.remove(anotherDocument));
    Assert.assertEquals(1, documentSet.size());

    Assert.assertTrue(documentSet.remove(document));
    Assert.assertEquals(0, documentSet.size());

    Assert.assertFalse(documentSet.remove(document));
  }

  /**
   * Test method for {@link DocumentSet#removeAll(Collection)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testRemoveAll() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    final Document anotherDocument = new Document(2, "1", "2");
    List<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(anotherDocument);
    documents.add(document);

    documentSet.add(document);
    documentSet.add(anotherDocument);

    Assert.assertEquals(2, documentSet.size());

    Assert.assertTrue(documentSet.removeAll(documents));
    Assert.assertEquals(0, documentSet.size());
  }

  /**
   * Test method for {@link DocumentSet#retainAll(Collection)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testRetainAll() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    final Document anotherDocument = new Document(2, "1", "2");
    final List<Document> documents = new LinkedList<>();
    documents.add(document);
    documents.add(document);

    documentSet.add(document);
    documentSet.add(anotherDocument);

    Assert.assertEquals(2, documentSet.size());

    Assert.assertTrue(documentSet.retainAll(documents));
    Assert.assertEquals(1, documentSet.size());
    Assert.assertEquals(document, documentSet.getKeyRecordById(1));
  }

  /**
   * Test method for {@link DocumentSet#size()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testSize() {
    final DocumentSet documentSet = new DocumentSet();
    Assert.assertEquals(0, documentSet.size());

    final Document document = new Document(1, "a", "b");
    documentSet.add(document);
    Assert.assertEquals(1, documentSet.size());
    documentSet.add(document);
    Assert.assertEquals(1, documentSet.size());

    final Document anotherDocument = new Document(2, "1", "2");
    documentSet.add(anotherDocument);
    Assert.assertEquals(2, documentSet.size());
  }

}
