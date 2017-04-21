package de.zabuza.lexisearch.model.document;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link Document}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class DocumentTest {

  /**
   * Test method for {@link Document#buildFromText(String, String)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testBuildFromText() {
    final int id = 1;
    final String name = "a";
    final String description = "b";
    final String separator = "\t";
    final int anotherId = 2;
    final String anotherName = "1";
    final String anotherDescription = "b";
    final String anotherSeparator = " ";

    final Document document = Document.buildFromText(
        id + separator + name + separator + description, separator);
    final Document anotherDocument =
        Document.buildFromText(anotherId + anotherSeparator + anotherName
            + anotherSeparator + anotherDescription, anotherSeparator);

    Assert.assertEquals(name, document.getName());
    Assert.assertEquals(description, document.getDescription());

    Assert.assertEquals(anotherName, anotherDocument.getName());
    Assert.assertEquals(anotherDescription, anotherDocument.getDescription());
  }

  /**
   * Test method for {@link Document#Document()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testDocument() {
    final Document document = new Document();

    Assert.assertEquals(Document.NO_ID, document.getId());
    Assert.assertEquals("", document.getName());
    Assert.assertEquals("", document.getDescription());
  }

  /**
   * Test method for {@link Document#Document(int, String)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testDocumentIntString() {
    final int id = 1;
    final String name = "a";
    final Document document = new Document(id, name);

    Assert.assertEquals(id, document.getId());
    Assert.assertEquals(name, document.getName());
    Assert.assertEquals("", document.getDescription());
  }

  /**
   * Test method for {@link Document#Document(int, String, String)}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testDocumentIntStringString() {
    final int id = 1;
    final String name = "a";
    final String description = "b";
    final Document document = new Document(id, name, description);

    Assert.assertEquals(id, document.getId());
    Assert.assertEquals(name, document.getName());
    Assert.assertEquals(description, document.getDescription());
  }

  /**
   * Test method for {@link Document#getDescription()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetDescription() {
    final String description = "a";
    final String anotherDescription = "b";
    final Document document = new Document(0, "name", description);
    final Document anotherDocument =
        new Document(0, "name", anotherDescription);

    Assert.assertEquals(description, document.getDescription());
    Assert.assertEquals(anotherDescription, anotherDocument.getDescription());
  }

  /**
   * Test method for {@link Document#getId()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetId() {
    final int id = 1;
    final int anotherId = 2;
    final Document document = new Document(id, "name", "description");
    final Document anotherDocument =
        new Document(anotherId, "name", "description");

    Assert.assertEquals(id, document.getId());
    Assert.assertEquals(anotherId, anotherDocument.getId());
  }

  /**
   * Test method for {@link Document#getKeys()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetKeys() {
    final String name = "a b";
    final String description = "c d";
    final String anotherName = "1 2";
    final String anotherDescription = "3 4";
    final Document document = new Document(0, name, description);
    final Document anotherDocument =
        new Document(0, anotherName, anotherDescription);

    final String[] words = document.getKeys();
    final String[] anotherWords = anotherDocument.getKeys();
    Assert.assertArrayEquals(new String[] { "a", "b", "c", "d" }, words);
    Assert.assertArrayEquals(new String[] { "1", "2", "3", "4" }, anotherWords);
  }

  /**
   * Test method for {@link Document#getName()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetName() {
    final String name = "a";
    final String anotherName = "b";
    final Document document = new Document(0, name, "description");
    final Document anotherDocument =
        new Document(0, anotherName, "description");

    Assert.assertEquals(name, document.getName());
    Assert.assertEquals(anotherName, anotherDocument.getName());
  }

  /**
   * Test method for {@link Document#getRecordId()}.
   */
  @SuppressWarnings("static-method")
  @Test
  public void testGetRecordId() {
    final int id = 1;
    final int anotherId = 2;
    final Document document = new Document(id, "name", "description");
    final Document anotherDocument =
        new Document(anotherId, "name", "description");

    Assert.assertEquals(id, document.getRecordId());
    Assert.assertEquals(anotherId, anotherDocument.getRecordId());
  }

}
