package de.zabuza.lexisearch.ranking;

/**
 * Interface for records which provide a score. This score can be used for
 * ranking, for example in {@link PostingBeforeRecordRanking}.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public interface IRecordScoreProvider {
  /**
   * Gets the score of the record.
   * 
   * @return The score of the record
   */
  int getScore();
}
