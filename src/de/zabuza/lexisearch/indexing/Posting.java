package de.zabuza.lexisearch.indexing;

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
   * The default term frequency of postings.
   */
  public static final int DEFAULT_TERM_FREQUENCY = 1;

  /**
   * The id of the record the posting belongs to.
   */
  private final int mId;

  /**
   * The term frequency of the posting.
   */
  private int mTermFrequency;

  /**
   * Creates a new posting with a given id of the record it belongs to and a
   * term frequency of {@link #DEFAULT_TERM_FREQUENCY}.
   * 
   * @param id
   *          The id of record the posting belongs to
   */
  public Posting(final int id) {
    this(id, DEFAULT_TERM_FREQUENCY);
  }

  /**
   * Creates a new posting with a given id of the record it belongs to and a
   * term frequency.
   * 
   * @param id
   *          The id of the record the posting belongs to
   * @param termFrequency
   *          The term frequency of the posting
   */
  public Posting(final int id, final int termFrequency) {
    mId = id;
    mTermFrequency = termFrequency;
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
    return "[id=" + mId + ", tf=" + mTermFrequency + "]";
  }
}
