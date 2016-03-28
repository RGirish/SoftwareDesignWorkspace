package asu.girish.raman.crud.gradebook.graman1.netbeans.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javax.ws.rs.core.MediaType;

public class StudentProfilesClient {

    private final WebResource webResource;
    private final Client client;

    public StudentProfilesClient() {
        ClientConfig clientConfig = new DefaultClientConfig();
        client = Client.create(clientConfig);
        webResource = client.resource("http://localhost:8080/CRUD-GradeBook-graman1-NetBeans/webresources").path("StudentProfiles");
    }

    public ClientResponse createStudentProfile(String jsonRequestMessage) throws UniformInterfaceException {
        return webResource.path("createStudentProfile").type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonRequestMessage);
    }

    public ClientResponse readStudentProfile(int id) throws UniformInterfaceException {
        return webResource.path("readStudentProfile/" + String.valueOf(id)).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    }

    public ClientResponse updateStudentProfile(String jsonRequestMessage) throws UniformInterfaceException {
        return webResource.path("updateStudentProfile").type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonRequestMessage);
    }

    public ClientResponse deleteStudentProfile(int id) throws UniformInterfaceException {
        return webResource.path("deleteStudentProfile/" + String.valueOf(id)).delete(ClientResponse.class);
    }

    public void close() {
        client.destroy();
    }

}