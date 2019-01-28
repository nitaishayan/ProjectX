package Common;

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
public MemberStatusDetails(String executionDate, String previousStatus, String currentStatus) {
	super();
	this.executionDate = executionDate;
	this.previousStatus = previousStatus;
	this.currentStatus = currentStatus;
}
public MemberStatusDetails() {
	super();
	this.executionDate = "";
	this.previousStatus ="";
	this.currentStatus = "";
}
}
