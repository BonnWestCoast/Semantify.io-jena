package com.semantify.webapp;

import com.google.gson.Gson;
import opcua.ontmalizer.OntmalizerController;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Path("/ontologies")
public class SemantifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SemantifyService.class);


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public Response getMessage(
            @FormDataParam("schema") InputStream schemaInputStream,
            @FormDataParam("instance") InputStream instanceInputStream,
            @FormDataParam("schema") FormDataContentDisposition schemaFileName,
            @FormDataParam("instance") FormDataContentDisposition instanceFileName
    ) {

        boolean areValidXML = validateXML(schemaInputStream, instanceInputStream);
        String idOntology = "";

        if (areValidXML) {

            idOntology = handleOntology(schemaInputStream, instanceInputStream);
            Success success = new Success(idOntology);
            String json = new Gson().toJson(success);
            return Response.status(200).entity(json).build();

        } else {

            //ToDo: return a response with a error message
            Error error = new Error("Something is wrong!");
            String json = new Gson().toJson(error);
            return Response.status(500).entity(json).build();
        }

    }

    private static Boolean validateXML(InputStream XMLSchema, InputStream XMLInstance) {

        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(XMLSchema));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(XMLInstance));
            return true;

        } catch(SAXException ex) {
            return false;
        } catch (IOException ex) {
            // ToDo: Returns also the error message
            return false;
        }
    }

    private static List<String> convertOntology(InputStream XMLSchema, InputStream XMLInstance) {

        List<String> ontologies = new ArrayList<String>();

        /*
        Implement here the conversion calling Ontmalizer and store the result in the ontologies variable
        XSD2OWLMapper mapping = new XSD2OWLMapper(XMLSchema);
        */

        OntmalizerController ontController = new OntmalizerController(XMLSchema, XMLInstance);
        ontController.createOPCUAOntology();

        // test
//        ClassLoader classLoader = getClass().getClassLoader();
//        InputStream typesOnt = new FileInputStream("src/main/resources/output/Types_Ont.ttl");
//        ontController.importOntology(typesOnt);


        ontController.createOPCUAInstances();

        ontController.getOntology();
        ontController.getModel();
        return null;
//        return ontologies;

    }

    private static boolean validateOntology(String ontology) {
        /*
         decide which is better here, if it is better to pass an string, a path of file name or a File object or an
         InputStream
         */
        boolean isValid = true;
        return isValid;
    }

    private static String handleOntology(InputStream schemaInputStream, InputStream instanceInputStream) {

        String idSchema = "";
        String idInstance = "";
        List<String> ontologies =  null;
        ontologies = convertOntology(schemaInputStream, instanceInputStream);

        /*
        String schema = ontologies.get(0);
        String instance = ontologies.get(1);
        boolean isValidSchema = validateOntology(schema);
        boolean isValidInstance = validateOntology(instance);
        if (isValidSchema && isValidInstance) {
            idSchema = storeOntology(schema);
            idInstance = storeOntology(instance);
        }
        */

        return idInstance;
    }

}