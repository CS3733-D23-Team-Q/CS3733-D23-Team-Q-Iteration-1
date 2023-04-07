package edu.wpi.cs3733.D23.teamq.controllers;

import edu.wpi.cs3733.D23.teamq.navigation.Navigation;
import edu.wpi.cs3733.D23.teamq.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class HomeController {

  @FXML MFXButton navigateButton;

  @FXML
  public void initialize() {
    navigateButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
  }
}
