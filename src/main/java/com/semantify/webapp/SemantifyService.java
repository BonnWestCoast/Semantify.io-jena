package com.semantify.webapp;

import com.google.gson.Gson;
import opcua.ontmalizer.OntmalizerController;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        // validateXml
        boolean xmlValid = validateXML(schemaInputStream, instanceInputStream);

        if (!xmlValid) {
            Error error = new Error("Validating instance against schema is failed!");
            String json = new Gson().toJson(error);
            return Response.status(500).entity(json).build();
        }

        int idOntology = -1;
        try {
            idOntology = handleOntology(schemaInputStream, instanceInputStream);

            Success success = new Success(idOntology);
            String json = new Gson().toJson(success);
            return Response.status(200).entity(json).build();
        } catch(Exception ex) {

            Error error = new Error(ex.getMessage());
            String json = new Gson().toJson(error);
            return Response.status(500).entity(json).build();
        }
    }

    /**
     *
     * @param XMLSchema
     * @param XMLInstance
     * @return
     */
    private Boolean validateXML(InputStream XMLSchema, InputStream XMLInstance) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(XMLSchema));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(XMLInstance));
            return true;

        } catch(SAXException e) {
            LOGGER.error("{}", e.getMessage());
            return false;
        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage());
            // ToDo: Returns also the error message
            return false;
        }
    }

//    @Ignore
//    public void testValidateXML() {
//        try {
//            InputStream isXSD = new FileInputStream("src/main/resources/opc_ua/UANodeSet.xsd");
//            InputStream isXML = new FileInputStream("src/main/resources/opc_ua/Opc.Ua.NodeSet2.xml");
//
//            boolean test = validateXML(isXSD, isXML);
//            if (test){
//                LOGGER.info("{}", "valid");
//            } else {
//                LOGGER.error("{}", "invalid");
//            }
//            //assert test;
//        } catch (Exception e) {
//            LOGGER.error("{}", e.getMessage());
//        }
//    }



    /**
     *
     * @param schemaInputStream
     * @param instanceInputStream
     * @return
     */
    private int handleOntology(InputStream schemaInputStream, InputStream instanceInputStream) {
        /*
        * convertOntology
        * validateOntology
        * storeOntology
        */

        int idSchema = 0;
        int idInstance = 0;
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

    /**
     *
     * @param XMLSchema
     * @param XMLInstance
     * @return
     */
    private List<String> convertOntology(InputStream XMLSchema, InputStream XMLInstance) {
        // Implement here the conversion calling Ontmalizer and store the result in the ontologies variable

        OntmalizerController ontController = new OntmalizerController(XMLSchema, XMLInstance);
        ontController.createOPCUAOntology();

        // test
        try {
            InputStream typesOnt = new FileInputStream("src/main/resources/output/Types_Ont.ttl");
            ontController.importOntology(typesOnt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ontController.createOPCUAInstances();

        ontController.getOntology();
        ontController.getModel();

        return null;

    }

    private boolean validateOntology(String ontology) {
        /*
         decide which is better here, if it is better to pass an string, a path of file name or a File object or an
         InputStream
         */
        boolean isValid = true;
        return isValid;
    }
}
