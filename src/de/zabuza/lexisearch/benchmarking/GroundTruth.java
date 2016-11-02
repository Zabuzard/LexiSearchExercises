package de.zabuza.lexisearch.benchmarking;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generic implementation of {@link IGroundTruth} which holds its elements in a
 * hashed-structure.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 * @param <K>
 *          Type of the key
 */
public final class GroundTruth<K> implements IGroundTruth<K> {

  /**
   * Builds a ground truth object for text based keys from a given file.
   * Therefore the file contains a data set per line. The format per line is:
   * <br/>
   * key1{@literal <dataSeparator>}key2{@literal <dataSeparator>}key3{@literal 
   * <dataSeparator>}...{@literal <contentSeparator>}recordId1{@literal 
   * <dataSeparator>}recordId2{@literal <dataSeparator>}recordId3{@literal 
   * <dataSeparator>}... .
   * 
   * @param textFile
   *          The text file to build the ground truth from
   * @param charset
   *          The charset of the text file
   * @param contentSeparator
   *          The separator used for content
   * @param dataSeparator
   *          The separator used for data
   * @return The ground truth object represented by the given file
   * @throws IOException
   *           If an I/O-Exception occurred
   */
  public static GroundTruth<String> buildFromTextFile(final File textFile,
      final Charset charset, final String contentSeparator,
      final String dataSeparator) throws IOException {
    final Stream<String> stream = Files.lines(textFile.toPath(), charset);
    final GroundTruth<String> groundTruth = buildFromTextIterator(
        stream.iterator(), contentSeparator, dataSeparator);
    stream.close();
    return groundTruth;
  }

  /**
   * Builds a ground truth object for text based keys from an iterator.
   * Therefore the iterator lists data sets per element. The format per element
   * is: <br/>
   * key1{@literal <dataSeparator>}key2{@literal <dataSeparator>}key3{@literal 
   * <dataSeparator>}...{@literal <contentSeparator>}recordId1{@literal 
   * <dataSeparator>}recordId2{@literal <dataSeparator>}recordId3{@literal 
   * <dataSeparator>}... .
   * 
   * @param textIterator
   *          The iterator to build the ground truth from
   * @param contentSeparator
   *          The separator used for content
   * @param dataSeparator
   *          The separator used for data
   * @return The ground truth object represented by the given iterator
   */
  public static GroundTruth<String> buildFromTextIterator(
      final Iterator<String> textIterator, final String contentSeparator,
      final String dataSeparator) {
    final GroundTruth<String> groundTruth = new GroundTruth<>();

    while (textIterator.hasNext()) {
      String groundTruthAsText = textIterator.next();
      final int idBegin = 0;
      final int idEnd = groundTruthAsText.indexOf(contentSeparator);
      final String keysText = groundTruthAsText.substring(idBegin, idEnd);
      final List<String> keys = Arrays.asList(keysText.split(dataSeparator));

      final String relevantRecordsText = groundTruthAsText.substring(idEnd + 1);
      final String[] relevantRecordsUnparsed =
          relevantRecordsText.split(dataSeparator);
      ArrayList<Integer> relevantRecords =
          new ArrayList<>(relevantRecordsUnparsed.length);
      for (int i = 0; i < relevantRecordsUnparsed.length; i++) {
        // We need to subtract one as our indices start with zero and not one
        relevantRecords.add(Integer.parseInt(relevantRecordsUnparsed[i]) - 1);
      }

      groundTruth.addRelevantRecords(keys, relevantRecords);
    }

    return groundTruth;
  }

  /**
   * Data structure which holds all data of this ground truth. It maps keys to
   * their relevant records.
   */
  private final HashMap<Collection<K>, HashSet<Integer>> mKeysToRelevantRecords;

  /**
   * Creates a new empty ground truth object.
   */
  public GroundTruth() {
    mKeysToRelevantRecords = new HashMap<>();
  }

  /**
   * Adds the given relevant records to the given keys.
   * 
   * @param keys
   *          Keys to add the records to
   * @param records
   *          Records to add
   */
  public void addRelevantRecords(final Collection<K> keys,
      final Collection<Integer> records) {
    HashSet<Integer> relevantRecords = mKeysToRelevantRecords.get(keys);
    if (relevantRecords == null) {
      relevantRecords = new HashSet<>();
    }
    relevantRecords.addAll(records);
    mKeysToRelevantRecords.put(keys, relevantRecords);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.benchmarking.IGroundTruth#getKeysForRelevantRecords()
   */
  @Override
  public Collection<Collection<K>> getKeysForRelevantRecords() {
    return mKeysToRelevantRecords.keySet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.benchmarking.IGroundTruth#getRelevantRecords(java.util
   * .Collection)
   */
  @Override
  public Collection<Integer> getRelevantRecords(final Collection<K> keys) {
    return mKeysToRelevantRecords.get(keys);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.zabuza.lexisearch.benchmarking.IGroundTruth#knowsRelevantRecords(java.
   * util.Collection)
   */
  @Override
  public boolean hasRelevantRecords(final Collection<K> keys) {
    return mKeysToRelevantRecords.containsKey(keys);
  }

}
