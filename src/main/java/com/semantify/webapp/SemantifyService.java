package com.semantify.webapp;

import com.google.gson.Gson;
import opcua.ontmalizer.OntmalizerController;
import org.apache.commons.io.IOUtils;
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
import java.io.*;

/* Logging */
import org.apache.log4j.Logger;

@Path("/ontologies")
public class SemantifyService {


    /**
     * Conversion InputStream --> String
     * @param inputStream
     * @return
     */
    private String isToString (InputStream inputStream) {

        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String string = writer.toString();
        return string;

    }


    /**
     * Conversion String --> InputStream
     * @param string
     * @return
     */
    private InputStream stringToInputStream (String string) {

        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stream;

    }


    /**
     * Receives a JSON request, this request is transformed into a RequestOntology object with the next fields:
     * schema, instance, ontName, and ontFormat.
     * @param requestOntology
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(
            RequestOntology requestOntology
    ) {

        /* get the value of the fields */
        String schema = requestOntology.getSchema();
        String instance = requestOntology.getInstance();
        String ontName = requestOntology.getOntName();
        //String ontFormat = requestOntology.getOntFormat();
        String ontFormat = "N3";

        /* Validate both schema and instance */
        InputStream schemaIS = stringToInputStream(schema);
        InputStream instanceIS = stringToInputStream(instance);
        boolean areValidXML = validateXML(schemaIS, instanceIS);

        if (areValidXML) {

            schemaIS = stringToInputStream(schema);
            instanceIS = stringToInputStream(instance);

            OntHandler oh = new OntHandler(ontName, ontFormat, schemaIS, instanceIS);
            oh.convertOntology();
            oh.storeOntology();

            Success success = new Success(oh.ontName);
            String json = new Gson().toJson(success);
            return Response.status(200).entity(json).build();

        } else {

            Error error = new Error("Something is wrong!");
            String json = new Gson().toJson(error);
            return Response.status(500).entity(json).build();

        }
    }


    /**
     * Validation of uploaded XML against to the uploaded XSD
     * @param XMLSchema
     * @param XMLInstance
     * @return
     */
    private static Boolean validateXML(InputStream XMLSchema, InputStream XMLInstance) {

        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(XMLSchema));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(XMLInstance));
            return true;

        } catch(SAXException ex) {
            //log.error("Exception ", ex);
            return false;
        } catch (IOException ex) {
            // ToDo: Returns also the error message
            //log.error("Exception ", ex);
            return false;
        }
    }

    @Test
    public void testCreateModel_OpcUa(){

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream schemaInputStream = classLoader.getResourceAsStream("opc_ua/UANodeSet.xsd");
            InputStream instanceInputStream = classLoader.getResourceAsStream("opc_ua/Opc.Ua.NodeSet2.test.xml");
            String ontName = "Test_OPCUA";
            String ontFormat = "N3";

            OntHandler oh = new OntHandler(ontName, ontFormat, schemaInputStream, instanceInputStream);
            oh.convertOntology();
            oh.storeOntology();

        } catch (Exception e) {
            //log.error("Exception ", e);
            System.out.println("Exception " + e.getMessage());
        }
    }

}


class OntHandler {

    /**
     * Conversion String --> InputStream
     * @param string
     * @return
     */
    private InputStream stringToInputStream (String string) {

        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stream;

    }

    public String ontName = null;
    public String ontFormat = null;
    private InputStream schemaInputStream = null;
    private InputStream instanceInputStream = null;
    private OntmalizerController ontController = null;

    /**
     * @param ontName
     * @param ontFormat may be one of these values;
     * "RDF/XML","RDF/XML-ABBREV","N-TRIPLE","N3".
     * @param schemaInputStream
     * @param instanceInputStream
     */
    public OntHandler(String ontName, String ontFormat, InputStream schemaInputStream, InputStream instanceInputStream) {
        this.ontName = ontName;
        this.ontFormat = ontFormat;
        this.schemaInputStream = schemaInputStream;
        this.instanceInputStream = instanceInputStream;
        this.ontController = new OntmalizerController(schemaInputStream, instanceInputStream);
    }

    public boolean validateOntology() {
        boolean isValid = true;
        return isValid;
    }

    public void convertOntology() {
        ontController.createOPCUAOntology();
        ontController.importOPCUATypesOntology();
        ontController.createOPCUAInstances();
    }

    public void storeOntology() {

        try {

            /* Write model to string */
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            this.ontController.getOntology().writeOntology(os, ontFormat);
            this.ontController.getModel().writeModel(os, ontFormat);

            String result = os.toString("UTF-8");
            InputStream is = stringToInputStream(result);
            RDFStoreController controller = new RDFStoreController();

            controller.storeOntology(this.ontName, is);

        } catch (Exception e) {
            System.out.println("Error :" +  e.toString());
        }
    }

}
