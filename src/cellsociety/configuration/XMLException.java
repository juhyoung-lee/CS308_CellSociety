package cellsociety.configuration;

/**
 * This class represents what might go wrong when using XML files.
 *
 * @author Kenneth Moore III
 */
public class XMLException extends Exception {

  /**
   * Create an exception based on an issue in our code.
   */
  public XMLException(String message, Object... values) {
    super(String.format(message, values));
  }

}

