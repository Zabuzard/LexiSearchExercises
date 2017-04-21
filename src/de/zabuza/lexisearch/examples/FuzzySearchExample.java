package de.zabuza.lexisearch.examples;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.CitySet;
import de.zabuza.lexisearch.model.city.ICity;
import de.zabuza.lexisearch.queries.FuzzyPrefixQuery;
import de.zabuza.lexisearch.ranking.PostingBeforeRecordRanking;

/**
 * Example which demonstrates the usage of {@link CitySet}s,
 * {@link IInvertedIndex} and {@link FuzzyPrefixQuery}. It accepts a content
 * file in a given format or uses a small sample file. Then it constructs a
 * {@link CitySet} which represents the content and builds a corresponding
 * {@link IInvertedIndex} by building a {@link FuzzyPrefixQuery} object. After
 * that it starts a search service where the user types in keywords. The service
 * then lists all cities that match all given keywords in the order of their
 * edit distance and relevance. For example a keyword like "Freyb" will likely
 * return "Freiburg" as city.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class FuzzySearchExample {
  /**
   * Text used to separate keywords.
   */
  private static final String KEYWORD_SEPARATOR = ";";
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
   * {@link IInvertedIndex} and {@link FuzzyPrefixQuery}. It accepts a content
   * file in a given format or uses a small sample file. Then it constructs a
   * {@link CitySet} which represents the content and builds a corresponding
   * {@link IInvertedIndex} by building a {@link FuzzyPrefixQuery} object. After
   * that it starts a search service where the user types in keywords. The
   * service then lists all cities that match all given keywords in the order of
   * their edit distance and relevance. For example a keyword like "Freyb" will
   * likely return "Freiburg" as city.
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
    final QGramProvider qGramProvider = new QGramProvider(qParameter);
    final CitySet cities =
        CitySet.buildFromTextFileUtf8Tab(file, qGramProvider);

    System.out.println("Creating fuzzy prefix query and ranking...");
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();
    final FuzzyPrefixQuery<IKeyRecord<String>> fuzzyQuery =
        new FuzzyPrefixQuery<>(cities, qGramProvider, ranking);

    System.out.println("Starting query service.");
    boolean stopService = false;

    try (final Scanner scanner = new Scanner(System.in)) {
      while (!stopService) {
        System.out.println(">Type your query. Type an empty text to stop.");
        final String query = scanner.nextLine().toLowerCase();

        if (query.trim().isEmpty()) {
          stopService = true;
        } else {
          final long queryStartTime = System.currentTimeMillis();

          final String[] keywords = query.split(KEYWORD_SEPARATOR);
          final List<Posting> queryResults =
              fuzzyQuery.searchOr(Arrays.asList(keywords));

          final long queryEndTime = System.currentTimeMillis();
          final long queryTime = queryEndTime - queryStartTime;

          System.out.println("Matching postings are (" + queryResults.size()
              + "): " + queryResults);

          System.out.println("Some of them are:");
          final int maximalPostingsForPreview = 5;
          int postingsShown = 0;
          for (final Posting posting : queryResults) {
            final int recordId = posting.getId();
            final ICity city = (ICity) cities.getKeyRecordById(recordId);
            System.out.println("\t" + (int) posting.getScore() + "\t"
                + city.getScore() + "\t" + city.getName());

            postingsShown++;
            if (postingsShown >= maximalPostingsForPreview) {
              break;
            }
          }
          System.out.println("Query took: " + queryTime + "ms");
        }
      }
    }

    System.out.println("Terminated.");
  }

  /**
   * Utility class. No implementation.
   */
  private FuzzySearchExample() {

  }
}
