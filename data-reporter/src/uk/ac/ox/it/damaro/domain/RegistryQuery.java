package uk.ac.ox.it.damaro.domain;

public class RegistryQuery {
	
	private String registryBaseURL = "http://aura.abdn.ac.uk/dspace-oai/request";
	
	private String from = "2013-01-08";
	
	private String until = "2013-02-28";
	
	private int numberOfRecords = 10;
	
	public RegistryQuery(){
		
	}

	public String getRegistryBaseURL() {
		return registryBaseURL;
	}

	public void setRegistryBaseURL(String registryBaseURL) {
		this.registryBaseURL = registryBaseURL;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

}
