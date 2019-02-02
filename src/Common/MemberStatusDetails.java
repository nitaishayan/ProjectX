package Common;

/**
 * Class that appeared as an object in the MemberStatusHistoryGUI
 * this class is initialized and enter to the table view
 * @author nitay shayan
 *
 */
public class MemberStatusDetails {
private String executionDate;
private String previousStatus;
private String currentStatus;
public String getCurrentStatus() {
	return currentStatus;
}
public void setCurrentStatus(String currentStatus) {
	this.currentStatus = currentStatus;
}
public String getPreviousStatus() {
	return previousStatus;
}
public void setPreviousStatus(String previousStatus) {
	this.previousStatus = previousStatus;
}
public String getExecutionDate() {
	return executionDate;
}
public void setExecutionDate(String executionDate) {
	this.executionDate = executionDate;
}
/**
 * 
 * @param executionDate		The execution date for the status change
 * @param previousStatus	The previous status of the member
 * @param currentStatus		The current status of the member
 */
public MemberStatusDetails(String executionDate, String previousStatus, String currentStatus) {
	super();
	this.executionDate = executionDate;
	this.previousStatus = previousStatus;
	this.currentStatus = currentStatus;
}
/**
 * Default constructor
 */
public MemberStatusDetails() {
	super();
	this.executionDate = "";
	this.previousStatus ="";
	this.currentStatus = "";
}
}
