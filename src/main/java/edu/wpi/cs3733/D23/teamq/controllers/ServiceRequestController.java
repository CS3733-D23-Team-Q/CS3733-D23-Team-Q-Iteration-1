package edu.wpi.cs3733.D23.teamq.controllers;

import edu.wpi.cs3733.D23.teamq.navigation.Navigation;
import edu.wpi.cs3733.D23.teamq.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class ServiceRequestController {

  @FXML MFXButton backButton;

  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
  }
}
