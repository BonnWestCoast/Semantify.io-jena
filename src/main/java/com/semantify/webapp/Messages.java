package com.semantify.webapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * POJOs: Auxiliary classes to built a response with JSON format
 */

/**
 * Helps to build the success messages
 */

class Messages {

    boolean success = true;
    Date time = null;

    protected void setTime(){
        this.time = new Date();
    }


}

class Success extends Messages {

    String data = null;

    public Success () {}

    public Success(String data) {
        setTime();
        this.data = data;
    }

}

/**
 * Helps to build the error messages
 */
class Error extends Messages {

    String message = "";
    boolean success = false;

    public Error () {}

    public Error(String message) {
        setTime();
        this.message = message;
    }

}

/**
 * Get ontology method in format:
 * {
 *   data: 'pure ontology text',
 *   time: Date.now(),
 *   success: true
 * }
 */
class Ontology extends Messages {

    String id = null;
    String name = null;
    String data = null;

    public Ontology() {}

    public Ontology(String data) {
        setTime();
        this.data = data;
    }

    public Ontology(String id, String name) {
        setTime();
        this.id = id;
        this.name = name;
    }

    public Ontology(String id, String name, String data) {
        setTime();
        this.id = id;
        this.name = name;
        this.data = data;
    }

}

/**
 * List of ontologies in format:
 * {
 *   data: [{'name': ',,,,', id: '...'},{'name': '...', id: ',,'}],
 *   time: Date.now(),
 *   success: true
 * }
 */
class OntologyList extends Messages {

     ArrayList<Ontology> data;

     public OntologyList () {}

     public OntologyList(ArrayList<Ontology> data) {
         setTime();
         this.data = data;
     }

}
