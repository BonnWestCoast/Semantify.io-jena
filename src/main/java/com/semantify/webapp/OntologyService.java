package com.semantify.webapp;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("/ontologies")
public class OntologyService {


    /**
     * Recovers the specified model
     * @param id the name of the model
     * @return the model in String format
     */
    @GET
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage( @PathParam("id") String id ) {

        Ontology ontology = null;
        String json = "";
        int status = 200;

        RDFStoreController controller = new RDFStoreController();
        // NOTE: Right now the ID is the name of the model
        String m = controller.getSchemaByName(id, false);

        if ( m.isEmpty() ) {

            status = 500;
            String message = String.format("The schema/instance %s does not exist!!!", id );
            Error error = new Error(message);
            json = new Gson().toJson(error);

        } else {

            ontology = new Ontology(m);
            json = new Gson().toJson(ontology);

        }

        return Response.status(status).entity(json).build();
    }


    /**
     * Recovers the specified model
     * @param id the name of the model
     * @return the model in String format
     */
    @GET
    @Path("/visualizer/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVisualizedMessage( @PathParam("id") String id ) {

        Ontology ontology = null;
        String json = "";
        int status = 200;

        RDFStoreController controller = new RDFStoreController();
        // NOTE: Right now the ID is the name of the model
        String m = controller.getSchemaByName(id, true);

        if ( m.isEmpty() ) {
            status = 500;
            String message = String.format("The schema/instance %s does not exist!!!", id );
            Error error = new Error(message);
            json = new Gson().toJson(error);
        } else {
            ontology = new Ontology(m);
            json = new Gson().toJson(ontology);
        }

        return Response.status(status).entity(json).build();
    }


    /**
     * Retrieves a list of all models in the dataset
     * @return a list of all models in a string formatted as JSON
     */
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {

        Ontology ontology = null;
        ArrayList<Ontology> data = new ArrayList<Ontology>();

        RDFStoreController controller = new RDFStoreController();
        List<String> result = controller.listOntologies();

        for (String nameOntology: result) {
            ontology = new Ontology(nameOntology, nameOntology);
            data.add(ontology);
        }

        OntologyList ontologyList = new OntologyList(data);
        String json = new Gson().toJson(ontologyList);

        return Response.status(200).entity(json).build();

    }


    /**
     * A SPARQL query is executed in a model
     * @param ontology with this variable we access to the specified model
     * @param requestQuery is the String
     * @return the triples in String format
     */
    @POST
    @Path("/query/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response SPARQLQuery(
        @PathParam("id") String ontology,
        RequestQuery requestQuery
    ) {

        /**
         * ToDo
         * Customize answer if there ontology does not exist, error message
         */

        String query = requestQuery.getData();

        RDFStoreController controller = new RDFStoreController();

        /* Return query result and also stats */
        String queryResult = controller.queryOntology(ontology, query);
        String stats = controller.getStatsFromOntology(ontology);

        ResponseQuery response = new ResponseQuery(queryResult, stats);
        String json = new Gson().toJson(response);

        return Response.status(200).entity(json).build();

    }

}
