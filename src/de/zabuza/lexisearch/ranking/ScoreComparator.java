package de.zabuza.lexisearch.ranking;

import java.util.Comparator;

import de.zabuza.lexisearch.indexing.Posting;

public final class ScoreComparator implements Comparator<Posting> {

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  @Override
  public int compare(final Posting o1, final Posting o2) {
    return Double.compare(o1.getScore(), o2.getScore());
  }

}
