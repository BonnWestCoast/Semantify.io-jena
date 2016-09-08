package com.semantify.webapp;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import org.apache.jena.rdf.model.Model;

@Path("/ontologies")
public class OntologyService {

    private static String path = "";

    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {

        Element element = null;
        ArrayList<Element> list = new ArrayList<Element>();

        RDFStoreController controller = new RDFStoreController();
        List<String> result = controller.listOntologies();

        for (String nameOntology: result) {
            element = new Element(0, nameOntology);
            list.add(element);
        }

        String json = new Gson().toJson(new elementList(list));
        return Response.status(200).entity(json).build();

    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage( @PathParam("id") String id ) {

        RDFStoreController controller = new RDFStoreController();
        // NOTE: Right now the ID is the name of the model
        String m = controller.getSchemaByName(id);

        Element element = new Element(0, id, m);
        String json = new Gson().toJson(element);
        return Response.status(200).entity(json).build();
    }

}
