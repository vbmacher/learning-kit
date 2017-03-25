/*
 * DerbyHandler.java
 *
 * (c) Copyright 2008, vbmacher
 * 
 */

package oknatradika.data;

import java.io.FileInputStream;
import java.io.InputStream;
import oknatradika.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Handles derby database
 * 
 * @author vbmacher
 */
public class DerbyHandler {
    private static DerbyHandler thisDH = null;
    private static Connection con = null;
    private final String dbName = System.getProperty("user.dir",".")
            + System.getProperty("file.separator") + "okna.db";
   
    /**
     * Opens database connection and DerbyHandler instance
     */
    private DerbyHandler() throws ClassNotFoundException, InstantiationException,
           IllegalAccessException {
        String driverS = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driverS).newInstance();
        System.out.println(dbName);
        String connectionURL = "jdbc:derby:" + dbName + ";ll=CS";
        try {
            con = DriverManager.getConnection(connectionURL);
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("XJ004")) {
                // Database not found
                createDB();
            }
        }
    }

    public static DerbyHandler getInstance() {
        if (thisDH == null) {
            try {
                thisDH = new DerbyHandler();
            } catch (ClassNotFoundException e) {
                Main.error("Ovládač Derby sa nenašiel !");
            } catch (InstantiationException e) {
                Main.error("Nemôžem vytvoriť inštanciu ovládača Derby !");
            } catch (IllegalAccessException e) {
                Main.error("Prístup k ovládaču bol zamietnutý !");
            }
        }
        return thisDH;
    }
   
    public static void closeConnection() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {}
        thisDH = null;
        con = null;
    }
   
   
    /**
     * Creates a database. It is called from constructor if database doesn't
     * exist
     */
    private void createDB() {
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";
        try {
            con = DriverManager.getConnection(connectionURL);
            Statement s;
            s = con.createStatement();
            s.execute("create table Types ("
                     + "id int primary key generated always as identity"
                     + " (START WITH 1, INCREMENT BY 1),"
                     + "name varchar(100) unique not null"
                     + ")");
            s.execute("create table Subtypes ("
                     + "id int primary key generated always as identity"
                     + " (START WITH 1, INCREMENT BY 1),"
                     + "type_id int not null references Types(id) on delete cascade,"
                     + "name varchar(100) unique not null,"
                     + "picture blob"
                     + ")");
            s.execute("create table Windows ("
                     + "id int primary key generated always as identity"
                     + " (START WITH 1, INCREMENT BY 1),"
                     + "subtype_id int not null references Subtypes(id) on delete cascade,"
                     + "gx_from int not null,"
                     + "gx_to int not null,"
                     + "gy_from int not null,"
                     + "gy_to int not null,"
                     + "prize real not null"
                     + ")");
        } catch (SQLException e) {
            Main.error("Databázu sa nepodarilo vytvoriť !"
                    + "\n\n" + e.getMessage());
        }
    }
    
    
    /**
     * Create PreparedStatemet instane from given SQL query
     * Accepted parameter types: int, boolean, String, Long, Sql.Date 
     * 
     * @param q SQL query
     * @param args parameters
     * @return statement
     * @throws java.sql.SQLException
     */
    private PreparedStatement prepareStatement(String q, Object... args)
            throws SQLException {
        PreparedStatement stat = con.prepareStatement(q);
        if (args.length < stat.getParameterMetaData().getParameterCount())
            return null;
        for (int i = 0; i < stat.getParameterMetaData().getParameterCount();
                i++) {
            Object arg = args[i];
            if (arg instanceof Boolean)
                stat.setBoolean(i+1, (Boolean) arg);
            else if (arg instanceof Long)
                stat.setLong(i+1, (Long) arg);
            else if (arg instanceof Integer)
                stat.setInt(i+1, (Integer) arg);
            else if (arg instanceof String)
                stat.setString(i+1, (String) arg);
            else if (arg instanceof java.sql.Date)
                stat.setDate(i+1, (java.sql.Date) arg);
            else if (arg instanceof FileInputStream)
                stat.setBinaryStream(i+1, (InputStream)arg);
            else
                stat.setObject(i+1, arg);
        }
        return stat;
    }

    public boolean execute(String q, Object... args) throws SQLException {
        PreparedStatement stat = prepareStatement(q, args);
        return stat.execute();
    }

    public ResultSet query(String q, Object... args) throws SQLException {
        PreparedStatement stat = prepareStatement(q, args);
        return stat.executeQuery();
    }

    public int update(String q, Object... args) throws SQLException {
        PreparedStatement stat = prepareStatement(q, args);
        return stat.executeUpdate();
    }

    /**
     * Method creates INSERT statement.
     * 
     * @param table Name of the table
     * @param params Couples of parameters used in insert = first is name of
     *               the column, second is the value
     * @return array <code>Object[]</code>, where first item in the array is
     * <code>String</code> - formulated INSERT statement and the rest are
     * parameters of the statement. This array can be used for execute/query
     * methods.
     */
    public static Object[] createInsertStat(String table, Object[] params) {
        if (params == null) return null;

        String stat = "insert into " + table + "(";
        String vals = "values(";
        ArrayList par = new ArrayList();

        for (int i = 0; i < params.length-1; i+=2) {
            String col = (String)params[i]; // column name
            Object arg = params[i+1];       // value
            if (i > 0) {
                stat += ",";
                vals += ",";
            }
            stat += col;
            vals += "?";
            par.add(arg);
        }
        par.add(0, stat + ") " + vals + ")");
        return par.toArray();
    }
    
    public static Object[] createUpdateStat(String table, Object[] params,
            Object[] endParams) {
        if (params == null) return null;

        String stat = "update " + table + " set ";
        ArrayList par = new ArrayList();

        for (int i = 0; i < params.length-1; i+=2) {
            String col = (String)params[i]; // column name
            Object arg = params[i+1];       // value
            if (i > 0) stat += ",";
            stat += col;
            if (arg == null) stat += "=NULL";
            else {
                stat += "=?";
                par.add(arg);
            }
        }
        if (endParams != null && endParams.length >= 2) stat += " where ";
        for (int i = 0; i < endParams.length-1; i+=2) {
            String col = (String)endParams[i]; // column name
            Object arg = endParams[i+1];       // value
            if (i > 0) stat += ",";
            if (arg instanceof String)
                stat += col + " like ?";
            else
                stat += col + "=?";
            par.add(arg);
        }
        par.add(0, stat);
        return par.toArray();
    }

}
