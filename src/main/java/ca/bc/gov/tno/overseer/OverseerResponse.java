package ca.bc.gov.tno.overseer;

import java.util.Date;

public class OverseerResponse {
	
	Long duration = 0L;
	Long timestamp = new Date().getTime();
	Boolean success = true;
	String error = "No Error.";
	String status = "Uninitialized";
	String dbProfile = "Uninitialized";
	String instance = "Uninitialized";
	String runTime = "Uninitialized";
	String startTime = "Uninitialized";
	Integer threadsCompleted = -1;
	Long maxDuration = -1L;
	Long minDuration = -1L;
	int activeThreads = -1;
	String buildNumber = "Uninitialized";
	String serverDetails = "Uninitialized";
	String eventTypesHandled = "Uninitialized";
	String serverUser = "Unitialized";
	
	public Long getDuration() {
		return duration;
	}
	
	public void setDuration(Long value) {
		this.duration = value;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long value) {
		this.timestamp = value;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getConnectionStatus() {
		return status;
	}
	
	public void setConnectionStatus(String status) {
		this.status = status;
	}

	public String getDbProfileName() {
		return dbProfile;
	}
	           
	public void setDbProfileName(String dbProfile) {
		this.dbProfile = dbProfile;
	}

	public String getInstanceName() {
		return instance;
	}
	           
	public void setInstanceName(String instance) {
		this.instance = instance;
	}

	public String getInstanceRunTime() {
		return runTime;
	}
	           
	public void setInstanceRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getStartTime() {
		return startTime;
	}
	           
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getThreadsCompleted() {
		return threadsCompleted;
	}
	           
	public void setThreadsCompleted(Integer threadsCompleted) {
		this.threadsCompleted = threadsCompleted;
	}

	public Long getMaxDuration() {
		return maxDuration;
	}
	           
	public void setMaxDuration(Long maxDuration) {
		this.maxDuration = maxDuration;
	}

	public Long getMinDuration() {
		return minDuration;
	}
	           
	public void setMinDuration(Long minDuration) {
		this.minDuration = minDuration;
	} 
	
	public String getBuildNumber() {
		return buildNumber;
	}
	           
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	} 
	
	public int getActiveThreads() {
		return activeThreads;
	}
	           
	public void setActiveThreads(int activeThreads) {
		this.activeThreads = activeThreads;
	} 

	public String getServerDetails() {
		return serverDetails;
	}
	           
	public void setServerDetails(String serverDetails) {
		this.serverDetails = serverDetails;
	} 
	
	public String getEventTypesHandled() {
		return eventTypesHandled;
	}
	           
	public void setEventTypesHandled(String eventTypesHandled) {
		this.eventTypesHandled = eventTypesHandled;
	} 
	
	public String getServerUser() {
		return serverUser;
	}
	           
	public void setServerUser(String serverUser) {
		this.serverUser = serverUser;
	} 
}
