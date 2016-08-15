package opcua.ontmalizer;

import tr.com.srdc.ontmalizer.XSD2OWLMapper;

import java.io.File;
import java.io.FileOutputStream;

//import java.io.FileWriter;
//import java.io.Writer;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by shinho on 6/28/16.
 */
public class XSD2OWL {

//    private static final Logger LOGGER = LoggerFactory.getLogger(XSD2OWL.class);

    public void createOPCUAOntology() {

        // This part converts XML schema to OWL ontology.
        XSD2OWLMapper mapping = new XSD2OWLMapper(new File("/Users/shinho/Workspace/UniBonn/2016_SS/SDWLab/Semantify.io-jena/src/main/resources/opc_ua/UANodeSet.mini.xsd"));
//        XSD2OWLMapper mapping = new XSD2OWLMapper(new File("/Users/shinho/Workspace/UniBonn/2016_SS/SDWLab/Semantify.io-jena/src/main/resources/opc_ua/UANodeSet.minimized_test.xsd"));
        mapping.setObjectPropPrefix("has");
        mapping.setDataTypePropPrefix("attr");
        mapping.convertXSD2OWL();

        // This part prints the ontology to the specified file.
        FileOutputStream ont;
        try {
            File f = new File("/Users/shinho/Workspace/UniBonn/2016_SS/SDWLab/Semantify.io-jena/src/main/resources/output/UANodeSet.mini.ttl");
//            File f = new File("/Users/shinho/Workspace/UniBonn/2016_SS/SDWLab/Semantify.io-jena/src/main/resources/output/UANodeSet.minimized_test.ttl");
            f.getParentFile().mkdirs();
            ont = new FileOutputStream(f);
            mapping.writeOntology(ont, "N3");
//            mapping.writeOntology(ont, "RDF/XML");
            ont.close();
        } catch (Exception e) {
//            LOGGER.error("{}", e.getMessage());
        }
    }
}
