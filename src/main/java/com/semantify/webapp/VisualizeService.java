package com.semantify.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
//import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Created by aw3s0_000 on 09.06.2016.
 * REST Service for ontology validation.
 */
@Path("/visualize")
public class VisualizeService {

    /**
     * Convert from OWL (TTL, RDF) to VOWL format, which is used on client side for visualization
     * @return json response
     */
    @GET
    public Response Convert() {
        //TODO: reimplement for post when front-end is ready
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream testT = classLoader.getResourceAsStream("data/full_ontobench_test.ttl");
        Owl2Vowl converter = new Owl2Vowl(testT);
        String output = converter.getJsonAsString();
        File file = new File("example.txt");
        converter.writeToFile(file);

        System.out.println(output);
        return Response.status(200).entity(output).build();
    }

    public static void main(String[] args) {
        VisualizeService service = new VisualizeService();
        service.Convert();
    }
}
