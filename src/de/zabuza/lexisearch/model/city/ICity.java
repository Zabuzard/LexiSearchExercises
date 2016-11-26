package de.zabuza.lexisearch.model.city;

import de.zabuza.lexisearch.indexing.IKeyRecord;

/**
 * Interface for cities.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public interface ICity extends IKeyRecord<String> {
  /**
   * Gets the id of the city.
   * 
   * @return The id of the city to get
   */
  int getId();

  /**
   * Gets the latitude coordinate of this city
   * 
   * @return The latitude coordinate of this city
   */
  float getLatitude();

  /**
   * Gets the longitude coordinate of this city
   * 
   * @return The longitude coordinate of this city
   */
  float getLongitude();

  /**
   * Gets the relevance score of this city. The higher the more relevant is this
   * city.
   * 
   * @return The relevance score of this city
   */
  int getRelevanceScore();
}
