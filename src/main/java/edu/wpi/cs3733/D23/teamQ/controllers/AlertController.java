package edu.wpi.cs3733.D23.teamQ.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertController implements IController {
  static String message;
  static Stage stage;
  @FXML private Label alertLabel;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void initialize() {
    alertLabel.setText(message);
  }

  @FXML
  public void okButtonClicked() {
    stage.close();
  }
}
