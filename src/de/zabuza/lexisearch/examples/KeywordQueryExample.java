package de.zabuza.lexisearch.examples;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import de.zabuza.lexisearch.document.DocumentSet;
import de.zabuza.lexisearch.document.IDocument;
import de.zabuza.lexisearch.indexing.IInvertedList;
import de.zabuza.lexisearch.queries.KeywordQuery;

public final class KeywordQueryExample {

  private static final String MSG_WRONG_ARGUMENT_LENGTH =
      "Wrong length of arguments.";
  private static final String PATH_DEFAULT_EXAMPLE_FILE =
      "res/examples/movies.txt";

  private static final String KEYWORD_SEPARATOR = " ";

  /**
   * Utility class. No implementation.
   */
  private KeywordQueryExample() {

  }

  /**
   * 
   * @param args
   *          The first argument specifies the path to the file
   * @throws IOException
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

    System.out.println("Creating keyword query...");
    final KeywordQuery<IDocument> keywordQuery = new KeywordQuery<>(documents);

    System.out.println("Starting query service.");
    boolean stopService = false;
    final Scanner scanner = new Scanner(System.in);
    while (!stopService) {
      System.out.println(">Type your query. Type an empty text to stop.");
      final String query = scanner.nextLine();

      if (query.trim().isEmpty()) {
        stopService = true;
      } else {
        String[] keywords = query.split(KEYWORD_SEPARATOR);
        IInvertedList queryResults =
            keywordQuery.searchAnd(Arrays.asList(keywords));
        System.out.println("Matching records are: " + queryResults);

        System.out.println("Some of them are:");
        Iterable<Integer> records = queryResults.getRecords();
        final int maximalRecordsForPreview = 3;
        int recordsShown = 0;
        for (final int recordId : records) {
          final IDocument document = documents.getDocumentById(recordId);
          System.out.println(
              "\t" + document.getName() + "\t" + document.getDescription());

          recordsShown++;
          if (recordsShown >= maximalRecordsForPreview) {
            break;
          }
        }
      }
    }

    scanner.close();
    System.out.println("Terminated.");
  }

}
