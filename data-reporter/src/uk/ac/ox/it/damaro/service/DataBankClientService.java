package uk.ac.ox.it.damaro.service;

import java.util.List;

public interface DataBankClientService {
	
	public List<String> getSilos();
	
	public List<String> getDatasets(String siloName);
	
	public List<String> getItems(String siloName, String datasetName);

}
