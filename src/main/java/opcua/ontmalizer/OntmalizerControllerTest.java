package opcua.ontmalizer;


import java.io.*;

import org.junit.Test;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OntmalizerControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OntmalizerControllerTest.class);

    @Ignore
    public void testCreateOntology_OpcUa(){
        try {
            InputStream isXSD = new FileInputStream("src/main/resources/opc_ua/UANodeSet.xsd");
            InputStream isXML = null;
            OntmalizerController oc = new OntmalizerController(isXSD, isXML);
            oc.createOPCUAOntology();

            FileOutputStream fs;

            File f = new File("src/main/resources/output/UANodeSet_Ont.ttl");
            fs = new FileOutputStream(f);
            oc.getOntology().writeOntology(fs, "N3");
            fs.close();
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }
    @Ignore
    public void testCreateOntology_OpcUaTypes(){
        try {
            InputStream isXSD = new FileInputStream("src/main/resources/opc_ua/Types.xsd");
            InputStream isXML = null;
            OntmalizerController oc = new OntmalizerController(isXSD, isXML);
            oc.createOPCUATypesOntology();

            FileOutputStream fs;

            File f = new File("src/main/resources/output/Types_Ont.ttl");
            fs = new FileOutputStream(f);
            oc.getOntology().writeOntology(fs, "N3");
            fs.close();
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }
    @Test
    public void testCreateModel_OpcUa(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream isXSD = classLoader.getResourceAsStream("opc_ua/UANodeSet.xsd");
            InputStream isXML = classLoader.getResourceAsStream("opc_ua/Opc.Ua.NodeSet2.test.xml");

            OntmalizerController oc = new OntmalizerController(isXSD, isXML);

            oc.createOPCUAOntology();
            InputStream typesOnt = classLoader.getResourceAsStream("output/Types_Ont.ttl");
            oc.importOntology(typesOnt);

            FileOutputStream fs;
            File f = new File("src/main/resources/output/UANodeSet_Ont_Merged.ttl");
            fs = new FileOutputStream(f);
            oc.getOntology().writeOntology(fs, "N3");
            fs.close();

            oc.createOPCUAInstances();
            f = new File("src/main/resources/output/UANodeSet_Model.test.ttl");
            fs = new FileOutputStream(f);
            oc.getModel().writeModel(fs, "N3");
            fs.close();

        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }




    @Ignore
    public void testCreateOntology_OpcUaSample(){
        try {
            InputStream isXSD = new FileInputStream("src/main/resources/opc_ua/UANodeSet.sample.xsd");
            InputStream isXML = null;
            OntmalizerController oc = new OntmalizerController(isXSD, isXML);
            oc.createOPCUAOntology();

            FileOutputStream fs;

            File f = new File("src/main/resources/output/UANodeSet_Ont.sample.ttl");
            fs = new FileOutputStream(f);
            oc.getOntology().writeOntology(fs, "N3");
            fs.close();
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }
    @Ignore
    public void testCreateOntology_OpcUaTest(){
        try {
            InputStream isXSD = new FileInputStream("src/main/resources/opc_ua/UANodeSet.test.xsd");
            InputStream isXML = new FileInputStream("src/main/resources/opc_ua/UANodeSet.test.xml");
            OntmalizerController oc = new OntmalizerController(isXSD, isXML);
            oc.createOPCUAOntology();
            //oc.createOPCUAInstances();

            FileOutputStream fs;
            File f = new File("src/main/resources/output/UANodeSet_Ont.test.ttl");
            fs = new FileOutputStream(f);
            oc.getOntology().writeOntology(fs, "N3");
            fs.close();

//            FileOutputStream fs_model;
//            File f_model = new File("src/main/resources/output/UANodeSet_Model.test.ttl");
//            fs_model = new FileOutputStream(f_model);
//            oc.getModel().writeModel(fs_model, "N3");
//            fs_model.close();

        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }


    @Ignore
    public void testCreateOntology_OpcUaTypesTest(){
        try {
            InputStream isXSD = new FileInputStream("src/main/resources/opc_ua/Types.test.xsd");
            InputStream isXML = null;
            OntmalizerController oc = new OntmalizerController(isXSD, isXML);
            oc.createOPCUAOntology();

            FileOutputStream fs;

            File f = new File("src/main/resources/output/Types_Ont.test.ttl");
            fs = new FileOutputStream(f);
            oc.getOntology().writeOntology(fs, "N3");
            fs.close();
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }
}
