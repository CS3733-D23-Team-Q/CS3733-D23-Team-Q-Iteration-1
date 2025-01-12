package edu.wpi.cs3733.D23.teamQ.db.obj;

import edu.wpi.cs3733.D23.teamQ.db.dao.IServiceRequest;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceRequest extends ServiceRequest implements IServiceRequest {
  private String dateTime;
  private String foodChoice;

  public ConferenceRequest(
      int requestID,
      Node node,
      String requester,
      String assignee,
      int progress,
      String specialInstructions,
      Date date,
      String time,
      String foodChoice) {
    super(requestID, requester, progress, assignee, node, specialInstructions, date, time);
    this.foodChoice = foodChoice;
  }

  public ConferenceRequest(
      String requester,
      int progress,
      String assignee,
      Node node,
      String specialInstructions,
      Date date,
      String time,
      String foodChoice) {
    super(0, requester, progress, assignee, node, specialInstructions, date, time);
    this.foodChoice = foodChoice;
  }

  public int progressToInt(Progress progress) {
    if (progress == Progress.BLANK) {
      return 0;
    } else if (progress == Progress.PROCESSING) {
      return 1;
    } else {
      return 2;
    }
  }
}
