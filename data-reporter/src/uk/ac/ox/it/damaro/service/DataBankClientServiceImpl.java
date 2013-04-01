package uk.ac.ox.it.damaro.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import uk.ac.ox.it.damaro.databank.ws.restful.client.Datasets;
import uk.ac.ox.it.damaro.databank.ws.restful.client.Silos;
import uk.ac.ox.it.damaro.databank.ws.restful.client.WebResourceBuilder;

import com.sun.jersey.api.client.WebResource;

@Service
public class DataBankClientServiceImpl implements DataBankClientService{
	
	
	public List<String> getSilos(){
		
		System.out.println("DatabankClient.getSilos()");
		WebResource webResource = WebResourceBuilder.getInstance(
				"http://163.1.127.173/", "eidcsr", "eidcsr").getWebResource();

		System.out.println("Silos().getAllSilosAsJSON()");
		String[] silosName = new Silos().getAllSilosAsJSON(webResource);

		List<String> silosNameList = Arrays.asList(silosName);		
		
		return silosNameList;
	}
	
	public List<String> getDatasets(String siloName){
		System.out.println("DatabankClient.getDatasets()");
		
		WebResource webResource = WebResourceBuilder.getInstance(
				"http://163.1.127.173/", "eidcsr", "eidcsr").getWebResource();
		
		String[] dataPackagesName = new Silos().getSiloAsJSON(webResource, siloName);

		List<String> dataPackagesNameList = Arrays.asList(dataPackagesName);		
		
		return dataPackagesNameList;
	}
	
	public List<String> getItems(String siloName, String datasetName){
		WebResource webResource = WebResourceBuilder.getInstance(
				"http://163.1.127.173/", "eidcsr", "eidcsr").getWebResource();
		
		webResource = webResource.path(siloName);
		String[] itemsName = new Datasets().getDataSetAsXML(webResource, "datasets", datasetName);
		List<String> itemsNameList = Arrays.asList(itemsName);		
		
		return itemsNameList;
	}

}
