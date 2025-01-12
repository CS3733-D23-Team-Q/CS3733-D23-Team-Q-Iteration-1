package edu.wpi.cs3733.D23.teamQ.db.impl;

import edu.wpi.cs3733.D23.teamQ.db.dao.GenDao;
import edu.wpi.cs3733.D23.teamQ.db.obj.*;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRequestDaoImpl {
  private ObservableList<ServiceRequest> serviceRequests = FXCollections.observableArrayList();
  private GenDao conferenceRequestTable;
  private GenDao flowerRequestTable;
  private GenDao mealRequestTable;
  private GenDao furnitureRequestTable;
  private GenDao patientTransportRequestTable;
  private GenDao officeSuppliesRequestTable;

  private static ServiceRequestDaoImpl single_instance = null;

  public static synchronized ServiceRequestDaoImpl getInstance(
      ConferenceRequestDaoImpl conferenceRequestTable,
      FlowerRequestDaoImpl flowerRequestTable,
      MealRequestDaoImpl mealRequests,
      FurnitureRequestDaoImpl furnitureRequests,
      OfficeSuppliesRequestDaoImpl officeSuppliesRequests,
      PatientTransportRequestDaoImpl patientTransportRequests) {
    if (single_instance == null)
      single_instance =
          new ServiceRequestDaoImpl(
              conferenceRequestTable,
              flowerRequestTable,
              mealRequests,
              furnitureRequests,
              officeSuppliesRequests,
              patientTransportRequests);

    return single_instance;
  }

  private ServiceRequestDaoImpl(
      GenDao conferenceRequests,
      GenDao flowerRequests,
      GenDao mealRequests,
      GenDao furnitureRequests,
      GenDao officeSuppliesRequests,
      GenDao patientTransportRequests) {
    this.conferenceRequestTable = conferenceRequests;
    this.flowerRequestTable = flowerRequests;
    this.mealRequestTable = mealRequests;
    this.furnitureRequestTable = furnitureRequests;
    this.officeSuppliesRequestTable = officeSuppliesRequests;
    this.patientTransportRequestTable = patientTransportRequests;
    populate();
  }

  public boolean populate() {
    List<FlowerRequest> flowerRequests = flowerRequestTable.getAllRows();
    List<ConferenceRequest> conferenceRequests = conferenceRequestTable.getAllRows();
    List<MealRequest> mealRequests = mealRequestTable.getAllRows();
    List<FurnitureRequest> furnitureRequests = furnitureRequestTable.getAllRows();
    List<OfficeSuppliesRequest> officeSuppliesRequests = officeSuppliesRequestTable.getAllRows();
    List<PatientTransportRequest> patientTransportRequests =
        patientTransportRequestTable.getAllRows();

    serviceRequests.addAll(flowerRequests);
    serviceRequests.addAll(conferenceRequests);
    serviceRequests.addAll(mealRequests);
    serviceRequests.addAll(furnitureRequests);
    serviceRequests.addAll(officeSuppliesRequests);
    serviceRequests.addAll(patientTransportRequests);
    return true;
  }

  public List<ServiceRequest> getAllRows() {
    return serviceRequests;
  }

  public ServiceRequest retrieveRow(Integer ID) {
    int index = getIndex(ID);
    return serviceRequests.get(index);
  }

  public int getIndex(int requestID) {
    populate();
    for (int i = 0; i < serviceRequests.size(); i++) {
      ServiceRequest sr = serviceRequests.get(i);
      if (sr.getRequestID() == requestID) {
        return i;
      }
    }
    return -1;
  }
}
