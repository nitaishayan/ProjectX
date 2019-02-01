package Common;

public class ActivityReport {
	private String executionTime;
	private String startTime;
	private String endTime;
	private String activeMembers;
	private String freezeMembers;
	private String lockedMembers;
	private String loanCopies;
	private String memberDelay;
	public String getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public ActivityReport(String executionTime, String startTime, String endTime, String activeMembers,
			String freezeMembers, String lockedMembers, String loanCopies, String memberDelay) {
		super();
		this.executionTime = executionTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activeMembers = activeMembers;
		this.freezeMembers = freezeMembers;
		this.lockedMembers = lockedMembers;
		this.loanCopies = loanCopies;
		this.memberDelay = memberDelay;
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
		return memberDelay;
	}
	public void setMemberDelay(String memberDelay) {
		this.memberDelay = memberDelay;
	}
}
