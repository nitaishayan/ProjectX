package Common;
/**
 * Class that present the history of Activity report requested by the librarian manager
 * the Class is shown in table view window
 * @author nitay shayan
 *
 */
public class ActivityReport {
	private String executionTime;
	private String startTime;
	private String endTime;
	private String activeMembers;
	private String freezeMembers;
	private String lockedMembers;
	private String loanCopies;
	private String member;
	
	public String getExecutionTime() {
		return executionTime;
	}
	
	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	/**
	 * 
	 * The constructor that generate object that will be show in the table view
	 * 
	 * @param executionTime		time of issuing the activity report
	 * @param startTime			the start time for the form
	 * @param endTime			the end time for the form
	 * @param activeMembers		the amount of active members in requested time
	 * @param freezeMembers		the amount of freeze members in requested time
	 * @param lockedMembers		the amount of locked members in requested time
	 * @param loanCopies		the amount of loan copies by members in requested time
	 * @param memberDelayed		the amount of  delaies by members in the requested time
	 */
	public ActivityReport(String executionTime, String startTime, String endTime, String activeMembers,
			String freezeMembers, String lockedMembers, String loanCopies, String memberDelayed) {
		super();
		this.executionTime = executionTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activeMembers = activeMembers;
		this.freezeMembers = freezeMembers;
		this.lockedMembers = lockedMembers;
		this.loanCopies = loanCopies;
		this.member = memberDelayed;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getActiveMembers() {
		return activeMembers;
	}
	public void setActiveMembers(String activeMembers) {
		this.activeMembers = activeMembers;
	}
	public String getFreezeMembers() {
		return freezeMembers;
	}
	public void setFreezeMembers(String freezeMembers) {
		this.freezeMembers = freezeMembers;
	}
	public String getLockedMembers() {
		return lockedMembers;
	}
	public void setLockedMembers(String lockedMembers) {
		this.lockedMembers = lockedMembers;
	}
	public String getLoanCopies() {
		return loanCopies;
	}
	public void setLoanCopies(String loanCopies) {
		this.loanCopies = loanCopies;
	}
	public String getMemberDelay() {
		return member;
	}
	public void setMemberDelay(String memberDelay) {
		this.member = memberDelay;
	}
}
