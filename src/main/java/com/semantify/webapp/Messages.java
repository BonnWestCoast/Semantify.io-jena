package com.semantify.webapp;

import java.util.ArrayList;

/**
 * POJOs: Auxiliary classes to built a response with JSON format
 */

class Success {

    int ontologyID;
    boolean success = true;

    public Success () {}

    public Success(int ontologyID) {
        this.ontologyID = ontologyID;
    }

}

class Error {

    String message = "";
    boolean success = false;

    public Error () {}

    public Error(String message) {
        this.message = message;
    }

}

class Element {

    int id;
    String name;
    String content;

    public Element() {}

    public Element(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Element(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

}

class elementList {

    public ArrayList<Element> list;
    public elementList () {}
    public elementList(ArrayList<Element> list) {
        this.list = list;
    }

}
