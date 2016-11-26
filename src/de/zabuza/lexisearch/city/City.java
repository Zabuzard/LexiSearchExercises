package de.zabuza.lexisearch.city;

import de.zabuza.lexisearch.indexing.IKeyProvider;

/**
 * Implementation for {@link ICity} which holds its parameters in memory.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class City implements ICity {

  /**
   * The default relevance score to use when no score was given.
   */
  private final static int DEFAULT_RELEVANCE_SCORE = 1;

  /**
   * Message which is shown when content should be parsed from a text format
   * that has an formatting error.
   */
  private static final String MSG_WRONG_TEXT_FORMAT =
      "The given city as text is in the wrong format.";
  /**
   * Index of the internal content information array where the id gets saved.
   */
  private static final int TEXT_FORMAT_ID_INDEX = 0;
  /**
   * Index of the internal content information array where the latitude
   * coordinate gets saved.
   */
  private static final int TEXT_FORMAT_LAT_INDEX = 3;
  /**
   * Index of the internal content information array where the longitude
   * coordinate gets saved.
   */
  private static final int TEXT_FORMAT_LONG_INDEX = 4;
  /**
   * Index of the internal content information array where the name content gets
   * saved.
   */
  private static final int TEXT_FORMAT_NAME_CONTENT_INDEX = 1;
  /**
   * Index of the internal content information array where the name gets saved.
   */
  private static final int TEXT_FORMAT_NAME_INDEX = 0;
  /**
   * Index of the internal content information array where the relevance score
   * gets saved.
   */
  private static final int TEXT_FORMAT_SCORE_INDEX = 2;
  /**
   * Index of the internal content information array where the state gets saved.
   */
  private static final int TEXT_FORMAT_STATE_INDEX = 1;

  /**
   * Builds a city representing the content given as text format.<br/>
   * The format is:
   * <tt>id{@literal <contentSeparator>}name{@literal <nameSeparator>}
   * state{@literal <contentSeparator>}
   * relevanceScore{@literal <contentSeparator>}
   * latitude{@literal <contentSeparator>}longitude</tt>.
   * 
   * @param cityAsText
   *          The city to represent given in a text format
   * @param contentSeparator
   *          The separator of the content
   * @param nameSeparator
   *          The separator of the names
   * @param keyProvider
   *          The key provider to use
   * @return The representing city object
   * @throws IllegalArgumentException
   *           If the city is in the wrong format
   */
  public static City buildFromText(final String cityAsText,
      final String contentSeparator, final String nameSeparator,
      IKeyProvider<String, String> keyProvider) {
    final String[] content = cityAsText.split(contentSeparator);

    if (content.length == 5) {
      final String[] nameContent =
          content[TEXT_FORMAT_NAME_CONTENT_INDEX].split(nameSeparator);
      if (nameContent.length == 2) {
        return new City(Integer.parseInt(content[TEXT_FORMAT_ID_INDEX]),
            nameContent[TEXT_FORMAT_NAME_INDEX],
            nameContent[TEXT_FORMAT_STATE_INDEX],
            Float.parseFloat(content[TEXT_FORMAT_LAT_INDEX]),
            Float.parseFloat(content[TEXT_FORMAT_LONG_INDEX]),
            Integer.parseInt(content[TEXT_FORMAT_SCORE_INDEX]), keyProvider);
      } else {
        throw new IllegalArgumentException(MSG_WRONG_TEXT_FORMAT);
      }
    } else {
      throw new IllegalArgumentException(MSG_WRONG_TEXT_FORMAT);
    }
  }

  /**
   * The id of the city.
   */
  private final int mId;
  /**
   * The key provider of the city.
   */
  private final IKeyProvider<String, String> mKeyProvider;
  /**
   * The latitude coordinate of the city.
   */
  private final float mLatitude;
  /**
   * The longitude coordinate of the city.
   */
  private final float mLongitude;
  /**
   * The name of the city.
   */
  private final String mName;
  /**
   * The relevance score of this city. The higher the more relevant is this
   * city.
   */
  private final int mRelevanceScore;
  /**
   * The state of the city.
   */
  private final String mState;

  /**
   * Creates a new city with the given parameters. The relevance score will be
   * set to a default value.
   * 
   * @param id
   *          The id of the city
   * @param name
   *          The name of the city
   * @param state
   *          The state of the city
   * @param latitude
   *          The latitude coordinate of the city
   * @param longitude
   *          The longitude coordinate of the city
   * @param keyProvider
   *          The key provider of the city
   */
  public City(final int id, final String name, final String state,
      final float latitude, final float longitude,
      final IKeyProvider<String, String> keyProvider) {
    this(id, name, state, latitude, longitude, DEFAULT_RELEVANCE_SCORE,
        keyProvider);
  }

  /**
   * Creates a new city with the given parameters.
   * 
   * @param id
   *          The id of the city
   * @param name
   *          The name of the city
   * @param state
   *          The state of the city
   * @param latitude
   *          The latitude coordinate of the city
   * @param longitude
   *          The longitude coordinate of the city
   * @param relevanceScore
   *          The relevance score of the city
   * @param keyProvider
   *          The key provider of the city
   */
  public City(final int id, final String name, final String state,
      final float latitude, final float longitude, final int relevanceScore,
      final IKeyProvider<String, String> keyProvider) {
    mId = id;
    mName = name;
    mState = state;
    mLatitude = latitude;
    mLongitude = longitude;
    mRelevanceScore = relevanceScore;
    mKeyProvider = keyProvider;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.city.ICity#getId()
   */
  @Override
  public int getId() {
    return mId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyRecord#getKeys()
   */
  @Override
  public String[] getKeys() {
    return mKeyProvider.getKeys(getRecordText());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.city.ICity#getLatitude()
   */
  @Override
  public float getLatitude() {
    return mLatitude;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.city.ICity#getLongitude()
   */
  @Override
  public float getLongitude() {
    return mLongitude;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.city.ICity#getName()
   */
  @Override
  public String getName() {
    return mName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyRecord#getRecordId()
   */
  @Override
  public int getRecordId() {
    return getId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.city.ICity#getRelevanceScore()
   */
  @Override
  public int getRelevanceScore() {
    return mRelevanceScore;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.indexing.IKeyRecord#getSize()
   */
  @Override
  public int getSize() {
    return mKeyProvider.getSize(getRecordText());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.zabuza.lexisearch.city.ICity#getState()
   */
  @Override
  public String getState() {
    return mState;
  }

  /**
   * Gets the text used for representing this city as record.
   * 
   * @return The text used for representing this city as record
   */
  private String getRecordText() {
    return getName() + getState();
  }

}
