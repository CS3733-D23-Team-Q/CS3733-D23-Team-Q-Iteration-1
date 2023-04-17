package edu.wpi.cs3733.D23.teamQ.controllers;

import static edu.wpi.cs3733.D23.teamQ.SecondaryStage.newStage;

import edu.wpi.cs3733.D23.teamQ.Alert;
import edu.wpi.cs3733.D23.teamQ.db.Qdb;
import edu.wpi.cs3733.D23.teamQ.db.obj.Location;
import edu.wpi.cs3733.D23.teamQ.db.obj.Node;
import edu.wpi.cs3733.D23.teamQ.navigation.Navigation;
import edu.wpi.cs3733.D23.teamQ.navigation.Screen;
import java.io.File;
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
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

public class GraphicalMapEditorController {

  private List<Line> line = new ArrayList<>();

  private boolean displayEdges = false;
  @FXML private Label WhichFloor;
  List<Button> button = new ArrayList<>();

  File[] file = {
    new File("src/main/resources/01_thefirstfloor.png"),
    new File("src/main/resources/02_thesecondfloor.png"),
    new File("src/main/resources/03_thethirdfloor.png"),
    new File("src/main/resources/00_thelowerlevel1.png"),
    new File("src/main/resources/00_thelowerlevel2.png")
  };

  String[] images = new String[file.length];

  Image[] image = new Image[images.length];
  int currentIndex = 0;
  @FXML private ImageView imageView;

  @FXML private SplitMenuButton menu;

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

  @FXML private ImageView image1;

  @FXML private TextField nodeidinput;

  /**
   * update node
   *
   * @param event
   */
  @FXML
  void setclicked(MouseEvent event) {
    if (nodeIDAlertone(nodeidinput, alerts, image1)) {
      if (coordAlert(xinitial, yinitial, alerts, image1)) {
        if (locationAlert(longnameinitial, shortnameinitial, nodetypeinitial, alerts, image1)) {
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
    if (nodeIDAlertone(nodeidinput, alerts, image1)) {
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
    if (nodeIDAlertone(nodeidinput, alerts, image1)) {
      nodeid = Integer.parseInt(nodeidinput.getText());
      NodeInformation(nodeid);
      String floor = qdb.nodeTable.retrieveRow(nodeid).getFloor();
      if (!floor.equals(Floor(currentIndex))) {
        currentIndex = findFloor(floor);
        if (!button.isEmpty()) parent.getChildren().removeAll(button);
        button = addButtons(Floor(currentIndex));
        imageView.setImage(image[currentIndex]);
        setFloor(currentIndex);
      }
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
    if (nodeIDAlerttwo(nodeidinput, alerts, image1)) {
      if (coordAlert(xinitial, yinitial, alerts, image1)) {
        if (locationAlert(longnameinitial, shortnameinitial, nodetypeinitial, alerts, image1)) {
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
    button = addButtons(Floor(currentIndex));

    javafx.scene.Node node = parent;
    pane = new GesturePane(node);
    root.getChildren().add(pane);

    pane.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 2) {
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

    for (int i = 0; i < file.length; i++) {
      images[i] = file[i].toURI().toString();
    }

    for (int i = 0; i < images.length; i++) {
      image[i] = new Image(images[i]);
    }
    imageView.setImage(image[currentIndex]);
    WhichFloor.setText("The First Floor");
  }

  void shownext() {
    HideEdges();
    currentIndex++;
    if (!button.isEmpty()) parent.getChildren().removeAll(button);
    if (currentIndex < file.length) {
      button = addButtons(Floor(currentIndex));
      imageView.setImage(image[currentIndex]);
      // refresh();

    } else {
      currentIndex = 0;
      button = addButtons(Floor(currentIndex));
      imageView.setImage(image[currentIndex]);
    }
    setFloor(currentIndex);
  }

  public List<Button> addButtons(String floor) {
    List<Button> buttons = new ArrayList<>();
    List<Node> nodes = qdb.retrieveAllNodes();
    List<Node> L1nodes = new ArrayList<>();
    for (Node n : nodes) {
      if (n.getFloor().equals(floor)) {
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
      buttons.add(node);
    }
    return buttons;
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

  List<javafx.scene.shape.Line> addLines(List<Node> path) {
    List<javafx.scene.shape.Line> lines = new ArrayList<>();
    for (int i = path.size() - 1; i >= 1; i--) {
      Node n = path.get(i);
      Node next = path.get(i - 1);
      int x1 = n.getXCoord() / 5;
      int y1 = n.getYCoord() / 5;
      int x2 = next.getXCoord() / 5;
      int y2 = next.getYCoord() / 5;
      javafx.scene.shape.Line line = new Line(x1, y1, x2, y2);
      line.setStyle("-fx-stroke: blue;");
      line.setStrokeWidth(0.5);
      parent.getChildren().add(line);
      lines.add(line);
    }
    displayEdges = true;
    return lines;
  }

  void removeLines(List<Line> lines) {
    if (lines.size() > 0) {
      for (Line line : lines) {
        parent.getChildren().remove(line);
      }
    }
  }

  List<Node> chooseLines(int floor) {
    List<Node> path = new ArrayList<>();
    for (int i = 0; i < qdb.edgeTable.getAllRows().size(); i++) {
      Node start = qdb.edgeTable.getAllRows().get(i).getStartNode();
      Node target = qdb.edgeTable.getAllRows().get(i).getEndNode();
      if (nodeOnTheFloor(start, floor) && nodeOnTheFloor(target, floor)) {
        path.add(start);
        path.add(target);
      }
    }
    return path;
  }

  @FXML
  void NextClicked(MouseEvent event) {
    shownext();
  }

  String Floor(int x) {
    if (x == 0) {
      return "1";
    } else if (x == 1) {
      return "2";
    } else if (x == 2) {
      return "3";
    } else if (x == 3) {
      return "L1";
    } else {
      return "L2";
    }
  }

  @FXML
  void LastClicked(MouseEvent event) {
    showlast();
  }

  void showlast() {
    HideEdges();
    currentIndex--;
    if (!button.isEmpty()) parent.getChildren().removeAll(button);
    if (currentIndex > 0) {
      button = addButtons(Floor(currentIndex));
      imageView.setImage(image[currentIndex]);
      // refresh();

    } else {
      currentIndex = 4;
      button = addButtons(Floor(currentIndex));
      imageView.setImage(image[currentIndex]);
    }
    setFloor(currentIndex);
  }

  void setFloor(int x) {
    if (x == 0) {
      WhichFloor.setText("The First Floor");
    } else if (x == 1) {
      WhichFloor.setText("The Second Floor");
    } else if (x == 2) {
      WhichFloor.setText("The Third Floor");
    } else if (x == 3) {
      WhichFloor.setText("The Lower Level 1");
    } else {
      WhichFloor.setText("The Lower Level 2");
    }
  }

  int findFloor(String floor) {
    if (floor.equals("1")) {
      return 0;
    } else if (floor.equals("2")) {
      return 1;
    } else if (floor.equals("3")) {
      return 2;
    } else if (floor.equals("L1")) {
      return 3;
    } else {
      return 4;
    }
  }

  @FXML
  void EdgesDispalyClicked(MouseEvent event) {
    line = addLines(chooseLines(currentIndex));
  }

  @FXML
  void EdgesHidingClicked(MouseEvent event) {
    HideEdges();
  }

  boolean nodeOnTheFloor(Node node, int floor) {
    if (node.getFloor().equals(Floor(floor))) return true;
    return false;
  }

  void HideEdges() {
    if (displayEdges == true) removeLines(line);
    displayEdges = false;
  }
}
