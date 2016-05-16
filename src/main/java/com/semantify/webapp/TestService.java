package com.semantify.webapp;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import java.io.StringWriter;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Test Service for testing Jersey and RDF creation in Jena
 */
@Path("/rdf")
public class TestService {

    @GET
    public Response getMessage() {
        String NS = "http://semantify.io/test/";
        Model m = ModelFactory.createDefaultModel();

        Resource res = m.createResource(NS + "alex");
        Property prop = m.createProperty(NS + "studyAt");

        res.addProperty(prop, "The University of Bonn", XSDDatatype.XSDstring);
        StringWriter strWriter = new StringWriter();
        m.write(strWriter, "Turtle");
        m.write(System.out, "Turtle");
        String output = "RDF Output" + strWriter.toString();
        return Response.status(200).entity(output).build();
    }
}