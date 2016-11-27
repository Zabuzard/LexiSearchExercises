package de.zabuza.lexisearch.indexing;

import de.zabuza.lexisearch.util.MathUtil;

/**
 * Class for postings. They contain the id of the record object they correspond
 * to. They can also have scores which determine their relevance to other
 * postings.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class Posting implements Comparable<Posting> {
  /**
   * The default score of postings.
   */
  public static final int DEFAULT_SCORE = 0;
  /**
   * The default term frequency of postings.
   */
  public static final int DEFAULT_TERM_FREQUENCY = 1;

  /**
   * The id of the record the posting belongs to.
   */
  private final int mId;

  /**
   * The score of this posting which reflects its relevance to the containing
   * inverted list.
   */
  private double mScore;
  /**
   * The term frequency of the posting.
   */
  private int mTermFrequency;

  /**
   * Creates a new posting with a given id of the record it belongs to, a term
   * frequency of {@link #DEFAULT_TERM_FREQUENCY} and a score of
   * {@link #DEFAULT_SCORE}.
   * 
   * @param id
   *          The id of record the posting belongs to
   */
  public Posting(final int id) {
    this(id, DEFAULT_TERM_FREQUENCY, DEFAULT_SCORE);
  }

  /**
   * Creates a new posting with a given id of the record it belongs to, a term
   * frequency and a score of {@link #DEFAULT_SCORE}.
   * 
   * @param id
   *          The id of the record the posting belongs to
   * @param termFrequency
   *          The term frequency of the posting
   */
  public Posting(final int id, final int termFrequency) {
    this(id, termFrequency, DEFAULT_SCORE);
  }

  /**
   * Creates a new posting with a given id of the record it belongs to, a term
   * frequency and a score.
   * 
   * @param id
   *          The id of the record the posting belongs to
   * @param termFrequency
   *          The term frequency of the posting
   * @param score
   *          The score of the posting which reflects its relevance to the
   *          containing inverted list.
   */
  public Posting(final int id, final int termFrequency, final double score) {
    mId = id;
    mTermFrequency = termFrequency;
    mScore = score;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Posting o) {
    return Integer.compare(getId(), o.getId());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Posting)) {
      return false;
    }
    Posting other = (Posting) obj;
    if (mId != other.mId) {
      return false;
    }
    return true;
  }

  /**
   * Gets the id of the record belonging to this posting.
   * 
   * @return The id of the record belonging to this posting
   */
  public int getId() {
    return mId;
  }

  /**
   * Gets the score of this posting which reflects its relevance to the
   * containing inverted list.
   * 
   * @return The score of this posting
   */
  public double getScore() {
    return mScore;
  }

  /**
   * Gets the term frequency of the posting.
   * 
   * @return The term frequency of the posting
   */
  public int getTermFrequency() {
    return mTermFrequency;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + mId;
    return result;
  }

  /**
   * Increases the term frequency of this posting by one.
   */
  public void increaseTermFrequency() {
    mTermFrequency++;
  }

  /**
   * Sets the score of this posting which reflects its relevance to the
   * containing inverted list.
   * 
   * @param score
   *          The score to set
   */
  public void setScore(final double score) {
    mScore = score;
  }

  /**
   * Sets the term frequency of the posting.
   * 
   * @param termFrequency
   *          The term frequency to set
   */
  public void setTermFrequency(final int termFrequency) {
    mTermFrequency = termFrequency;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "[id=" + mId + ",tf=" + mTermFrequency + ",sc="
        + MathUtil.formatDecimalTwo(mScore) + "]";
  }
}
