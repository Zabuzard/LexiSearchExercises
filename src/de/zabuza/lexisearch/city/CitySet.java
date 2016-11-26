package de.zabuza.lexisearch.city;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyProvider;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.InvertedIndexUtil;

/**
 * Implementation of {@link Set} which holds {@link ICity} objects. It provides
 * fast access to its elements via their id, much like a {@link Map}. It also
 * provides static methods for building it from files in different formats.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class CitySet
    implements IKeyRecordSet<IKeyRecord<String>, String> {
  /**
   * Whether the static {@link CitySet} building methods should always self
   * assign IDs to the cities or they should use the IDs the cities have
   * specified, if they have.
   */
  private static boolean alwaysSelfAssignIds = true;
  /**
   * The pattern which matches a valid city id.
   */
  private static final String CITY_ID_PATTERN = "\\d+";
  /**
   * Constant for a comma value. Is used to separate names in some formats.
   */
  private static final String COMMA_VALUE = ", ";
  /**
   * Constant for a tab value. Is used to separate content in some formats.
   */
  private static final String TAB_VALUE = "\t";

  /**
   * Builds a {@link CitySet} from a text file. It needs to list cities line per
   * line where the format for a city is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <nameSeparator>}
   * state{@literal <contentSeparator>}
   * relevanceScore{@literal <contentSeparator>}
   * latitude{@literal <contentSeparator>}longitude</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textFile
   *          The text file to build the set from
   * @param charset
   *          The charset to use for decoding the text file
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @param nameSeparator
   *          The separator of the names
   * @param provider
   *          The key provider to use
   * @return The set of cities build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static CitySet buildFromTextFile(final File textFile,
      final Charset charset, final String contentSeparator,
      final String nameSeparator, final IKeyProvider<String, String> provider)
          throws IOException {
    final Stream<String> stream = Files.lines(textFile.toPath(), charset);
    final CitySet cities = buildFromTextIterator(stream.iterator(),
        contentSeparator, nameSeparator, provider);
    stream.close();
    return cities;
  }

  /**
   * Builds a {@link CitySet} from a text file. The method assumes the text file
   * is encoded in UTF-8. It needs to list cities line per line where the format
   * for a city is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <nameSeparator>}
   * state{@literal <contentSeparator>}
   * relevanceScore{@literal <contentSeparator>}
   * latitude{@literal <contentSeparator>}longitude</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textFile
   *          The text file to build the set from, assumed to be encoded in
   *          UTF-8
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @param nameSeparator
   *          The separator of the names
   * @param provider
   *          The key provider to use
   * @return The set of cities build from the given file
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  public static CitySet buildFromTextFileUtf8(final File textFile,
      final String contentSeparator, final String nameSeparator,
      final IKeyProvider<String, String> provider) throws IOException {
    return buildFromTextFile(textFile, StandardCharsets.UTF_8, contentSeparator,
        nameSeparator, provider);
  }

  /**
   * Builds a {@link CitySet} from a text file. The method assumes the text file
   * is encoded in UTF-8. It needs to list cities line per line where the format
   * for a city is:<br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <nameSeparator>}
   * state{@literal <contentSeparator>}
   * relevanceScore{@literal <contentSeparator>}
   * latitude{@literal <contentSeparator>}longitude</tt>
   * 
   * @param textFile
   *          The text file to build the set from, assumed to be encoded in
   *          UTF-8
   * @param provider
   *          The key provider to use
   * @return The set of cities build from the given file
   * @throws IOException
   *           IOException If an I/O-Exception occurred.
   */
  public static CitySet buildFromTextFileUtf8Tab(final File textFile,
      final IKeyProvider<String, String> provider) throws IOException {
    return buildFromTextFileUtf8(textFile, TAB_VALUE, COMMA_VALUE, provider);
  }

  /**
   * Builds a {@link CitySet} from a text iterator. It needs to list cities as
   * elements in the iterator where the format for a city is: <br/>
   * <tt>id{@literal <contentSeparator>}name{@literal <nameSeparator>}
   * state{@literal <contentSeparator>}
   * relevanceScore{@literal <contentSeparator>}
   * latitude{@literal <contentSeparator>}longitude</tt><br/>
   * where <tt>id</tt> is optional.
   * 
   * @param textIterator
   *          The text iterator to build the set from
   * @param contentSeparator
   *          The text used to separate the content in the format
   * @param nameSeparator
   *          The separator of the names
   * @param provider
   *          The key provider to use
   * @return The set of cities build from the given iterator
   */
  public static CitySet buildFromTextIterator(
      final Iterator<String> textIterator, final String contentSeparator,
      final String nameSeparator, final IKeyProvider<String, String> provider) {
    final CitySet cities = new CitySet();

    int nextCityId = 0;
    while (textIterator.hasNext()) {
      String cityAsText = textIterator.next();
      final int idBegin = 0;
      final int idEnd = cityAsText.indexOf(contentSeparator);
      final String potentialId = cityAsText.substring(idBegin, idEnd);
      if (alwaysSelfAssignIds || !potentialId.matches(CITY_ID_PATTERN)) {
        cityAsText = nextCityId + contentSeparator + cityAsText;
        nextCityId++;
      }

      cities.add(City.buildFromText(cityAsText, contentSeparator, nameSeparator,
          provider));
    }

    return cities;
  }

  /**
   * Structure that allows a fast access to cities by their id.
   */
  private final HashMap<Integer, IKeyRecord<String>> mIdToCity;

  /**
   * Creates a new empty city set.
   */
  public CitySet() {
    mIdToCity = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#add(java.lang.Object)
   */
  @Override
  public boolean add(final IKeyRecord<String> e) {
    final int id = e.getRecordId();
    final IKeyRecord<String> valueBefore = mIdToCity.get(id);
    mIdToCity.put(id, e);

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
    for (final IKeyRecord<String> city : c) {
      if (add(city)) {
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
    mIdToCity.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#contains(java.lang.Object)
   */
  @Override
  public boolean contains(final Object o) {
    if (o instanceof ICity) {
      IKeyRecord<String> currentValue = mIdToCity.get(((ICity) o).getId());
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
   * Creates an {@link IInvertedIndex} from this set of cities.
   * 
   * @return The inverted index working on this set of cities
   */
  public IInvertedIndex<String> createInvertedIndex() {
    return InvertedIndexUtil.createFromWords(mIdToCity.values());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyRecordSet#getKeyRecordById(int)
   */
  @Override
  public IKeyRecord<String> getKeyRecordById(final int cityId) {
    return mIdToCity.get(cityId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return mIdToCity.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#iterator()
   */
  @Override
  public Iterator<IKeyRecord<String>> iterator() {
    return mIdToCity.values().iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#remove(java.lang.Object)
   */
  @Override
  public boolean remove(final Object o) {
    if (contains(o)) {
      mIdToCity.remove(((ICity) o).getId());
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
    for (final Object city : c) {
      if (remove(city)) {
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
    return mIdToCity.values().retainAll(c);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#size()
   */
  @Override
  public int size() {
    return mIdToCity.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#toArray()
   */
  @Override
  public Object[] toArray() {
    return mIdToCity.values().toArray();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Set#toArray(java.lang.Object[])
   */
  @Override
  public <T> T[] toArray(final T[] a) {
    return mIdToCity.values().toArray(a);
  }

}