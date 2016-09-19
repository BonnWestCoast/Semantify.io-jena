package com.semantify.webapp;

import com.google.gson.Gson;
import opcua.ontmalizer.OntmalizerController;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.junit.Test;
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
import java.io.*;


@Path("/ontologies")
public class SemantifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SemantifyService.class);

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


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(
            RequestOntology requestOntology
    ) {

        String schema = requestOntology.getSchema();
        String instance = requestOntology.getInstance();
        String ontName = requestOntology.getOntName();

        InputStream schemaIS = stringToInputStream(schema);
        InputStream instanceIS = stringToInputStream(instance);

        boolean areValidXML = validateXML(schemaIS, instanceIS);

        System.out.println("Valid XML: " + areValidXML);

        if (areValidXML) {

            schemaIS = stringToInputStream(schema);
            instanceIS = stringToInputStream(instance);

            OntHandler oh = new OntHandler(ontName, schemaIS, instanceIS);
            oh.convertOntology();
            oh.storeOntology();

            Success success = new Success(oh.ontName);
            String json = new Gson().toJson(success);
            return Response.status(200).entity(json).build();

        } else {

            //ToDo: return a response with a error message
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
            return false;
        } catch (IOException ex) {
            // ToDo: Returns also the error message
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

            OntHandler oh = new OntHandler(ontName, schemaInputStream, instanceInputStream);
            oh.convertOntology();
            oh.storeOntology();

        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
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
    private InputStream schemaInputStream = null;
    private InputStream instanceInputStream = null;
    private OntmalizerController ontController = null;

    public OntHandler(String ontName, InputStream schemaInputStream, InputStream instanceInputStream) {
        this.ontName = ontName;
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
            this.ontController.getOntology().writeOntology(os, "N3");
            this.ontController.getModel().writeModel(os, "N3");

            String result = os.toString("UTF-8");
            InputStream is = stringToInputStream(result);
            RDFStoreController controller = new RDFStoreController();

            controller.storeOntology(this.ontName, is);
            //controller.cleanDataset();

        } catch (Exception e) {
            System.out.println("Error :" +  e.toString());
        }
    }
}
