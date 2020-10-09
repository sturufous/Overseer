package ca.bc.gov.tno.overseer;

public class Jorel2HostsDto {
	
	String hostUrl = "Uninitialized";
	Boolean graph = true;

	public String getHostUrl() {
		return this.hostUrl;
	}
	
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public Boolean getGraph() {
		return this.graph;
	}
	
	public void setGraph(Boolean graph) {
		this.graph = graph;
	}
}
