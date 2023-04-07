package edu.wpi.teamQ.db.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRequest {

  private int requestID;
  private Progress progress;
  private String requester;
  private String assignee;
  private String roomNumber;
  private String specialInstructions;

  enum Progress {
    BLANK,
    PROCESSING,
    DONE
  }

  public ServiceRequest(
      int requestID,
      String requester,
      int progress,
      String assignee,
      String roomNumber,
      String specialInstructions) {
    this.requestID = requestID;
    if (progress == 0) {
      this.progress = Progress.BLANK;
    } else if (progress == 1) {
      this.progress = Progress.PROCESSING;
    } else if (progress == 2) {
      this.progress = Progress.DONE;
    } else {
      this.progress = null;
    }
    this.requester = requester;
    this.assignee = assignee;
    this.roomNumber = roomNumber;
    this.specialInstructions = specialInstructions;
  }
}
