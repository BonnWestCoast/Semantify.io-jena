package com.semantify.webapp;

import javax.ws.rs.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.xml.sax.SAXException;

//import tr.com.srdc.ontmalizer.XSD2OWLMapper;



@Path("/ontologies")
public class OntologyService {

    private static String path = "";

    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {

        DBController query = new DBController();
        ArrayList<Element> list = query.listElements();
        String json = new Gson().toJson(new elementList(list));
        return Response.status(200).entity(json).build();

    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(
            @PathParam("id") String id
    ) {
        //DBController query = new DBController();
        Element element = new Element();
        String json = new Gson().toJson(element);
        return Response.status(200).entity(json).build();
    }

}
