package com.semantify.webapp;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Omar Gutiérrez on 04.07.16.
 */

public class DBController {

    Connection conn = null;
    Statement stmt = null;
    Element element = null;
    ArrayList<Element> list = new ArrayList<Element>();

    /*
    * Queries to the database
    * */

    public ArrayList<Element> listElements() {

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

        return list;
    }

    public Element getElementByID(String id) {

        PreparedStatement stmt = null;

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

        return element;
    }


    public void storeFile(InputStream is, FormDataContentDisposition fileName) {

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

        saveFile(fileName.getFileName(), fileLocation);

    }

    private void saveFile(String fileName, String fileLocation){
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
