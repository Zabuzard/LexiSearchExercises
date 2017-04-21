package de.zabuza.lexisearch.examples;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import de.zabuza.lexisearch.indexing.IInvertedIndex;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.model.document.DocumentSet;
import de.zabuza.lexisearch.model.document.IDocument;
import de.zabuza.lexisearch.queries.KeywordQuery;
import de.zabuza.lexisearch.ranking.Bm25Ranking;

/**
 * Example which demonstrates the usage of {@link DocumentSet}s,
 * {@link IInvertedIndex} and {@link KeywordQuery}. It accepts a content file in
 * a given format or uses a small sample file. Then it constructs a
 * {@link DocumentSet} which represents the content and builds a corresponding
 * {@link IInvertedIndex} by building a {@link KeywordQuery} object. After that
 * it starts a search service where the user types in keywords. The service then
 * lists all documents that contain all given keywords in the order of their
 * BM25 ranking.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class KeywordQueryExample {

  /**
   * Text used to separate keywords.
   */
  private static final String KEYWORD_SEPARATOR = " ";
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
      "res/examples/movies.txt";

  /**
   * Example which demonstrates the usage of {@link DocumentSet}s,
   * {@link IInvertedIndex} and {@link KeywordQuery}. It accepts a content file
   * in a given format or uses a small sample file. Then it constructs a
   * {@link DocumentSet} which represents the content and builds a corresponding
   * {@link IInvertedIndex} by building a {@link KeywordQuery} object. After
   * that it starts a search service where the user types in keywords. The
   * service then lists all documents that contain all given keywords in the
   * order of their BM25 ranking.
   * 
   * @param args
   *          The first argument specifies the path to the file
   * @throws IOException
   *           If an I/O-Exception occurred
   */
  public static void main(final String[] args) throws IOException {
    final boolean isWindowsFile = true;
    final int newLineLength;
    if (isWindowsFile) {
      newLineLength = 2;
    } else {
      newLineLength = 1;
    }

    final File file;
    if (args.length == 1) {
      file = new File(args[0]);
    } else if (args.length == 0) {
      file = new File(PATH_DEFAULT_EXAMPLE_FILE);
    } else {
      throw new IllegalArgumentException(MSG_WRONG_ARGUMENT_LENGTH);
    }

    System.out.println("Fetching documents...");
    boolean useFixLookupFiles = false;
    final DocumentSet documents;
    if (useFixLookupFiles) {
      documents =
          DocumentSet.buildLookupsFromFixTextFileUtf8Tab(file, newLineLength);
    } else {
      documents = DocumentSet.buildFromTextFileUtf8Tab(file);
    }

    System.out.println("Creating keyword query and ranking...");
    final Bm25Ranking<String> ranking = new Bm25Ranking<>(1.75, 0.1);
    final KeywordQuery<IKeyRecord<String>> keywordQuery =
        new KeywordQuery<>(documents, ranking);

    System.out.println("Starting query service.");
    boolean stopService = false;

    try (final Scanner scanner = new Scanner(System.in)) {
      while (!stopService) {
        System.out.println(">Type your query. Type an empty text to stop.");
        final String query = scanner.nextLine().toLowerCase();

        if (query.trim().isEmpty()) {
          stopService = true;
        } else {
          String[] keywords = query.split(KEYWORD_SEPARATOR);
          List<Posting> queryResults =
              keywordQuery.searchOr(Arrays.asList(keywords));
          System.out.println("Matching postings are: " + queryResults);

          System.out.println("Some of them are:");
          final int maximalPostingsForPreview = 3;
          int postingsShown = 0;
          for (final Posting posting : queryResults) {
            final int recordId = posting.getId();
            final IDocument document =
                (IDocument) documents.getKeyRecordById(recordId);
            System.out.println(
                "\t" + document.getName() + "\t" + document.getDescription());

            postingsShown++;
            if (postingsShown >= maximalPostingsForPreview) {
              break;
            }
          }
        }
      }
    }

    System.out.println("Terminated.");
  }

  /**
   * Utility class. No implementation.
   */
  private KeywordQueryExample() {

  }

}
