package edu.wpi.cs3733.D23.teamQ.controllers;

import static edu.wpi.cs3733.D23.teamQ.SecondaryStage.newStage;

import edu.wpi.cs3733.D23.teamQ.Alert;
import edu.wpi.cs3733.D23.teamQ.db.Qdb;
import edu.wpi.cs3733.D23.teamQ.db.obj.Location;
import edu.wpi.cs3733.D23.teamQ.db.obj.Node;
import edu.wpi.cs3733.D23.teamQ.navigation.Navigation;
import edu.wpi.cs3733.D23.teamQ.navigation.Screen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

public class GraphicalMapEditorController {

  Qdb qdb = Qdb.getInstance();
  // Stage stage = App.getPrimaryStage();
  Alert alert = new Alert();
  Text text;

  @FXML Group parent;
  @FXML ImageView map;
  @FXML Button addButton;
  @FXML Button editButton;
  @FXML Button deleteButton;

  private GesturePane pane;

  @FXML private AnchorPane root;

  private int xcoord;

  private int ycoord;

  private String newBuilding;
  private String newFloor;
  private int newXcoord;
  private int newYcoord;
  private String newLongName;
  private String newShortName;

  private String newNodeType;

  @FXML private TextField xinitial;

  @FXML private TextField yinitial;

  private int nodeid;
  @FXML private TextField buildinginitial;

  @FXML private TextField floorinitial;

  @FXML private TextField longnameinitial;

  @FXML private TextField shortnameinitial;

  @FXML private TextField nodetypeinitial;

  @FXML private Label alerts;

  @FXML private ImageView image;

  @FXML private TextField nodeidinput;

  /**
   * update node
   *
   * @param event
   */
  @FXML
  void setclicked(MouseEvent event) {
    if (nodeIDAlertone(nodeidinput, alerts, image)) {
      if (coordAlert(xinitial, yinitial, alerts, image)) {
        if (locationAlert(longnameinitial, shortnameinitial, nodetypeinitial, alerts, image)) {
          nodeid = Integer.parseInt(nodeidinput.getText());
          newLongName = longnameinitial.getText();
          newNodeType = nodetypeinitial.getText();
          newShortName = shortnameinitial.getText();
          qdb.locationTable.updateRow(
              nodeid, new Location(nodeid, newLongName, newShortName, newNodeType));
          newBuilding = buildinginitial.getText();
          newFloor = floorinitial.getText();
          newXcoord = Integer.parseInt(xinitial.getText());
          newYcoord = Integer.parseInt(yinitial.getText());

          qdb.nodeTable.updateRow(
              nodeid,
              new Node(
                  nodeid,
                  newXcoord,
                  newYcoord,
                  newFloor,
                  newBuilding,
                  Qdb.getInstance().locationTable.retrieveRow(nodeid)));
          refresh();
        }
      }
    }
  }

  /**
   * delete node
   *
   * @param event
   */
  @FXML
  void deleteclicked(MouseEvent event) {
    if (nodeIDAlertone(nodeidinput, alerts, image)) {
      qdb.nodeTable.deleteRow(nodeid);
      qdb.locationTable.deleteRow(nodeid);
      refresh();
    } else {
      InitialNode();
    }
  }

  /**
   * find node and display the information of the node
   *
   * @param event
   */
  @FXML
  void findclicked(MouseEvent event) {
    if (nodeIDAlertone(nodeidinput, alerts, image)) {
      nodeid = Integer.parseInt(nodeidinput.getText());
      NodeInformation(nodeid);
      Point2D pivotOnTarget =
          new Point2D(
              qdb.nodeTable.retrieveRow(nodeid).getXCoord() / 5,
              qdb.nodeTable.retrieveRow(nodeid).getYCoord() / 5);
      pane.animate(Duration.millis(200))
          .interpolateWith(Interpolator.EASE_BOTH)
          .zoomBy(pane.getCurrentScale(), pivotOnTarget);

    } else {
      InitialNode();
    }
  }

  @FXML
  void addclicked(MouseEvent event) {
    if (nodeIDAlerttwo(nodeidinput, alerts, image)) {
      if (coordAlert(xinitial, yinitial, alerts, image)) {
        if (locationAlert(longnameinitial, shortnameinitial, nodetypeinitial, alerts, image)) {
          nodeid = Integer.parseInt(nodeidinput.getText());
          newLongName = longnameinitial.getText();
          newNodeType = nodetypeinitial.getText();
          newShortName = shortnameinitial.getText();
          qdb.locationTable.addRow(new Location(nodeid, newLongName, newShortName, newNodeType));
          newBuilding = buildinginitial.getText();
          newFloor = floorinitial.getText();
          newXcoord = Integer.parseInt(xinitial.getText());
          newYcoord = Integer.parseInt(yinitial.getText());

          qdb.nodeTable.addRow(
              new Node(
                  nodeid,
                  newXcoord,
                  newYcoord,
                  newFloor,
                  newBuilding,
                  Qdb.getInstance().locationTable.retrieveRow(nodeid)));
          refresh();
        }
      }
    }
  }

  @FXML
  void clearclicked(MouseEvent event) {
    nodeidinput.setText("");
    InitialNode();
  }

  @FXML
  public void initialize() throws IOException {
    /*  Text texts = new Text();
     root.getChildren().add(texts);

    */
    addButtons();
    javafx.scene.Node node = parent;
    pane = new GesturePane(node);
    root.getChildren().add(pane);

    pane.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            Point2D pivotOnTarget =
                pane.targetPointAt(new Point2D(e.getX(), e.getY()))
                    .orElse(pane.targetPointAtViewportCentre());
            pane.animate(Duration.millis(200))
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(pane.getCurrentScale(), pivotOnTarget);
          }
          if (e.getButton() == MouseButton.SECONDARY) {
            xcoord = (int) e.getX() * 5;
            ycoord = (int) e.getY() * 5;
            nodeidinput.setText("");
            InitialNode();
            xinitial.setText(Integer.toString(xcoord));
            yinitial.setText(Integer.toString(ycoord));
          }
        });

    /*
    Text texts = new Text();
    root.setOnMouseClicked(
        event -> {
          String location = String.format("(%.1f,%.1f)", event.getX(), event.getY());
          texts.setText(location);
          texts.setX(event.getX());
          texts.setY(event.getY());
        });
    root.setOnMouseReleased(event -> texts.setText(""));
    parent.getChildren().add(texts);

     */
  }

  public void addButtons() {
    List<Node> nodes = qdb.retrieveAllNodes();
    List<Node> L1nodes = new ArrayList<>();
    for (Node n : nodes) {
      if (n.getFloor().equals("1")) {
        L1nodes.add(n);
      }
    }
    for (Node n : L1nodes) {
      int x = n.getXCoord() / 5;
      int y = n.getYCoord() / 5;
      Button node = new Button();
      node.setLayoutX(x);
      node.setLayoutY(y);
      node.setStyle(
          "-fx-background-radius: 5em;"
              + "-fx-min-width: 3px;"
              + "-fx-min-height: 3px;"
              + "-fx-max-width: 3px;"
              + "-fx-max-height: 3px;"
              + "-fx-background-insets: 0px;");
      node.setOnMouseClicked(
          e -> {
            int nodeID = n.getNodeID();
            nodeidinput.setText(Integer.toString(nodeID));
            NodeInformation(nodeID);
          });
      node.setOnMouseEntered(
          e -> {
            int nodeID = n.getNodeID();
            Location location = qdb.retrieveLocation(nodeID);
            String name = location.getShortName();
            Pattern pattern = Pattern.compile("(?i).*hall.*");
            if (!pattern.matcher(name).matches()) {
              text = new Text(x + 3, y + 3, name);
              text.setStyle("-fx-font-size: 8px;");
              parent.getChildren().add(text);
            } else {
              text = new Text(x + 3, y + 3, "");
              text.setStyle("-fx-font-size: 8px;");
              parent.getChildren().add(text);
            }
          });
      node.setOnMouseExited(
          e -> {
            parent.getChildren().remove(text);
          });
      final Delta dragDelta = new Delta();
      /*
      node.setOnMousePressed(
          e -> {
            //dragDelta.x = node.getLayoutX() - e.getSceneX();
            //dragDelta.y = node.getLayoutY() - e.getSceneY();
              dragDelta.x = node.getLayoutX() - e.getSceneX();
            node.setCursor(Cursor.MOVE);
          });
      node.setOnMouseReleased(
          e -> {
            node.setCursor(Cursor.HAND);
          });
      node.setOnMouseDragged(
          e -> {
            node.setLayoutX(e.getSceneX());
            node.setLayoutY(e.getSceneY());
          });
      node.setOnMouseEntered(
          e -> {
            node.setCursor(Cursor.HAND);
          });
       */
      /*
      node.setOnMouseClicked(
              e -> {
              });
       */
      parent.getChildren().add(node);
    }
  }

  class Delta {
    double x, y;
  }

  /** if nodeid exist, the user can edit the node. Else call alert. */
  public void NodeInformation(int id) {
    xinitial.setText(Integer.toString(qdb.nodeTable.retrieveRow(id).getXCoord()));
    buildinginitial.setText(qdb.nodeTable.retrieveRow(id).getBuilding());
    yinitial.setText(Integer.toString(qdb.nodeTable.retrieveRow(id).getYCoord()));
    floorinitial.setText(qdb.nodeTable.retrieveRow(id).getFloor());
    longnameinitial.setText(qdb.locationTable.retrieveRow(id).getLongName());
    shortnameinitial.setText(qdb.locationTable.retrieveRow(id).getShortName());
    nodetypeinitial.setText(qdb.locationTable.retrieveRow(id).getNodeType());
  }

  /** initialize the node information. */
  public void InitialNode() {
    xinitial.setText("--");
    buildinginitial.setText("--");
    yinitial.setText("--");
    floorinitial.setText("--");
    longnameinitial.setText("--");
    shortnameinitial.setText("--");
    nodetypeinitial.setText("--");
  }

  /**
   * if the str is a number, return true, else false
   *
   * @param str
   * @return
   */
  public boolean isNumber(String str) {
    if (str == "") return false;
    for (char c : str.toCharArray()) {
      if (!Character.isDigit(c)) {
        return false;
      }
    }
    return true;
  }

  /**
   * true if the node exists, false else
   *
   * @param nodeID
   * @return
   */
  public boolean nodeIDExist(int nodeID) {
    for (int i = 0; i < Qdb.getInstance().nodeTable.getAllRows().size(); i++) {
      if (nodeID == Qdb.getInstance().nodeTable.getAllRows().get(i).getNodeID()) return true;
    }
    return false;
  }

  public boolean nodeIDAlertone(TextField nodeID, Label nodeIDAlert, ImageView image) {
    Alert alert = new Alert();
    if (isNumber(nodeID.getText())) {
      int nodeIDInput = Integer.parseInt(nodeID.getText());
      if (nodeIDExist(nodeIDInput)) {
        alert.clearLabelAlert(nodeIDAlert, image);
        return true;
      } else {
        alert.setLabelAlert("This nodeID does not exist.", nodeIDAlert, image);
      }
    } else {
      alert.setLabelAlert("Please input the correct nodeID.", nodeIDAlert, image);
    }
    return false;
  }

  public boolean nodeIDAlerttwo(TextField nodeID, Label nodeIDAlert, ImageView image) {
    Alert alert = new Alert();
    if (isNumber(nodeID.getText())) {
      int nodeIDInput = Integer.parseInt(nodeID.getText());
      if (!nodeIDExist(nodeIDInput)) {
        alert.clearLabelAlert(nodeIDAlert, image);
        return true;
      } else {
        alert.setLabelAlert("This nodeID exists.", nodeIDAlert, image);
      }
    } else {
      alert.setLabelAlert("Please input the correct nodeID.", nodeIDAlert, image);
    }
    return false;
  }

  public boolean coordAlert(
      TextField xcoord, TextField ycoord, Label informationAlert, ImageView image) {
    Alert alert = new Alert();
    if (isNumber(xcoord.getText())) {
      if (isNumber(ycoord.getText())) {
        alert.clearLabelAlert(informationAlert, image);
        return true;
      } else {
        alert.setLabelAlert("Please input the correct X-coord.", informationAlert, image);
      }
    } else {
      alert.setLabelAlert("Please input the correct Y-coord.", informationAlert, image);
    }
    return false;
  }

  public boolean locationAlert(
      TextField longname,
      TextField shortname,
      TextField nodetype,
      Label informationAlert,
      ImageView image) {
    Alert alert = new Alert();
    if (longname.getText() != "") {
      if (shortname.getText() != "") {
        if (nodetype.getText() != "") {
          alert.clearLabelAlert(informationAlert, image);
          return true;
        } else {
          alert.setLabelAlert("Please input the correct node type.", informationAlert, image);
        }
      } else {
        alert.setLabelAlert("Please input the correct short name.", informationAlert, image);
      }
    } else {
      alert.setLabelAlert("Please input the correct long name.", informationAlert, image);
    }
    return false;
  }
  /*
  @FXML
  void AddonmapClicked(MouseEvent event) {
    if (AddOnMap == true) {
      AddOnMap = false;
    } else {

      /*

      root.setOnMouseClicked(
          mouseEvent -> {
            xcoord = mouseEvent.getSceneX() * 5;
            ycoord = mouseEvent.getSceneY() * 5;
            xinitial.setText(Integer.toString((int) xcoord));
            yinitial.setText(Integer.toString((int) ycoord));
          });



      xcoord = event.getSceneX() * 5;
      ycoord = event.getSceneY() * 5;

      xinitial.setText(Integer.toString((int) xcoord));
      yinitial.setText(Integer.toString((int) ycoord));

      AddOnMap = true;
    }

  }*/
  @FXML
  void helpClicked(MouseEvent event) throws IOException {
    MapEditorHelpController controller =
        (MapEditorHelpController) Navigation.getController(Screen.MAPEDITORHELP);
    Stage stage = newStage("Help", Screen.MAPEDITORHELP);
    controller.setStage(stage);
    stage.show();
    stage.centerOnScreen();
  }

  void refresh() {
    Navigation.navigate(Screen.GRAPHICAL_MAP_EDITOR);
  }
}
