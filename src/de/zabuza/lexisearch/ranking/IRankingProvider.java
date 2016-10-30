package de.zabuza.lexisearch.ranking;

import java.util.List;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.IKeyRecordSet;
import de.zabuza.lexisearch.indexing.Posting;

public interface IRankingProvider<K> {
  IInvertedIndex<K> getInvertedIndex();

  IKeyRecordSet<IKeyRecord<K>, K> getKeyRecords();

  double getRankingScore(final K key, final Posting posting);

  void setRankingScoreToIndex();

  void sortPostingsByRank(final List<Posting> postings);

  void takeSnapshot(final IInvertedIndex<K> invertedIndex,
    final IKeyRecordSet<IKeyRecord<K>, K> keyRecords);
}
