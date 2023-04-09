package edu.wpi.cs3733.D23.teamQ.db.impl;

import edu.wpi.cs3733.D23.teamQ.db.dao.GenDao;
import edu.wpi.cs3733.D23.teamQ.db.obj.FlowerRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlowerRequestDaoImpl implements GenDao<FlowerRequest, Integer> {
  private List<FlowerRequest> flowerRequests = new ArrayList<FlowerRequest>();
  private int nextID = 0;
  private NodeDaoImpl nodeTable;
  private static FlowerRequestDaoImpl single_instance = null;

  public static synchronized FlowerRequestDaoImpl getInstance(NodeDaoImpl nodeTable) {
    if (single_instance == null) single_instance = new FlowerRequestDaoImpl(nodeTable);

    return single_instance;
  }

  private FlowerRequestDaoImpl(NodeDaoImpl nodeTable) {
    populate();
    this.nodeTable = nodeTable;
    if (flowerRequests.size() != 0) {
      nextID = flowerRequests.get(flowerRequests.size() - 1).getRequestID() + 1;
    }
  }

  /**
   * returns a flowerRequest given a requestID
   *
   * @param requestID of conferenceRequest being retrieved
   * @return a conferenceRequest with the given nodeID
   */
  public FlowerRequest retrieveRow(Integer requestID) {
    try {
      int index = this.getIndex(requestID);
      return flowerRequests.get(index);
    } catch (Exception e) {
      System.out.println("No request found with ID: " + requestID);
    }
    return null;
  }

  /**
   * updates flowerRequest in list with a new flowerRequest
   *
   * @param requestID requestID of conferenceRequest being replaced
   * @param newRequest new conferenceRequest being inserted
   * @return true if successful
   */
  public boolean updateRow(Integer requestID, FlowerRequest newRequest) {
    try (Connection connection = GenDao.connect();
        PreparedStatement st =
            connection.prepareStatement(
                "UPDATE \"flowerRequest\" SET \"requestID\" = ?, requester = ?, progress = ?, assignee = ?, \"specialInstructions\" = ?, note = ?, \"typeOfFlower\" = ?, \"bouquetSize\" = ?, \"roomNum\" = ? "
                    + "WHERE \"requestID\" = ?")) {

      st.setInt(1, requestID);
      st.setString(2, newRequest.getRequester());
      st.setInt(3, newRequest.getProgress().ordinal());
      st.setString(4, newRequest.getAssignee());
      st.setString(5, newRequest.getSpecialInstructions());
      st.setString(6, newRequest.getNote());
      st.setString(7, newRequest.getFlowerType());
      st.setInt(8, newRequest.getNumberOfBouquets());
      st.setInt(9, newRequest.getNode().getNodeID());
      st.setInt(10, requestID);

      st.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    int index = this.getIndex(requestID);
    flowerRequests.get(index).setRequester(newRequest.getRequester());
    flowerRequests.get(index).setProgress(newRequest.getProgress());
    flowerRequests.get(index).setAssignee(newRequest.getAssignee());
    flowerRequests.get(index).setSpecialInstructions(newRequest.getSpecialInstructions());
    flowerRequests.get(index).setNote(newRequest.getNote());
    flowerRequests.get(index).setFlowerType(newRequest.getFlowerType());
    flowerRequests.get(index).setNumberOfBouquets(newRequest.getNumberOfBouquets());
    flowerRequests.get(index).setNode(newRequest.getNode());

    return true;
  }

  /**
   * deletes flowerRequest from list of flowerRequests
   *
   * @param requestID of flowerRequest being deleted
   * @return true if successfully deleted
   */
  public boolean deleteRow(Integer requestID) {
    try (Connection conn = GenDao.connect();
        PreparedStatement stmt =
            conn.prepareStatement("DELETE FROM \"flowerRequest\" WHERE \"requestID\" = ?")) {
      stmt.setInt(1, requestID);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    int index = this.getIndex(requestID);
    flowerRequests.remove(index);
    return true;
  }

  /**
   * adds a flowerRequest to the list
   *
   * @param request flowerRequest being added
   * @return true if successful
   */
  public boolean addRow(FlowerRequest request) {
    try (Connection conn = GenDao.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "INSERT INTO \"flowerRequest\"(\"requester\", \"progress\", \"assignee\", \"specialInstructions\", \"note\", \"typeOfFlower\", \"bouquetSize\", \"roomNum\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
      stmt.setString(1, request.getRequester());
      stmt.setInt(2, request.progressToInt(request.getProgress()));
      stmt.setString(3, request.getAssignee());
      stmt.setString(4, request.getSpecialInstructions());
      stmt.setString(5, request.getNote());
      stmt.setString(6, request.getFlowerType());
      stmt.setInt(7, request.getNumberOfBouquets());
      stmt.setInt(8, request.getNode().getNodeID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    request.setRequestID(nextID);
    nextID++;
    return flowerRequests.add(request);
  }

  @Override
  public boolean populate() {
    try {
      Connection conn = GenDao.connect();
      PreparedStatement pst = conn.prepareStatement("SELECT * FROM \"flowerRequest\"");
      ResultSet rs = pst.executeQuery();
      while (rs.next()) {
        flowerRequests.add(
            new FlowerRequest(
                rs.getInt("requestID"),
                rs.getString("requester"),
                rs.getInt("progress"),
                rs.getString("assignee"),
                nodeTable.retrieveRow(rs.getInt("node")),
                rs.getString("specialInstructions"),
                rs.getString("note"),
                rs.getString("typeOfFlower"),
                rs.getInt("bouquetSize")));
      }
      conn.close();
      pst.close();
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * gets index of given request in the list of requests
   *
   * @param requestID requestID being checked
   * @return value of index
   */
  private int getIndex(Integer requestID) {
    for (int i = 0; i < flowerRequests.size(); i++) {
      FlowerRequest x = flowerRequests.get(i);
      if (x.getRequestID() == requestID) {
        return i;
      }
    }
    throw new RuntimeException("No move found with ID " + requestID);
  }

  /**
   * function that gets all flower requests in the list
   *
   * @return all flower requests in list
   */
  public List<FlowerRequest> getAllRows() {
    return flowerRequests;
  }
}
