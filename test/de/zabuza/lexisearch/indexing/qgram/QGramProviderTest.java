package de.zabuza.lexisearch.indexing.qgram;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link QGramProvider}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class QGramProviderTest {

  /**
   * Test method for {@link QGramProvider#getKeys(String)}.
   */
  @Test
  public void testGetKeys() {
    final QGramProvider provider = new QGramProvider(3, '$');
    final String[] qGrams = provider.getKeys("abc");
    Assert.assertEquals(3, qGrams.length);
    Assert.assertEquals("$$a", qGrams[0]);
    Assert.assertEquals("$ab", qGrams[1]);
    Assert.assertEquals("abc", qGrams[2]);

    final String[] longerQGrams = provider.getKeys("abcdef");
    Assert.assertEquals(6, longerQGrams.length);
    Assert.assertEquals("$$a", longerQGrams[0]);
    Assert.assertEquals("$ab", longerQGrams[1]);
    Assert.assertEquals("abc", longerQGrams[2]);
    Assert.assertEquals("bcd", longerQGrams[3]);
    Assert.assertEquals("cde", longerQGrams[4]);
    Assert.assertEquals("def", longerQGrams[5]);

    final QGramProvider anotherProvider = new QGramProvider(2, '$');
    final String[] anotherQGrams = anotherProvider.getKeys("abc");
    Assert.assertEquals(3, anotherQGrams.length);
    Assert.assertEquals("$a", anotherQGrams[0]);
    Assert.assertEquals("ab", anotherQGrams[1]);
    Assert.assertEquals("bc", anotherQGrams[2]);

    final String[] anotherLongerQGrams = anotherProvider.getKeys("abcdef");
    Assert.assertEquals(6, anotherLongerQGrams.length);
    Assert.assertEquals("$a", anotherLongerQGrams[0]);
    Assert.assertEquals("ab", anotherLongerQGrams[1]);
    Assert.assertEquals("bc", anotherLongerQGrams[2]);
    Assert.assertEquals("cd", anotherLongerQGrams[3]);
    Assert.assertEquals("de", anotherLongerQGrams[4]);
    Assert.assertEquals("ef", anotherLongerQGrams[5]);

    // Test required by lecture
    final String[] testLongerQGrams = provider.getKeys("lirum");
    Assert.assertEquals(5, testLongerQGrams.length);
    Assert.assertEquals("$$l", testLongerQGrams[0]);
    Assert.assertEquals("$li", testLongerQGrams[1]);
    Assert.assertEquals("lir", testLongerQGrams[2]);
    Assert.assertEquals("iru", testLongerQGrams[3]);
    Assert.assertEquals("rum", testLongerQGrams[4]);
  }

  /**
   * Test method for {@link QGramProvider#getQParameter()}.
   */
  @Test
  public void testGetQParameter() {
    final QGramProvider provider = new QGramProvider(3);
    Assert.assertEquals(3, provider.getQParameter());

    final QGramProvider anotherProvider = new QGramProvider(2);
    Assert.assertEquals(2, anotherProvider.getQParameter());
  }

  /**
   * Test method for {@link QGramProvider#getSize(String)}.
   */
  @Test
  public void testGetSize() {
    final QGramProvider provider = new QGramProvider(3);

    Assert.assertEquals(3, provider.getQParameter());
    Assert.assertEquals(3, provider.getSize("abc"));
    Assert.assertEquals(6, provider.getSize("abcdef"));

    final QGramProvider anotherProvider = new QGramProvider(2);

    Assert.assertEquals(2, anotherProvider.getQParameter());
    Assert.assertEquals(3, anotherProvider.getSize("abc"));
    Assert.assertEquals(6, anotherProvider.getSize("abcdef"));
  }

  /**
   * Test method for {@link QGramProvider#normalizeRecord(String)}.
   */
  @Test
  public void testNormalizeRecord() {
    Assert.assertEquals("", QGramProvider.normalizeRecord(""));
    Assert.assertEquals("abcdefg", QGramProvider.normalizeRecord("ABcDeFG"));
    Assert.assertEquals("abcdefg",
        QGramProvider.normalizeRecord(" *A+b, CdE-,fG?  "));
  }

  /**
   * Test method for {@link QGramProvider#QGramProvider(int)}.
   */
  @Test
  public void testQGramProviderInt() {
    final QGramProvider provider = new QGramProvider(3);

    Assert.assertEquals(3, provider.getQParameter());
    Assert.assertEquals(3, provider.getSize("abc"));
    Assert.assertEquals(6, provider.getSize("abcdef"));

    final QGramProvider anotherProvider = new QGramProvider(2);

    Assert.assertEquals(2, anotherProvider.getQParameter());
    Assert.assertEquals(3, anotherProvider.getSize("abc"));
    Assert.assertEquals(6, anotherProvider.getSize("abcdef"));
  }

  /**
   * Test method for {@link QGramProvider#QGramProvider(int, char)}.
   */
  @Test
  public void testQGramProviderIntChar() {
    final QGramProvider provider = new QGramProvider(3, '-');

    final String[] qGrams = provider.getKeys("abc");
    Assert.assertEquals("--a", qGrams[0]);
    Assert.assertEquals("-ab", qGrams[1]);
    Assert.assertEquals("abc", qGrams[2]);

    final QGramProvider anotherProvider = new QGramProvider(3, 'x');

    final String[] anotherQGrams = anotherProvider.getKeys("abc");
    Assert.assertEquals("xxa", anotherQGrams[0]);
    Assert.assertEquals("xab", anotherQGrams[1]);
    Assert.assertEquals("abc", anotherQGrams[2]);
  }

}
