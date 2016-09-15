package com.semantify.webapp;

import com.google.gson.Gson;
import opcua.ontmalizer.OntmalizerController;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


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
            @FormDataParam("instance") FormDataContentDisposition instanceFileName,
            @FormDataParam("ontName") String ontName
    ) {

        boolean areValidXML = validateXML(schemaInputStream, instanceInputStream);

        if (areValidXML) {
            OntHandler oh = new OntHandler(ontName, schemaInputStream, instanceInputStream);
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
            FileOutputStream fs;
            String filename = "src/main/resources/data/" + this.ontName + ".ttl";
            File f = new File(filename);
            fs = new FileOutputStream(f);
            this.ontController.getOntology().writeOntology(fs, "N3");
            this.ontController.getModel().writeModel(fs, "N3");
            fs.close();
        } catch (Exception e) {

        }
    }
}
