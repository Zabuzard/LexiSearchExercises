package de.zabuza.lexisearch.ranking;

import java.util.List;

import de.zabuza.lexisearch.indexing.Posting;

public interface IRankingProvider<K> {
  double getRankingScore(final K key, final Posting posting);

  void sortPostingsByRank(List<Posting> postings);
}
