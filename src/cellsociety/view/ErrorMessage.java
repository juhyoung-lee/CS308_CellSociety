package cellsociety.view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Purpose: Creates buttons for the view.
 * Assumptions: The buttons in these classes are the only ones that need to be displayed
 *              (can add new buttons though to this class)
 * Dependencies: javafx.geometry.Pos, javafx.scene.layout.HBox,
 *               javafx.scene.text.Font, java.scene.text.Text
 * Example of use: ErrorMessage error = new ErrorMessage(message)
 *
 * @author Kathleen Chen
 */

public class ErrorMessage {
  private HBox myBox;

  /**
   * Purpose: Constructor of ErrorMessage.
   * Assumptions: None
   * Parameters: String message
   * Exception: None
   */
  public ErrorMessage(String message) {
    myBox = new HBox();
    myBox.setAlignment(Pos.CENTER);
    displayMessage(message);
  }

  private void displayMessage(String message) {
    Text dText = new Text(0, 0, message);
    dText.setFont(new Font(25));
    myBox.getChildren().add(dText);
  }

  /**
   * Purpose: Returns the HBox that holds the error message
   *          so that it can be displayed on a main Pane.
   * Assumptions: None
   * Parameters: None
   * Exception: None
   * Returns: HBox
   */
  public HBox getBox() {
    return myBox;
  }
}
