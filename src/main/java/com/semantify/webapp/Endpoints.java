package com.semantify.webapp;

import javax.ws.rs.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 * This is a POJO class useful to return JSON format in the response of the services.
 */
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

/**
 * This is a POJO class useful to return JSON format in the response of the services.
 */
class elementList {

    public ArrayList<Element> list;

    public elementList () {}

    public elementList(ArrayList<Element> list) {
        this.list = list;
    }
}

@Path("/instance")
public class Endpoints {

    @POST
    @Path("/new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public Response getMessage(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileName
    ) {

        String fileLocation = fileName.getFileName();
        saveToDisk(uploadedInputStream, fileName);
        return Response.status(200).build();
    }

    @GET
    @Path("/list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {

        Connection conn = null;
        Statement stmt = null;
        ArrayList<Element> list = new ArrayList<Element>();

        try {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:files.db");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from files");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Element element = new Element(id, name);
                list.add(element);
            }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String json = new Gson().toJson(new elementList(list));

        return Response.status(200).entity(json).build();
    }


    @GET
    @Path("/load/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(
            @PathParam("id") String id
    ) {

        Connection conn = null;
        PreparedStatement stmt = null;
        Element element = null;

        try {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:files.db");
            stmt = conn.prepareStatement("select * from files where id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id_ = rs.getInt("id");
                String name = rs.getString("name");
                String content = rs.getString("xml_file");
                element = new Element(id_, name, content);
            }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String json = new Gson().toJson(element);
        return Response.status(200).entity(json).build();
    }

    private void saveToDisk(InputStream is, FormDataContentDisposition fileName) {

        String fileLocation = "/home/hariseldon/" + fileName.getFileName();

        try {
            OutputStream out = new FileOutputStream(new File(fileLocation));
            IOUtils.copy(is, out);
            is.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Exception");
            e.printStackTrace();
        }

        SQLStuff(fileName.getFileName(), fileLocation);

    }

    Connection conn = null;
    Statement stmt = null;

    public void SQLStuff(String fileName, String fileLocation){
        try {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:files.db");

            if (conn != null) {

                /* create table if not exists */
                stmt = conn.createStatement();
                String sql = " create table if not exists files ( " +
                             " id integer primary key autoincrement, " +
                             " name text not null, " +
                             " xml_file blob " +
                             ")";
                stmt.executeUpdate(sql);

                /* insert new instance */
                sql = "insert into files (name, xml_file) values (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, fileName);

                File xmlFile = new File(fileLocation);
                FileInputStream fis = new FileInputStream(xmlFile);
                stmt.setBinaryStream(2, fis, (int) xmlFile.length());
                stmt.execute();

                stmt.close();
                conn.close();
                System.out.println("New instance");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

