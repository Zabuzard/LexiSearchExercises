package de.zabuza.lexisearch.examples;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.zabuza.lexisearch.benchmarking.AveragePrecision;
import de.zabuza.lexisearch.benchmarking.GroundTruth;
import de.zabuza.lexisearch.benchmarking.IGroundTruth;
import de.zabuza.lexisearch.benchmarking.IMeasure;
import de.zabuza.lexisearch.benchmarking.IMeasureSet;
import de.zabuza.lexisearch.benchmarking.MeasureSet;
import de.zabuza.lexisearch.benchmarking.PrecisionAtK;
import de.zabuza.lexisearch.benchmarking.PrecisionAtR;
import de.zabuza.lexisearch.document.DocumentSet;
import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.queries.KeywordQuery;
import de.zabuza.lexisearch.ranking.Bm25Ranking;

public final class BenchmarkExample {
  /**
   * Text used to separate content.
   */
  private static final String CONTENT_SEPARATOR = "\t";
  /**
   * Text used to separate data.
   */
  private static final String DATA_SEPARATOR = " ";
  /**
   * Message shown when using the {@link #main(String[])} with the wrong amount
   * of arguments.
   */
  private static final String MSG_WRONG_ARGUMENT_LENGTH =
      "Wrong length of arguments.";
  /**
   * Path to the default data file.
   */
  private static final String PATH_DEFAULT_DATA_FILE =
      "res/examples/movies.txt";
  /**
   * Path to the default ground truth file.
   */
  private static final String PATH_DEFAULT_GROUND_TRUTH_FILE =
      "res/examples/movies-benchmark.txt";

  /**
   * 
   * @param args
   *          Not supported
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

    final File dataFile;
    final File groundTruthFile;
    if (args.length == 2) {
      dataFile = new File(args[0]);
      groundTruthFile = new File(args[1]);
    } else if (args.length == 1) {
      dataFile = new File(args[0]);
      groundTruthFile = new File(PATH_DEFAULT_GROUND_TRUTH_FILE);
    } else if (args.length == 0) {
      dataFile = new File(PATH_DEFAULT_DATA_FILE);
      groundTruthFile = new File(PATH_DEFAULT_GROUND_TRUTH_FILE);
    } else {
      throw new IllegalArgumentException(MSG_WRONG_ARGUMENT_LENGTH);
    }

    System.out.println("Fetching documents...");
    boolean useFixLookupFiles = false;
    final DocumentSet documents;
    if (useFixLookupFiles) {
      documents = DocumentSet.buildLookupsFromFixTextFileUtf8Tab(dataFile,
          newLineLength);
    } else {
      documents = DocumentSet.buildFromTextFileUtf8Tab(dataFile);
    }

    System.out.println("Creating keyword query and ranking...");
    final Bm25Ranking<String> ranking = new Bm25Ranking<>();
    final KeywordQuery<IKeyRecord<String>> keywordQuery =
        new KeywordQuery<>(documents, ranking);

    System.out.println("Creating ground truth and benchmark set...");
    final IGroundTruth<String> groundTruth =
        GroundTruth.buildFromTextFile(groundTruthFile, StandardCharsets.UTF_8,
            CONTENT_SEPARATOR, DATA_SEPARATOR);
    final IMeasureSet<String> measures = new MeasureSet<>();
    measures.addMeasure(new PrecisionAtK<>(3));
    measures.addMeasure(new PrecisionAtR<>());
    measures.addMeasure(new AveragePrecision<>());

    final Collection<Collection<String>> allKeys =
        groundTruth.getKeysForRelevantRecords();

    System.out.println("Benchmarking...");
    final Map<IMeasure<String>, LinkedList<Double>> evaluationResults =
        new HashMap<>();
    for (final Collection<String> keys : allKeys) {
      final List<Posting> queryResult = keywordQuery.searchOr(keys);
      final Map<IMeasure<String>, Double> evaluation =
          measures.evaluateRelevance(keys, queryResult, groundTruth);

      for (final IMeasure<String> measure : evaluation.keySet()) {
        LinkedList<Double> currentResults = evaluationResults.get(measure);
        if (currentResults == null) {
          currentResults = new LinkedList<>();
        }
        currentResults.add(evaluation.get(measure));
        evaluationResults.put(measure, currentResults);
      }
    }

    System.out.println("Mean results are:");
    final NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(1);
    for (final IMeasure<String> measure : evaluationResults.keySet()) {
      final LinkedList<Double> resultsForMeasure =
          evaluationResults.get(measure);
      final double amountOfResultsForMeasure = resultsForMeasure.size();
      double resultsForMeasureTotal = 0;
      for (final Double value : resultsForMeasure) {
        resultsForMeasureTotal += value;
      }

      final double meanResultsForMeasure;
      if (amountOfResultsForMeasure == 0) {
        meanResultsForMeasure = 0;
      } else {
        meanResultsForMeasure =
            resultsForMeasureTotal / amountOfResultsForMeasure;
      }

      System.out.println("\t" + measure.getClass().getSimpleName() + ": "
          + percentFormat.format(meanResultsForMeasure));
    }
  }

}
