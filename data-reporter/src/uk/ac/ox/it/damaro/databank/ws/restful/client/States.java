/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ox.it.damaro.databank.ws.restful.client;

import com.sun.jersey.api.client.WebResource;
//import javax.ws.rs.core.MediaType;

/**
 *
 * @author Asif Akram
 */
public class States {

    private String pathKeyword = "states";

    /*
     * /{silo name}/states
     * GET Accept: * / none, authd -
     * Returns a JSON-encoded hash/dict, keys map with the silo name, base uri
     * and embargo information for each of the datasets in the silo {silo name}
     * as text/plain
     */
    public void getStates(WebResource webResource) {
        String response = webResource.path(pathKeyword).get(String.class);
        System.out.println(response);
    }

    /*
     * /{silo name}/states
     * GET Accept: * / none, authd -
     * Returns a JSON-encoded hash/dict, keys map with the detailed state information of the dataset id as text/plain
     * information of the dataset id as text/plain
     */
    public void getStatesWithDatasetID(WebResource webResource, String datasetIDValue) {
        String response = webResource.path(pathKeyword).path(datasetIDValue).get(String.class);
        System.out.println(response);
    }

}
