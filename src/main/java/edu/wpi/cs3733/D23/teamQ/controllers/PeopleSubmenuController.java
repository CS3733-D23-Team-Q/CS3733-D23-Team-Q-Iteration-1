package edu.wpi.cs3733.D23.teamQ.controllers;

import edu.wpi.cs3733.D23.teamQ.navigation.Navigation;
import edu.wpi.cs3733.D23.teamQ.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class PeopleSubmenuController {
  @FXML VBox peopleSM;
  @FXML MFXButton profile;
  @FXML MFXButton directory;
  @FXML MenuRootController mrc;

  @FXML
  public void profileClicked() {
    Navigation.navigate(Screen.PROFILE_PAGE);
  }

  @FXML
  public void directoryClicked() {
    Navigation.navigate(Screen.DIRECTORY);
  }

  @FXML
  public void psmExited() {
    mrc.showPeopleSM(false);
  }

  public void setVisible(boolean v) {
    peopleSM.toFront();
    peopleSM.setVisible(v);
  }

  public void setRootController(MenuRootController mrc) {
    this.mrc = mrc;
  }
}
