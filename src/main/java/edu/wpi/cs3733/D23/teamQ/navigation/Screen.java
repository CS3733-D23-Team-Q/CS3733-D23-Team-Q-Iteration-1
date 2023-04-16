package edu.wpi.cs3733.D23.teamQ.navigation;

public enum Screen {
  ROOT("views/Root.fxml", "Root"),
  HOME("views/Home.fxml", "Home"),
  HOME2("views/Home2.fxml", "Home"),
  LOGIN("views/LoginPage.fxml", "Login"),
  CREATE_ACCOUNT("views/CreateAccount.fxml", "Create Account"),
  FORGOT_PASSWORD("views/ForgotPassword.fxml", "Reset Password"),
  MAP_EDITOR_TABLE("views/MapEditor.fxml", "Map Editor Table"),
  GRAPHICAL_MAP_EDITOR("views/GraphicalMapEditor.fxml", "Graphical Map Editor"),
  PATH_TEXT("views/PathText.fxml", "Textual Path"),
  PATH_FINDING("views/Pathfinding.fxml", "Graphical Pathfinding"),
  SIGNAGE("views/Signage.fxml", "Signage"),
  HELP("views/Help.fxml", "Help"),
  SERVICE_REQUEST_HUB("views/ServiceRequestHub.fxml", "Selector"),
  SERVICE_PLACEHOLDER("views/ServiceRequestPlaceholder.fxml", "Placeholder"),
  CONFERENCE_ROOM_REQUEST("views/ConferenceRoomRequest.fxml", "Conference Room Request"),
  FLOWER_REQUEST("views/FlowerRequest.fxml", "Flower Request"),
  FLOWER_REQUEST_SUBMISSION("views/FlowerRequestSubmission.fxml", "Flower Request Submission"),
  SETTINGS("views/Settings.fxml", "Settings"),
  CONFERENCE_ROOM_REQUEST_DISPLAY(
      "views/ConferenceRoomRequestDisplay.fxml", "Conference Room Request Display"),
  FLOWER_REQUEST_DISPLAY("views/FlowerRequestDisplay.fxml", "Flower Request Display"),
  MEAL_REQUEST("views/MealDeliveryRequest.fxml", "Meal Request"),
  MEAL_REQUEST_DISPLAY("views/MealDeliveryRequestDisplay.fxml", "Meal Request Display"),
  OFFICE_SUPPLIES_REQUEST("views/OfficeSuppliesRequest.fxml", "Office Supplies Request"),
  OFFICE_SUPPLIES_REQUEST_DISPLAY(
      "views/OfficeSuppliesRequestDisplay.fxml", "Office Supplies Request Display"),
  FURNITURE_REQUEST("views/FurnitureDeliveryRequest.fxml", "Furniture Request"),
  FURNITURE_REQUEST_DISPLAY(
      "views/FurnitureDeliveryRequestDisplay.fxml", "Furniture Request Display"),
  LIST_REQUESTS("views/ListServiceRequests.fxml", "List of Service Requests"),
  PATIENT_TRANSPORT_REQUEST("views/PatientTransportRequest.fxml", "Patient Transport Request"),
  EDIT_PROFILE("views/EditProfile.fxml", "Edit Profile Page"),
  PROFILE_PAGE("views/ProfilePage.fxml", "Profile Page"),

  CONFIRM("views/Confirm.fxml", "Confirm"),
  ALERT("views/Alert.fxml", "Alert"),
  Move_Table("views/Move.fxml", "MoveTable"),
  Node_Table("views/Node.fxml", "MoveTable"),
  LocationName_Table("views/Location.fxml", "LocationNameTable"),
  Edge_Table("views/Edge.fxml", "EdgeTable"),
  MENU_PANE("views/MenuRoot.fxml", "Menu Pane"),

  DIRECTORY("views/Directory.fxml", "Directory"),
  STATISTICS("views/Statistics.fxml", "Statistics"),
  SUBMISSION("views/SubmissionPage.fxml", "Service Request Submitted"),

  MAPEDITORHELP("views/MapEditorHelp.fxml", "Help");

  private final String filename;
  private final String title;

  Screen(String filename, String title) {
    this.filename = filename;
    this.title = title;
  }

  public String getFilename() {
    return filename;
  }

  public String getTitle() {
    return title;
  }
}
