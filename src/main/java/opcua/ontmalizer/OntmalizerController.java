package opcua.ontmalizer;

import tr.com.srdc.ontmalizer.XML2OWLMapper;
import tr.com.srdc.ontmalizer.XSD2OWLMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OntmalizerController {

    private XSD2OWLMapper ontology = null;
    private XSD2OWLMapper ontologyTypes = null;
    private XML2OWLMapper model = null;
    private InputStream xmlSchema = null;
    private InputStream xmlInstance = null;

    public OntmalizerController(InputStream xmlSchema, InputStream xmlInstance) {
        this.xmlSchema = xmlSchema;
        this.xmlInstance = xmlInstance;
    }

    public XSD2OWLMapper getOntology() {
        return ontology;
    }
    public XSD2OWLMapper getOntologyTypes() {
        return ontologyTypes;
    }
    public XML2OWLMapper getModel() {
        return model;
    }


    public void createOPCUAOntology() {
        XSD2OWLMapper mapping = new XSD2OWLMapper(xmlSchema);
//        mapping.setNsPrefix("opcua");
        mapping.setObjectPropPrefix("has");
        mapping.setDataTypePropPrefix("attr");
        mapping.convertXSD2OWL();

        ontology = mapping;
    }

    public void importOntology(InputStream isOnt) {
        ontology.importOntology(isOnt);
    }

    public void importOPCUATypesOntology() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream typesOnt = classLoader.getResourceAsStream("output/Types_Ont.ttl");
        importOntology(typesOnt);
    }

    public void createOPCUATypesOntology() {
        XSD2OWLMapper mapping = new XSD2OWLMapper(xmlSchema);
        mapping.setNsPrefix("opctypes");
        mapping.setObjectPropPrefix("has");
        mapping.setDataTypePropPrefix("attr");

        mapping.convertXSD2OWL();

        ontology = mapping;
    }


    public void createOPCUAInstances() {
        XML2OWLMapper generator = new XML2OWLMapper(xmlInstance, ontology);
        generator.convertXML2OWL();

        model = generator;
    }


}
