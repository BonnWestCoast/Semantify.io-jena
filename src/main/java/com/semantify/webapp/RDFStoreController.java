package com.semantify.webapp;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class RDFStoreController {

    static String schemas = "schema";
    static Dataset dataset = TDBFactory.createDataset(schemas);

    /**
     * Here we test the methods
     */
    @Test
    public void testStoreController() {

        RDFStoreController storeController = new RDFStoreController();

        /* fill the dataset with dummy data */
        //storeController.fillDataset(storeController);

        //storeController.queryOntology("product", query);

        /* print a list of all ontologies in the database */
        //List<String> listOnt = storeController.listOntologies();
        //for (String name: listOnt) imprime(name);

        //String result = storeController.getStatsFromOntology("tbox");
        //storeController.getSchemaByName("tbox");

        //storeController.getSchemaByName("xobt");
        //storeController.cleanDataset();
        //storeController.getSchemas();
    }


    /**
     * Auxiliar method to print easier
     * @param mensaje is an String object
     */
    public static void imprime(String mensaje) {
        System.out.println(mensaje);
    }


    /**
     * Auxiliar method to delete all the models in the dataset
     */
    public void cleanDataset() {

        Iterator list = dataset.listNames();
        String nameModel = "";

        while ( list.hasNext() ) {
            nameModel = list.next().toString();
            dataset.removeNamedModel(nameModel);
        }

    }


    /**
     * Auxiliar method to create dummy model examples
     */
    public void fillDataset( RDFStoreController storeController ) {

        Map<String, String> dictionary = new HashMap<String, String>();

        dictionary.put("tbox", "data/tbox.owl");
        dictionary.put("abox", "data/abox.owl");
        dictionary.put("product", "data/Product.owl");
        dictionary.put("opcua_ont", "data/UANodeSet_Ont_Merged.ttl");
        dictionary.put("opcua test sample", "data/Test_OPCUA.ttl");

        Set allKeys = dictionary.keySet();
        Iterator iterKeys = allKeys.iterator();

        while ( iterKeys.hasNext() ) {

            String nameSchema = (String) iterKeys.next();
            String pathSchema = dictionary.get( nameSchema );
            storeController.storeOntology(nameSchema, pathSchema);
        }
    }


    /**
     * It gets the number of classes, properties and triples in the ontology
     * @param nameSchema
     * @return
     */
    public String getStatsFromOntology(String nameSchema) {

        Model model = null;
        String stringResult = "";
        dataset.begin(ReadWrite.READ);

        /* Basic query to get all triples */
        String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                             "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                             "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                             "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                             "select ( count(?owl) as ?owl_classes )" +
                             "( count(?rdf_c) as ?rdf_classes )" +
                             "( count(?rdf_p) as ?rdf_properties )" +
                             "where {" +
                             "    { ?owl a owl:Class . { select * where { ?a ?b ?c } } }" +
                             "    union" +
                             "    { ?rdf_p a rdf:Property . { select * where { ?a ?b ?c } } }" +
                             "    union" +
                             "    { ?rdf_c a rdf:Class . { select * where { ?a ?b ?c } } }" +
                             "}";

        try {

            model = dataset.getNamedModel(nameSchema);
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet rs = qe.execSelect();
            QuerySolution solution = rs.nextSolution();

            /* Build stringResult */
            stringResult += "OWL classes: " + solution.get("owl_classes").asLiteral().getInt() + "\n";
            stringResult += "RDF classes: " + solution.get("rdf_classes").asLiteral().getInt() + "\n";
            stringResult += "RDF Properties: " + solution.get("rdf_properties").asLiteral().getInt();

            qe.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }

        return stringResult;

    }


    /**
     * From nameSchema looks for the correspondent model and execute the query
     * @param nameSchema is the schema where we want to execute the query
     * @param queryString is the value of the query
     */
    public String queryOntology(String nameSchema, String queryString) {

        Model model = null;
        String stringResult = "";
        dataset.begin(ReadWrite.READ);

        try {

            model = dataset.getNamedModel(nameSchema);
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);

            ResultSet rs = qe.execSelect();
            stringResult = ResultSetFormatter.asText(rs, query);

            qe.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }

        return stringResult ;
    }


    /**
     * From the name of the Schema returns the correspondent model
     * @param nameSchema
     * @return m, the model
     */
    public String getSchemaByName(String nameSchema, boolean isVisualization) {

        Model m = null;
        String modelContent = "";
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        dataset.begin(ReadWrite.READ);

        try {

            if ( dataset.containsNamedModel(nameSchema) ) {

                m = dataset.getNamedModel(nameSchema);
                System.out.println();
                if (isVisualization) {
                    m.write(os, "Turtle");
                }
                else {
                    m.write(os);
                }
                modelContent = new String(os.toByteArray());

            } else {
                System.out.println("The model does not exist");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }

        return modelContent;

    }


    /**
     * Looks on the dataset variable and returns a list of all the models
     * @return ontologies
     */
    public List<String> listOntologies() {

        List<String> ontologies = new ArrayList<String>();
        ontologies = new ArrayList<String>();

        dataset.begin(ReadWrite.READ);

        try {

            Iterator list = dataset.listNames();

            while ( list.hasNext() ) {
                String modelName = (String) list.next();
                imprime(modelName);
                ontologies.add(modelName);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }

        return ontologies;

    }


    /**
     * Stores the ontology using the name and path of the Schema
     * @param nameSchema
     * @param pathSchema
     */
    public void storeOntology(String nameSchema, String pathSchema) {

        dataset.begin(ReadWrite.WRITE);

        try {

            /* Create the model */
            Model model = ModelFactory.createDefaultModel();
            model.read(pathSchema);

            dataset.getNamedModel(pathSchema);
            dataset.addNamedModel(nameSchema, model);
            dataset.commit();

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }
    }


    /**
     * Stores the ontology using the name and path of the Schema
     * @param nameSchema
     * @param inputStream
     */
    public void storeOntology(String nameSchema, InputStream inputStream) {

        dataset.begin(ReadWrite.WRITE);

        try {

            /* Create the model */
            Model model = ModelFactory.createDefaultModel();
            model.read(inputStream, null, "N3");

            dataset.addNamedModel(nameSchema, model);
            dataset.commit();


        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }
    }


    public void getSchemas() {

        /**
         * Determine if we are dealing with an Schema or Instance
         */

        List<String> ontologies = new ArrayList<String>();
        ontologies = new ArrayList<String>();
        Model model = null;

        dataset.begin(ReadWrite.READ);

        try {

            Iterator list = dataset.listNames();

            while ( list.hasNext() ) {

                String modelName = (String) list.next();
                ontologies.add(modelName);
                model = dataset.getNamedModel(modelName);

                /* get the URL of this schema */
                StmtIterator iter = model.listStatements();
                while ( iter.hasNext() ) {
                    Statement stmt = iter.nextStatement();
                    Resource rs = stmt.getSubject();
                    imprime( "resource:" + rs.toString() );
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            dataset.end();
        }

    }
}