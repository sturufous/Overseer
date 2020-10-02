package ca.bc.gov.tno.overseer;

public class MailResponse {
	
	String fromAddress = "Uninitialized";
	String toAddress = "Uninitialized";
	String hostAddress = "Uninitialized";
	String portNumber = "Uninitialized";
	
	public String getFromAddress() {
		return fromAddress;
	}
	
	public void setFromAddress(String value) {
		this.fromAddress = value;
	}
	
	public String getToAddress() {
		return toAddress;
	}
	
	public void setToAddress(String value) {
		this.toAddress = value;
	}
	
	public String getHostAddress() {
		return hostAddress;
	}
	
	public void setHostAddress(String value) {
		this.hostAddress = value;
	}
	
	public String getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber(String value) {
		this.portNumber = value;
	}
}
