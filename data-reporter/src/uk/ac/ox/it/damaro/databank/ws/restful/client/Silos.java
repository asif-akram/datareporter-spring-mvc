/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ox.it.damaro.databank.ws.restful.client;

import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Asif Akram
 */
public class Silos {

    public void getAllSilosAsHTML(WebResource webResource) {
        String defaultPath = "silos";
        String response = webResource.path(defaultPath).accept(MediaType.TEXT_HTML_TYPE).get(String.class);
        System.out.println(response);

    }

    public String[] getAllSilosAsJSON(WebResource webResource) {
        String[] silosName = null;
        String defaultPath = "silos";
        String response = webResource.path(defaultPath).accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(response);
        
        
        try {
            org.json.JSONArray blah = new org.json.JSONArray(response);
            silosName = new String[blah.length()];
            for(int i = 0; i < blah.length(); i++){
                silosName[i] = blah.getString(i);
                System.out.println(blah.getString(i));
            }
        } catch (org.json.JSONException exp) {
            exp.printStackTrace();
        }
        return silosName;
    }

    public void getSiloAsHTML(WebResource webResource, String silo) {
        String response = webResource.path(silo).accept(MediaType.TEXT_HTML_TYPE).get(String.class);
        System.out.println(response);
    }

    public String[] getSiloAsJSON(WebResource webResource, String silo) {
        String[] dataPakagesName = null;
        String response = webResource.path(silo).accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON).get(String.class);
        //System.out.println(response);

        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(response);
            //System.out.println(jsonObject.length());
            dataPakagesName = new String[jsonObject.length()];
            java.util.Iterator keys = jsonObject.keys();
            int counter = 0;
            while (keys.hasNext()) {
                String dataPackage = keys.next().toString();
                dataPakagesName[counter++] = dataPackage;
                System.out.println(dataPackage);
                org.json.JSONArray blah = jsonObject.getJSONArray(dataPackage);
                for (int i = 0; i < blah.length(); i++) {
                    //System.out.println(blah.getString(i));
                }
            }
        } catch (org.json.JSONException exp) {
            exp.printStackTrace();
        }     
        return dataPakagesName;
    }
}
