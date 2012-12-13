package uk.ac.ox.it.damaro.databank.ws.restful.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 *
 * @author Asif Akram
 */
public class WebResourceBuilder {

    private String url;
    private String userName;
    private String password;
    private WebResource webResource;

    private static WebResourceBuilder instance;

    private WebResourceBuilder(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    private void createWebResource(){
        HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(userName, password);
       Client client = Client.create();
       client.addFilter(authFilter);
       webResource = client.resource(url);
    }
    public static WebResourceBuilder getInstance(String url, String userName, String password){
        if(instance == null){
            instance = new WebResourceBuilder(url, userName, password);
            instance.createWebResource();
        }
        return instance;
    }

    /**
     * Get the value of webResource
     *
     * @return the value of webResource
     */
    public WebResource getWebResource() {
        return webResource;
    }

}
