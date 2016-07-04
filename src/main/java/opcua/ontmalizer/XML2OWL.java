package opcua.ontmalizer;

import tr.com.srdc.ontmalizer.XML2OWLMapper;
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
public class XML2OWL {

//    private static final Logger LOGGER = LoggerFactory.getLogger(XML2OWL.class);

    public void createOPCUAInstances() {

        // This part converts XML schema to OWL ontology.
        XSD2OWLMapper mapping = new XSD2OWLMapper(new File("resources/OPC_UA/UANodeSet.xsd"));
        mapping.setObjectPropPrefix("");
        mapping.setDataTypePropPrefix("");
        mapping.convertXSD2OWL();

        // This part converts XML instance to RDF data model.
        XML2OWLMapper generator = new XML2OWLMapper(new File("resources/OPC_UA/Opc.Ua.NodeSet2.xml"), mapping);
        generator.convertXML2OWL();

        // This part prints the RDF data model to the specified file.
        try {
            File f = new File("resources/output/Opc.Ua.NodeSet2.n3");
            f.getParentFile().mkdirs();
            FileOutputStream fout = new FileOutputStream(f);
            generator.writeModel(fout, "N3");
            fout.close();

        } catch (Exception e) {
//            LOGGER.error("{}", e.getMessage());
        }
    }
}
