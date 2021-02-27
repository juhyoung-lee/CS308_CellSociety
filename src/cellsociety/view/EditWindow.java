package cellsociety.view;

import cellsociety.Control;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class EditWindow extends NewWindow {
  private Map<String, Integer> paramsMap;
  private List<TextField> allTextFields;

  public EditWindow(String styleSheet, String displayTitle, String windowTitle, int size, ScreenControl screenControl) {
    super(styleSheet, displayTitle, windowTitle, size, screenControl);
    paramsMap = mySC.getParamsMap();
    allTextFields = setAllTextFields(myWindow);
  }

  public Button buttonCreation(String text, double x, double y) {
    Button button = new Button(text);
    button.getStyleClass().add("button");
    button.setLayoutX(x);
    button.setLayoutY(y);
    myWindow.getChildren().add(button);
    return button;
  }

  private List<TextField> setAllTextFields(Pane myWindow) {
    List<TextField> textFields = new ArrayList<>();

    int i = 0;
    for (String key : paramsMap.keySet()) {
      if (!((key.equals("width") || key.equals("neighborhoodSize") || key.equals("state")
              || key.equals("height")))) {
        textFields.add(textFieldCreation(key, 0,
                25 + i * (Control.X_SIZE / paramsMap.size()), myWindow));
        i++;
      }
    }
    return textFields;
  }

  private TextField textFieldCreation(String prompt, double x, double y, Pane pane) {
    javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
    textField.setPromptText(prompt);
    textField.setLayoutX(x);
    textField.setLayoutY(y);
    pane.getChildren().add(textField);
    return textField;
  }

  public List<TextField> getAllTextFields() {
    return allTextFields;
  }

  public Map<String, Integer> getData(List<TextField> allTextFields) {
    Map<String, Integer> newParams = new HashMap<>();

    for (TextField textField : allTextFields) {
      if (!textField.getText().equals("")) {
        newParams.put(textField.getPromptText(), Integer.parseInt(textField.getText()));
      }
    }
  return newParams;
  }
}
