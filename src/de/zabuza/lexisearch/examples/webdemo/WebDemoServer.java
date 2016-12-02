package de.zabuza.lexisearch.examples.webdemo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import de.zabuza.lexisearch.indexing.IKeyRecord;
import de.zabuza.lexisearch.indexing.Posting;
import de.zabuza.lexisearch.indexing.qgram.QGramProvider;
import de.zabuza.lexisearch.model.city.City;
import de.zabuza.lexisearch.model.city.CitySet;
import de.zabuza.lexisearch.queries.FuzzyPrefixQuery;
import de.zabuza.lexisearch.ranking.PostingBeforeRecordRanking;

/**
 * Demo application which shows how the API can be used as the server of a web
 * application which solves fuzzy prefix search queries.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class WebDemoServer {
  /**
   * Path to the data file.
   */
  private static final String DEFAULT_PATH_DATA_FILE =
      "res/examples/cities_small.tsv";
  /**
   * The default port to use.
   */
  private static final int DEFAULT_PORT = 8888;
  /**
   * The path from where file serving is allowed. Every file whose path is not
   * included included in this one must not get served.
   */
  private static final String DEFAULT_SERVER_PATH = "res/examples/webdemo";
  /**
   * Constant for an empty answer text.
   */
  private static final String EMPTY_ANSWER = "";
  /**
   * The keyword which every GET request ends with.
   */
  private static final String GET_REQUEST_END = "HTTP";
  /**
   * The text used in HTTP GET requests to separate parameters.
   */
  private static final String GET_SEPARATOR = "&";
  /**
   * Text used in Java-Script to indicate the begin of an array.
   */
  private static final String JS_ARRAY_BEGIN = "[";
  /**
   * Delimiter used in Java-Script to separate entries in an array.
   */
  private static final String JS_ARRAY_DELIMITER = ", ";
  /**
   * Text used in Java-Script to indicate the end of an array.
   */
  private static final String JS_ARRAY_END = "]";
  /**
   * The maximal amount of query matches to return.
   */
  private static final int MAX_AMOUNT_QUERY_MATCHES = 10;
  /**
   * Message shown when using the {@link #main(String[])} with the wrong amount
   * of arguments.
   */
  private static final String MSG_WRONG_ARGUMENT_LENGTH =
      "Wrong length of arguments. Two optional arguments are allowed: "
          + "<dataFile> <serverPath>. See the documentation for more info.";
  /**
   * The keyword which every ordinary GET request begins with.
   */
  private static final String ORDINARY_GET_REQUEST = "GET";
  /**
   * The HTTP GET-parameter that contains the request data.
   */
  private static final String QUERY_GET_REQUEST = "q=";
  /**
   * The charset to use for encoding and decoding text.
   */
  private static final Charset TEXT_CHARSET = StandardCharsets.UTF_8;

  /**
   * Starts the server of the web application.
   * 
   * @param args
   *          The first argument specifies the path to the file to use, else a
   *          sample file gets used. The second argument specifies the port to
   *          use for the server. The third argument specifies the path from
   *          where file serving is allowed. Every file whose path is not
   *          included included in this one must not get served.
   * @throws IOException
   *           If an I/O-exception occurred
   */
  public static void main(final String[] args) throws IOException {
    final File dataFile;
    final String serverPath;
    final int port;
    if (args.length > 3) {
      throw new IllegalArgumentException(MSG_WRONG_ARGUMENT_LENGTH);
    }
    if (args.length >= 1) {
      dataFile = new File(args[0]);
    } else {
      dataFile = new File(DEFAULT_PATH_DATA_FILE);
    }
    if (args.length >= 2) {
      port = Integer.parseInt(args[1]);
    } else {
      port = DEFAULT_PORT;
    }
    if (args.length >= 3) {
      serverPath = args[2];
    } else {
      serverPath = DEFAULT_SERVER_PATH;
    }

    WebDemoServer server =
        new WebDemoServer(port, dataFile, serverPath);
    server.runService();
  }

  /**
   * Data structure which holds all cities.
   */
  private final CitySet mCities;
  /**
   * The path from where file serving is allowed. Every file which path is not
   * included included in this one must not get served.
   */
  private final Path mFileServingPath;
  /**
   * The port to listen at.
   */
  private final int mPort;
  /**
   * Object to use for performing the fuzzy prefix search queries.
   */
  private final FuzzyPrefixQuery<IKeyRecord<String>> mQuery;
  /**
   * The ID of the current request the server is processing.
   */
  private int mRequestId;
  /**
   * The server socket used to listen for requests.
   */
  private final ServerSocket mServerSocket;

  /**
   * Creates a new server for the web application which listens at the given
   * port for requests and solves them.
   * 
   * @param port
   *          Port to use for communication
   * @param dataFile
   *          The file which holds all the data to search for
   * @param serverPath
   *          The path from where file serving is allowed. Every file whose path
   *          is not included included in this one must not get served.
   * @throws IOException
   *           If an I/O-exception occurred
   */
  public WebDemoServer(final int port, final File dataFile,
      final String serverPath) throws IOException {
    mPort = port;
    mRequestId = 0;
    mServerSocket = new ServerSocket(mPort);
    mFileServingPath = Paths.get(serverPath).toAbsolutePath();

    System.out.println("Initializing service...");
    // Loading data from file
    System.out.println("\tLoading file...");
    // Creating data structure
    System.out.println("\tFetching cities...");
    final int qParameter = 3;
    QGramProvider qGramProvider = new QGramProvider(qParameter);
    mCities = CitySet.buildFromTextFileUtf8Tab(dataFile, qGramProvider);

    // Creating fuzzy prefix query and ranking
    System.out.println("\tCreating fuzzy prefix query and ranking...");
    final PostingBeforeRecordRanking<String> ranking =
        new PostingBeforeRecordRanking<>();
    mQuery = new FuzzyPrefixQuery<>(mCities, qGramProvider, ranking);
  }

  /**
   * Runs the service in an infinite loop.
   * 
   * @throws IOException
   *           If an I/O-exception occurred
   */
  public void runService() throws IOException {
    boolean continueService = true;
    System.out.println("Service started.");
    System.out.println("Waiting for requests on port " + mPort + " ...");
    while (continueService) {
      mRequestId++;
      Socket clientSocket = mServerSocket.accept();

      // Read the request
      BufferedReader br = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      System.out.println("#" + mRequestId + " Connected with "
          + clientSocket.getInetAddress());
      String request = br.readLine();
      System.out.println("\tRequest: " + request);

      // Reject the request if empty
      if (request == null || request.trim().length() <= 0) {
        sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.BAD_REQUEST,
            clientSocket);
        br.close();
        clientSocket.close();
        continue;
      }

      // Find the request type and serve
      boolean requestTypeFound = false;

      // Query GET request
      if (!requestTypeFound) {
        int requestDataBeginIndex = request.indexOf(QUERY_GET_REQUEST);
        if (requestDataBeginIndex >= 0) {
          requestTypeFound = true;
          serveQueryGetRequest(request, clientSocket);
        }
      }
      // Ordinary GET request
      if (!requestTypeFound) {
        int requestDataBeginIndex = request.indexOf(ORDINARY_GET_REQUEST);
        if (requestDataBeginIndex >= 0) {
          requestTypeFound = true;
          serveOrdinaryGetRequest(request, clientSocket);
        }
      }
      // Unknown type
      if (!requestTypeFound) {
        sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.NOT_IMPLEMENTED,
            clientSocket);
      }

      br.close();
      clientSocket.close();
    }
  }

  /**
   * Gets the file extension of the given file.
   * 
   * @param file
   *          The file to get its extension
   * @return The extension of the given file
   */
  private EFileExtension getFileExtension(final File file) {
    final String fileName = file.getName();
    String extensionText = "";

    int i = fileName.lastIndexOf('.');
    int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

    if (i > p) {
      extensionText = fileName.substring(i + 1);
    }

    extensionText = extensionText.toLowerCase();

    if (extensionText.matches("txt")) {
      return EFileExtension.TEXT;
    } else if (extensionText.matches("s?html?")) {
      return EFileExtension.HTML;
    } else if (extensionText.matches("css")) {
      return EFileExtension.CSS;
    } else if (extensionText.matches("js")) {
      return EFileExtension.JS;
    } else if (extensionText.matches("png")) {
      return EFileExtension.PNG;
    } else if (extensionText.matches("jp(eg|e|g)")) {
      return EFileExtension.JPG;
    } else {
      return EFileExtension.UNKNOWN;
    }
  }

  /**
   * Sends an empty answer with the given parameters to the given client by
   * using the HTTP/1.0 protocol.
   * 
   * @param client
   *          Client to send to
   * @param contentType
   *          Type of the content to send
   * @param status
   *          The status of the answer to send
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  private void sendHttpAnswer(EHttpContentType contentType, EHttpStatus status,
      final Socket client) throws IOException {
    sendHttpAnswer(EMPTY_ANSWER, contentType, status, client);
  }

  /**
   * Sends the given answer with the given parameters to the given client by
   * using the HTTP/1.0 protocol.
   * 
   * @param answerText
   *          Answer to send
   * @param client
   *          Client to send to
   * @param contentType
   *          Type of the content to send
   * @param status
   *          The status of the answer to send
   * @throws IOException
   *           If an I/O-Exception occurred.
   */
  private void sendHttpAnswer(String answerText, EHttpContentType contentType,
      EHttpStatus status, final Socket client) throws IOException {
    final String contentTypeText;
    if (contentType == EHttpContentType.TEXT) {
      contentTypeText = "text/plain";
    } else if (contentType == EHttpContentType.HTML) {
      contentTypeText = "text/html";
    } else if (contentType == EHttpContentType.CSS) {
      contentTypeText = "text/css";
    } else if (contentType == EHttpContentType.JS) {
      contentTypeText = "application/javascript";
    } else if (contentType == EHttpContentType.PNG) {
      contentTypeText = "image/png";
    } else if (contentType == EHttpContentType.JPG) {
      contentTypeText = "image/jpeg";
    } else {
      contentType = EHttpContentType.TEXT;
      contentTypeText = "text/plain";
      status = EHttpStatus.INTERNAL_SERVER_ERROR;
      // In case of an server error inside this method, don't send the intended
      // message. It might contain sensible data.
      answerText = "";
    }

    final int statusNumber;
    if (status == EHttpStatus.OK) {
      statusNumber = 200;
    } else if (status == EHttpStatus.NO_CONTENT) {
      statusNumber = 204;
    } else if (status == EHttpStatus.BAD_REQUEST) {
      statusNumber = 400;
    } else if (status == EHttpStatus.FORBIDDEN) {
      statusNumber = 403;
    } else if (status == EHttpStatus.NOT_FOUND) {
      statusNumber = 404;
    } else if (status == EHttpStatus.INTERNAL_SERVER_ERROR) {
      statusNumber = 500;
    } else if (status == EHttpStatus.NOT_IMPLEMENTED) {
      statusNumber = 501;
    } else {
      status = EHttpStatus.INTERNAL_SERVER_ERROR;
      statusNumber = 500;
      // In case of an server error inside this method, don't send the intended
      // message. It might contain sensible data.
      answerText = "";
    }

    final byte[] answerTextAsBytes = answerText.getBytes(TEXT_CHARSET);
    final String charset = TEXT_CHARSET.displayName().toLowerCase();

    final String nextLine = "\r\n";
    final StringBuilder answer = new StringBuilder();
    answer.append("HTTP/1.0 " + statusNumber + " " + status + nextLine);
    answer.append("Content-Length: " + answerTextAsBytes.length + nextLine);
    answer.append(
        "Content-Type: " + contentTypeText + ", charset=" + charset + nextLine);
    answer.append("Connection: close" + nextLine);
    answer.append(nextLine);
    answer.append(answerText);

    final DataOutputStream output =
        new DataOutputStream(client.getOutputStream());
    output.write(answer.toString().getBytes(TEXT_CHARSET));
    output.close();

    System.out.println("\tSent " + status);
  }

  /**
   * Serves an ordinary GET request.
   * 
   * @param request
   *          The request to serve
   * @param client
   *          The client to serve
   * @throws IOException
   *           If an I/O-Exception occurred
   */
  private void serveOrdinaryGetRequest(final String request,
      final Socket client) throws IOException {
    // Extract request data
    int requestDataBeginIndex =
        request.indexOf(ORDINARY_GET_REQUEST) + ORDINARY_GET_REQUEST.length();
    int requestDataEndIndex =
        request.indexOf(GET_REQUEST_END, requestDataBeginIndex);
    if (requestDataEndIndex < 0) {
      sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.BAD_REQUEST, client);
      return;
    }
    String requestData =
        request.substring(requestDataBeginIndex, requestDataEndIndex);
    requestData = requestData.trim();
    // Strip the first '/' for a valid directory
    requestData = requestData.substring(1);

    final File file = new File(mFileServingPath.toString(), requestData);
    if (file.exists() && !file.isDirectory()) {
      // Limit allowed files to the file serving path
      final Path filePath = Paths.get(file.toURI()).toAbsolutePath();
      if (filePath.startsWith(mFileServingPath) && file.canRead()
          && !file.isHidden()) {
        // Extract file extension and choose content type
        final EHttpContentType contentType;
        final EFileExtension fileExtension = getFileExtension(file);
        if (fileExtension == EFileExtension.TEXT) {
          contentType = EHttpContentType.TEXT;
        } else if (fileExtension == EFileExtension.HTML) {
          contentType = EHttpContentType.HTML;
        } else if (fileExtension == EFileExtension.CSS) {
          contentType = EHttpContentType.CSS;
        } else if (fileExtension == EFileExtension.JS) {
          contentType = EHttpContentType.JS;
        } else if (fileExtension == EFileExtension.PNG) {
          contentType = EHttpContentType.PNG;
        } else if (fileExtension == EFileExtension.JPG) {
          contentType = EHttpContentType.JPG;
        } else {
          sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.FORBIDDEN, client);
          return;
        }

        // Serve the file
        final byte[] contentAsBytes = Files.readAllBytes(filePath);
        final String content = new String(contentAsBytes, TEXT_CHARSET);

        // Send the answer
        sendHttpAnswer(content, contentType, EHttpStatus.OK, client);
      } else {
        sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.FORBIDDEN, client);
        return;
      }
    } else {
      sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.NOT_FOUND, client);
      return;
    }
  }

  /**
   * Serves an query GET request.
   * 
   * @param request
   *          The request to serve
   * @param client
   *          The client to serve
   * @throws IOException
   *           If an I/O-Exception occurred
   */
  private void serveQueryGetRequest(final String request, final Socket client)
      throws IOException {
    // Extract request data
    int requestDataBeginIndex =
        request.indexOf(QUERY_GET_REQUEST) + QUERY_GET_REQUEST.length();
    int requestDataEndIndex =
        request.indexOf(GET_SEPARATOR, requestDataBeginIndex);
    if (requestDataEndIndex < 0) {
      requestDataEndIndex =
          request.indexOf(GET_REQUEST_END, requestDataBeginIndex);
      if (requestDataEndIndex < 0) {
        sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.BAD_REQUEST, client);
        return;
      }
    }
    final String requestData =
        request.substring(requestDataBeginIndex, requestDataEndIndex);

    // Parse request data
    final String searchKeyword =
        URLDecoder.decode(requestData.trim(), TEXT_CHARSET.displayName());
    if (searchKeyword.length() <= 0) {
      sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.NO_CONTENT, client);
      return;
    }

    // Perform the query
    final LinkedList<String> keywords = new LinkedList<>();
    keywords.add(searchKeyword);
    final List<Posting> matches = mQuery.searchOr(keywords);

    // Send an answer
    if (!matches.isEmpty()) {
      // Build the answer
      final StringJoiner matchesArray = new StringJoiner(JS_ARRAY_DELIMITER);

      // Append every element of the result up to the maximal allowed number
      int amountOfIncludedMatchings = 0;
      final String textWrapper = "\"";
      for (final Posting matchingPosting : matches) {
        final City matchingCity =
            (City) mCities.getKeyRecordById(matchingPosting.getId());
        final String name = matchingCity.getName();
        final float lattitude = matchingCity.getLatitude();
        final float longitude = matchingCity.getLongitude();

        // Append data to the answer
        final String matchArray = JS_ARRAY_BEGIN + textWrapper + name
            + textWrapper + JS_ARRAY_DELIMITER + lattitude + JS_ARRAY_DELIMITER
            + longitude + JS_ARRAY_END;
        matchesArray.add(matchArray);

        // Break if limit reached
        amountOfIncludedMatchings++;
        if (amountOfIncludedMatchings >= MAX_AMOUNT_QUERY_MATCHES) {
          break;
        }
      }

      // Build the answer text as jsonp which calls
      // a callback function
      final String jsonp =
          "queryServerCallback({" + textWrapper + "matches" + textWrapper + ": "
              + JS_ARRAY_BEGIN + matchesArray.toString() + JS_ARRAY_END + "})";
      // Send the answer
      sendHttpAnswer(jsonp, EHttpContentType.TEXT, EHttpStatus.OK, client);
    } else {
      sendHttpAnswer(EHttpContentType.TEXT, EHttpStatus.NO_CONTENT, client);
      return;
    }
  }
}