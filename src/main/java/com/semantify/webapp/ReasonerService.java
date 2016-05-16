package com.semantify.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.ValidityReport.Report;

/**
 * Created by aw3s0_000 on 16.05.2016.
 * REST Service for ontology validation.
 */
@Path("/reasoner")
public class ReasonerService {

    /**
     * Inconsistency validation method. true - if consistent, otherwise - false.
     * @return str
     */
    @GET
    public Response Validate() {
        //TODO: reimplement for post when front-end is ready
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream tboxS = classLoader.getResourceAsStream("data/tbox.owl");
        InputStream aboxS = classLoader.getResourceAsStream("data/abox.owl");
        Model tbox = ModelFactory.createDefaultModel();
        tbox.read(tboxS, "RDF/XML");
        Model abox = ModelFactory.createDefaultModel();
        abox.read(aboxS, "RDF/XML");
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(tbox.getGraph());

        InfModel inf = ModelFactory.createInfModel(reasoner, abox);
        Boolean result;
        ValidityReport validityReport = inf.validate();

        if ( !validityReport.isValid() ) {
            result = false;
            Iterator<Report> iter = validityReport.getReports();
            while ( iter.hasNext() ) {
                Report report = iter.next();
                System.out.println(report);
            }
        } else {
            result = true;
        }

        String output = result ? "Valid" : "Inconsistent";
        System.out.println(output);
        return Response.status(200).entity(output).build();
    }

    public static void main(String[] args) {
        ReasonerService service = new ReasonerService();
        //service.Validate("file.owl");
    }
}
