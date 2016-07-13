package opcua.ontmalizer;

import tr.com.srdc.ontmalizer.XSD2OWLMapper;
import java.io.File;
import java.io.FileOutputStream;

//import java.io.FileWriter;
//import java.io.Writer;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class XSD2OWLTest {
    public void createOPCUAOntology() {
        // This part converts XML schema to OWL ontology.
        XSD2OWLMapper mapping = new XSD2OWLMapper(new File("../OPC_UA/UANodeSet.xsd"));
        mapping.setObjectPropPrefix("hasObjProp_");
        mapping.setDataTypePropPrefix("hasDatProp_");
        mapping.convertXSD2OWL();

        // This part prints the ontology to the specified file.
        FileOutputStream ont;
        try {
            File f = new File("output/UANodeSet.n3");
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