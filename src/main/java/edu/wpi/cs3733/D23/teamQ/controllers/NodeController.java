package edu.wpi.cs3733.D23.teamQ.controllers;

import edu.wpi.cs3733.D23.teamQ.Alert;
import edu.wpi.cs3733.D23.teamQ.Confirm;
import edu.wpi.cs3733.D23.teamQ.db.Qdb;
import edu.wpi.cs3733.D23.teamQ.db.obj.Node;
import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class NodeController {
  Alert alert = new Alert();

  private String path;

  private int nodeIDEdit;

  private String newBuilding;

  private String newFloor;

  private int newXcoord;

  private int newYcoord;

  @FXML private TextField ImportPath;

  @FXML private ImageView image;

  @FXML private ImageView image2;

  @FXML private Label InformationAlert;

  @FXML private Label nodeIDAlert;
  @FXML private TextField BuildingInput;

  @FXML private TextField FloorInput;

  @FXML private TextField NodeIDInput;

  @FXML private TextField XInput;

  @FXML private TextField YInput;

  @FXML private TableView<Node> node;

  @FXML private TableColumn<Node, Number> NodeID;

  @FXML private TableColumn<Node, String> Building;

  @FXML private TableColumn<Node, String> Floor;

  @FXML private TableColumn<Node, Number> Xcoord;

  @FXML private TableColumn<Node, Number> Ycoord;

  /** used to put Nodes from database arraylist to observablelist */
  public ObservableList<Node> nodes() {
    ObservableList<Node> node = FXCollections.observableArrayList();
    for (int i = 0; i < Qdb.getInstance().nodeTable.getAllRows().size(); i++) {
      node.add(Qdb.getInstance().nodeTable.getAllRows().get(i));
    }
    return node;
  }

  public void initialize() {

    /** input the building to the table */
    Building.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(TableColumn.CellDataFeatures<Node, String> param) {
            SimpleStringProperty buildings =
                new SimpleStringProperty(param.getValue().getBuilding());
            return buildings;
          }
        });

    /** input the Xcoord to the table */
    Xcoord.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Node, Number>, ObservableValue<Number>>() {
          @Override
          public ObservableValue<Number> call(TableColumn.CellDataFeatures<Node, Number> param) {
            SimpleIntegerProperty xcoords = new SimpleIntegerProperty(param.getValue().getXCoord());
            return xcoords;
          }
        });

    /** input the Ycoord to the table */
    Ycoord.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Node, Number>, ObservableValue<Number>>() {
          @Override
          public ObservableValue<Number> call(TableColumn.CellDataFeatures<Node, Number> param) {
            SimpleIntegerProperty ycoords = new SimpleIntegerProperty(param.getValue().getYCoord());
            return ycoords;
          }
        });

    /** input the nodeID to the table */
    NodeID.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Node, Number>, ObservableValue<Number>>() {
          @Override
          public ObservableValue<Number> call(TableColumn.CellDataFeatures<Node, Number> param) {
            SimpleIntegerProperty nodeids = new SimpleIntegerProperty(param.getValue().getNodeID());
            return nodeids;
          }
        });

    /** input the floor to the table */
    Floor.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(TableColumn.CellDataFeatures<Node, String> param) {
            SimpleStringProperty floors = new SimpleStringProperty(param.getValue().getFloor());
            return floors;
          }
        });

    /** set the node tableview */
    node.setItems(nodes());
  }
  /*

  @FXML
  void edgeClicked(ActionEvent event) {
    Navigation.navigate(Screen.Edge_Table);
  }

  @FXML
  void homeClicked(ActionEvent event) {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  void locationClicked(ActionEvent event) {
    Navigation.navigate(Screen.LocationName_Table);
  }

  @FXML
  void mapClicked(ActionEvent event) {}

  @FXML
  void moveClicked(ActionEvent event) {
    Navigation.navigate(Screen.Move_Table);
  }

  public void exitClicked(ActionEvent actionEvent) {
    Platform.exit();
  }

   */

  @FXML
  void AddClicked(MouseEvent event) throws SQLException {
    if (isNumber(NodeIDInput.getText())) {
      nodeIDEdit = Integer.parseInt(NodeIDInput.getText());
      if (!nodeIDExist(nodeIDEdit)) {
        if (isNumber(XInput.getText())) {
          if (isNumber(YInput.getText())) {

            if (FloorInput.getText() != "") {

              if (BuildingInput.getText() != "") {

                if (locationExist(nodeIDEdit)) {
                  alert.clearLabelAlert(InformationAlert, image2);
                  alert.clearLabelAlert(nodeIDAlert, image);
                  newBuilding = BuildingInput.getText();
                  newFloor = FloorInput.getText();
                  newXcoord = Integer.parseInt(XInput.getText());
                  newYcoord = Integer.parseInt(YInput.getText());

                  Qdb.getInstance()
                      .nodeTable
                      .addRow(
                          new Node(
                              nodeIDEdit,
                              newXcoord,
                              newYcoord,
                              newFloor,
                              newBuilding,
                              Qdb.getInstance().locationTable.retrieveRow(nodeIDEdit)));
                  node.setItems(nodes());
                } else {
                  alert.setLabelAlert(
                      "The location does not exist, please create a new location with this nodeID",
                      nodeIDAlert,
                      image);
                  alert.clearLabelAlert(InformationAlert, image2);
                }

              } else {
                alert.setLabelAlert("Please input the correct building", InformationAlert, image2);
                alert.clearLabelAlert(nodeIDAlert, image);
                // Call Alert "Please input the correct Building"

              }
            } else {
              alert.setLabelAlert("Please input the correct floor", InformationAlert, image2);
              alert.clearLabelAlert(nodeIDAlert, image);
              // Call Alert "Please input the correct Floor"
            }
          } else {
            alert.setLabelAlert("Please input the correct Y-coord.", InformationAlert, image2);
            alert.clearLabelAlert(nodeIDAlert, image);
            // Call Alert "Please input the correct Y-coord"
          }

        } else {

          alert.setLabelAlert("Please input the correct X-coord.", InformationAlert, image2);
          alert.clearLabelAlert(nodeIDAlert, image);
          // Call Alert "Please input the  correct X-coord"
        }
      } else {
        alert.setLabelAlert("This nodeID exists, please use another one.", nodeIDAlert, image);
        alert.clearLabelAlert(InformationAlert, image2);

        // Here to call alert "This nodeID exists, please use another one
      }
    } else {
      alert.setLabelAlert("Please input the correct nodeID.", nodeIDAlert, image);
      alert.clearLabelAlert(InformationAlert, image2);
      // Call Alert "Please input the correct nodeID"
    }
  }

  @FXML
  void DeleteClicked(MouseEvent event) throws SQLException {

    if (isNumber(NodeIDInput.getText())) {
      nodeIDEdit = Integer.parseInt(NodeIDInput.getText());

      if (nodeIDExist(nodeIDEdit)) {
        alert.clearLabelAlert(InformationAlert, image2);
        alert.clearLabelAlert(nodeIDAlert, image);

        Qdb.getInstance().nodeTable.deleteRow(nodeIDEdit);
        node.setItems(nodes());

        // Here to call confirm
      } else {
        alert.setLabelAlert("This nodeID does not exist.", nodeIDAlert, image);
        alert.clearLabelAlert(InformationAlert, image2);
        // here to call alert "This nodeID does not exist
      }

    } else {
      alert.setLabelAlert("Please input the correct nodeID.", nodeIDAlert, image);
      alert.clearLabelAlert(InformationAlert, image2);
      // Here to call alert "Please input the correct nodeID"

    }
  }

  @FXML
  void SetClicked(MouseEvent event) throws SQLException {
    if (isNumber(NodeIDInput.getText())) {

      if (nodeIDExist(nodeIDEdit)) {
        if (locationExist(nodeIDEdit)) {
          if (isNumber(XInput.getText())) {
            if (isNumber(YInput.getText())) {
              if (FloorInput != null) {
                if (BuildingInput != null) {

                  nodeIDEdit = Integer.parseInt(NodeIDInput.getText());

                  alert.clearLabelAlert(InformationAlert, image2);
                  alert.clearLabelAlert(nodeIDAlert, image);
                  newBuilding = BuildingInput.getText();
                  newFloor = FloorInput.getText();
                  newXcoord = Integer.parseInt(XInput.getText());
                  newYcoord = Integer.parseInt(YInput.getText());

                  Qdb.getInstance()
                      .nodeTable
                      .updateRow(
                          nodeIDEdit,
                          new Node(
                              nodeIDEdit,
                              newXcoord,
                              newYcoord,
                              newFloor,
                              newBuilding,
                              Qdb.getInstance().locationTable.retrieveRow(nodeIDEdit)));

                  node.setItems(nodes());

                } else {
                  alert.setLabelAlert(
                      "Please input the correct building", InformationAlert, image2);
                  alert.clearLabelAlert(nodeIDAlert, image);
                  // Call Alert "Please input the correct Building"

                }
              } else {
                alert.setLabelAlert("Please input the correct floor", InformationAlert, image2);
                alert.clearLabelAlert(nodeIDAlert, image);
                // Call Alert "Please input the correct Floor"
              }
            } else {
              alert.setLabelAlert("Please input the correct Y-coord.", InformationAlert, image2);
              alert.clearLabelAlert(nodeIDAlert, image);
              // Call Alert "Please input the correct Y-coord"
            }
          } else {
            alert.setLabelAlert("Please input the correct X-coord.", InformationAlert, image2);
            alert.clearLabelAlert(nodeIDAlert, image);
            // Call Alert "Please input the  correct X-coord"
          }
        } else {
          alert.setLabelAlert("The location of this nodeID does not exist.", nodeIDAlert, image);
          alert.clearLabelAlert(InformationAlert, image2);
        }
      } else {
        alert.setLabelAlert("This nodeID does not exist.", nodeIDAlert, image);
        alert.clearLabelAlert(InformationAlert, image2);
        // Here to call alert "This nodeID does not exist"
      }
    } else {
      alert.setLabelAlert("Please input the correct nodeID.", nodeIDAlert, image);
      alert.clearLabelAlert(InformationAlert, image2);
      // Call Alert "Please input the correct nodeID"
    }
  }

  public static boolean isNumber(String str) {
    if (str == "") return false;
    for (char c : str.toCharArray()) {
      String s = "" + c;
      if (!Character.isDigit(c) || s.equals(".")) {
        return false;
      }
    }
    return true;
  }

  public static boolean nodeIDExist(int nodeID) {
    for (int i = 0; i < Qdb.getInstance().nodeTable.getAllRows().size(); i++) {
      if (nodeID == Qdb.getInstance().nodeTable.getAllRows().get(i).getNodeID()) return true;
    }
    return false;
  }

  public boolean locationExist(int nodeID) {
    for (int i = 0; i < Qdb.getInstance().locationTable.getAllRows().size(); i++) {
      if (nodeID == Qdb.getInstance().locationTable.getAllRows().get(i).getNodeID()) return true;
    }
    return false;
  }

  @FXML
  void ExportClicked(MouseEvent event) throws IOException {
    path = ImportPath.getText();
    boolean success = Qdb.getInstance().nodeTable.toCSV(path);
    if (success) {
      Confirm.confirmBox("Export Successfully", "Export Successfully");
    } else {
      Alert.alertBox("Failed to Export", "Failed to Export");
    }
    node.setItems(nodes());
  }

  @FXML
  void ImportClicked(MouseEvent event) throws IOException {
    path = ImportPath.getText();
    boolean success = Qdb.getInstance().nodeTable.importCSV(path);
    if (success) {
      Confirm.confirmBox("Import Successfully", "Import Successfully");
    } else {
      Alert.alertBox("Failed to Import", "Failed to Import");
    }
    node.setItems(nodes());
  }
  /*
   @FXML
   void BackClicked(ActionEvent event) {
     Navigation.navigate(Screen.MAP_EDITOR);
   }

  */
}
