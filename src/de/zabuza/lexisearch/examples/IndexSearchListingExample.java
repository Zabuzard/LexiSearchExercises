package de.zabuza.lexisearch.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.zabuza.lexisearch.document.DocumentSet;
import de.zabuza.lexisearch.indexing.IInvertedIndex;

public final class IndexSearchListingExample {

  /**
   * Utility class. No implementation.
   */
  private IndexSearchListingExample() {

  }

  private static final String MSG_WRONG_ARGUMENT_LENGTH =
      "Wrong length of arguments.";
  private static final String PATH_DEFAULT_EXAMPLE_FILE =
      "res/examples/movies_small.tsv";

  /**
   * 
   * @param args
   *          The first argument specifies the path to the file
   * @throws IOException
   */
  public static void main(final String[] args) throws IOException {
    final boolean isWindowsFile = false;
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

    System.out.println("Creating inverted index...");
    final long indexBuildStartTime = System.currentTimeMillis();
    final IInvertedIndex<String> invertedIndex =
        documents.createInvertedIndex();
    final long indexBuildEndTime = System.currentTimeMillis();
    final long indexBuildTime = indexBuildEndTime - indexBuildStartTime;

    System.out.println("Post processing...");
    final HashMap<Integer, List<String>> hitsToWords =
        new HashMap<Integer, List<String>>();

    for (final String word : invertedIndex.getKeys()) {
      final int hits = invertedIndex.getRecords(word).getSize();

      List<String> words = hitsToWords.get(hits);
      if (words == null) {
        words = new LinkedList<>();
      }
      words.add(word);
      hitsToWords.put(hits, words);
    }

    final List<Integer> hits = new ArrayList<>(hitsToWords.keySet());
    Collections.sort(hits);

    System.out.println("Hit ranking:");
    for (final Integer hit : hits) {
      for (final String word : hitsToWords.get(hit)) {
        System.out.println(word + " : " + hit);
      }
    }

    System.out.println("Time index building: " + indexBuildTime + "ms");
    System.out.println("Terminated.");
  }

}