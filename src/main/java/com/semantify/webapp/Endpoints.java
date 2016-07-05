package com.semantify.webapp;

import javax.ws.rs.*;
import java.io.*;
import java.util.ArrayList;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import opcua.ontmalizer.XML2OWL;
import opcua.ontmalizer.XSD2OWL;

/**
 * This is a POJO class useful to return JSON format in the response of the services.
 */
class Element {

    int id;
    String name;
    String content;

    public Element() {}

    public Element(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Element(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

}

/**
 * This is a POJO class useful to return JSON format in the response of the services.
 */
class elementList {

    public ArrayList<Element> list;
    public elementList () {}
    public elementList(ArrayList<Element> list) {
        this.list = list;
    }
}

@Path("/instance")
public class Endpoints {

    @POST
    @Path("/new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public Response getMessage(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileName
    ) {

        String fileLocation = fileName.getFileName();
        DBController objectController = new DBController();
        objectController.storeFile(uploadedInputStream, fileName);

        //XSD2OWL conversor = new XSD2OWL();
        //conversor.createOPCUAOntology();

        return Response.status(200).build();
    }

    @GET
    @Path("/list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {

        DBController query = new DBController();
        ArrayList<Element> list = query.listElements();
        String json = new Gson().toJson(new elementList(list));
        return Response.status(200).entity(json).build();

    }

    @GET
    @Path("/load/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(
            @PathParam("id") String id
    ) {
        DBController query = new DBController();
        Element element = new Element();
        String json = new Gson().toJson(element);
        return Response.status(200).entity(json).build();
    }



}
