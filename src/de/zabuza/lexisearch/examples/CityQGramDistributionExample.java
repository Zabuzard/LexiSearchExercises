package de.zabuza.lexisearch.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.CitySet;

/**
 * Example which demonstrates the usage of {@link CitySet}s,
 * {@link IInvertedIndex} and {@link QGramProvider}. It accepts a content file
 * in a given format or uses a small sample file. Then it constructs a
 * {@link CitySet} which represents the content and builds a corresponding
 * {@link IInvertedIndex}. After that it lists all q-Grams of the content file
 * sorted by their hits in cities.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class CityQGramDistributionExample {

  /**
   * Message shown when using the {@link #main(String[])} with the wrong amount
   * of arguments.
   */
  private static final String MSG_WRONG_ARGUMENT_LENGTH =
      "Wrong length of arguments.";

  /**
   * Path to the default sample file.
   */
  private static final String PATH_DEFAULT_EXAMPLE_FILE =
      "res/examples/cities.txt";

  /**
   * Example which demonstrates the usage of {@link CitySet}s,
   * {@link IInvertedIndex} and {@link QGramProvider}. It accepts a content file
   * in a given format or uses a small sample file. Then it constructs a
   * {@link CitySet} which represents the content and builds a corresponding
   * {@link IInvertedIndex}. After that it lists all q-Grams of the content file
   * sorted by their hits in cities.
   * 
   * @param args
   *          The first argument specifies the path to the file to use, else a
   *          sample file gets used.
   * @throws IOException
   *           If an I/O-Exception occurred
   */
  public static void main(final String[] args) throws IOException {
    final File file;
    if (args.length == 1) {
      file = new File(args[0]);
    } else if (args.length == 0) {
      file = new File(PATH_DEFAULT_EXAMPLE_FILE);
    } else {
      throw new IllegalArgumentException(MSG_WRONG_ARGUMENT_LENGTH);
    }

    System.out.println("Fetching cities...");
    final int qParameter = 3;
    QGramProvider qGramProvider = new QGramProvider(qParameter);
    final CitySet cities =
        CitySet.buildFromTextFileUtf8Tab(file, qGramProvider);

    System.out.println("Creating inverted index...");
    final long indexBuildStartTime = System.currentTimeMillis();
    final IInvertedIndex<String> invertedIndex = cities.createInvertedIndex();
    final long indexBuildEndTime = System.currentTimeMillis();
    final long indexBuildTime = indexBuildEndTime - indexBuildStartTime;

    System.out.println("Post processing...");
    final HashMap<Integer, List<String>> hitsToQGram =
        new HashMap<Integer, List<String>>();

    for (final String qGram : invertedIndex.getKeys()) {
      int hits = 0;
      for (Posting cityPosting : invertedIndex.getRecords(qGram)
          .getPostings()) {
        hits += cityPosting.getTermFrequency();
      }

      List<String> qGrams = hitsToQGram.get(hits);
      if (qGrams == null) {
        qGrams = new LinkedList<>();
      }
      qGrams.add(qGram);
      hitsToQGram.put(hits, qGrams);
    }

    final List<Integer> hits = new ArrayList<>(hitsToQGram.keySet());
    Collections.sort(hits);

    System.out.println("Hit ranking:");
    for (final Integer hit : hits) {
      for (final String qGram : hitsToQGram.get(hit)) {
        System.out.println(qGram + " : " + hit);
      }
    }

    System.out.println("Time index building: " + indexBuildTime + "ms");
    System.out.println("Terminated.");
  }

  /**
   * Utility class. No implementation.
   */
  private CityQGramDistributionExample() {

  }

}
