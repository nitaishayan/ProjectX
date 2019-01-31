package Common;

/**
 * Class that appeared as an object in the DelayandLostDetailsTableViewGUI
 * this class is initialized and enter to the table view
 * @author nitay shayan
 *
 */
public class DelayandLostDetails {
private String copyName;
private String copyID;
private String Delay;

public DelayandLostDetails(String copyName, String copyID,String Delay) {
	super();
	this.copyName = copyName;
	this.copyID = copyID;
	this.setDelay(Delay);
	}
public DelayandLostDetails() {
	this.copyName="";
	this.copyID="";
}
public String getCopyName() {
	return copyName;
}
public void setCopyName(String copyName) {
	this.copyName = copyName;
}
public String getCopyID() {
	return copyID;
}
public void setCopyID(String copyID) {
	this.copyID = copyID;
}
public String getDelay() {
	return Delay;
}
public void setDelay(String c) {
	this.Delay = c;
}
}
