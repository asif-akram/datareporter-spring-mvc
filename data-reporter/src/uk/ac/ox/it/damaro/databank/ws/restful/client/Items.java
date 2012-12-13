/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ox.it.damaro.databank.ws.restful.client;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Asif Akram
 */
public class Items {

    private String pathKeyword = "items";

    /*
     * /{silo name}/items/{id}
     * GET Accept: *, authd - Returns JSON encoded a list of zipfiles in {id} with form to unpack the zip file
     */
    public void getItemsWithDatasetID(WebResource webResource, String datasetIDValue) {
        String response = webResource.path(pathKeyword).path(datasetIDValue).accept(/*MediaType.TEXT_HTML_TYPE*/ MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(response);
    }

    /*
     * /{silo name}/items/{id}
     * POST Unpack file. Create new dataset containing upacked file contents
     * Parameters-> filename = {name of file to be unpacked, including the path}
     * -> Id={(Optional)}. if Id is not supplied, the default dataset id for the new dataset will be {dataset_id}-{file_name}
     */
    public void unpackFileWithDatasetID(WebResource webResource, String datasetIDValue, String zipFileName, String newDataSetIDValue) {
        if (datasetIDValue == null || zipFileName == null) {
            return;
        }

        String fileNameKey = "filename";
        String newDataSetIDKey = "id";


        Form newForm = new Form();
        newForm.add(fileNameKey, zipFileName);

        if (newDataSetIDValue != null) {
            newForm.add(newDataSetIDKey, newDataSetIDValue);
        }

        ClientResponse response = webResource.path(pathKeyword).path(datasetIDValue).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, newForm);

        System.out.println(response.getStatus());
    }

    /*
      * /{silo name}/items/{id}/{path}
      */
     public void getDataSetWithSubPathAsHTML(WebResource webResource, String datasetIDValue, String subPathValue){
        webResource = webResource.path(pathKeyword).path(datasetIDValue).path(subPathValue);
        ClientResponse response = webResource.get(ClientResponse.class);
        System.out.println(response.getStatus());
     }

     /*
     * /{silo name}/items/{id}/{path}
     * POST Unpack file. Create new dataset containing upacked file contents
     * Parameters-> filename = {name of file to be unpacked, including the path}
     * -> Id={(Optional)}. if Id is not supplied, the default dataset id for the new dataset will be {dataset_id}-{file_name}
     */
    public void unpackFileWithDatasetWithSubPathID(WebResource webResource, String datasetIDValue, String subPathValue, String zipFileName, String newDataSetIDValue) {
        if (datasetIDValue == null || zipFileName == null) {
            return;
        }

        String fileNameKey = "filename";
        String newDataSetIDKey = "id";


        Form newForm = new Form();
        newForm.add(fileNameKey, zipFileName);

        if (newDataSetIDValue != null) {
            newForm.add(newDataSetIDKey, newDataSetIDValue);
        }

        ClientResponse response = webResource.path(pathKeyword).path(datasetIDValue).path(subPathValue)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, newForm);

        System.out.println(response.getStatus());
    }
}

