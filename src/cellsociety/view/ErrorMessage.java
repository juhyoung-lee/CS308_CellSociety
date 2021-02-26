package cellsociety.view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ErrorMessage {
  private HBox myBox;

  /**
   * Purpose: TODO
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
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
   * Purpose: TODO
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public HBox getBox() {
      return myBox;
  }
}
