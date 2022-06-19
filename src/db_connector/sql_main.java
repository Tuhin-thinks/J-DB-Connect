package src.db_connector;

import src.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class sql_main {
    private final String host;
    private final String port;
    private final String user;
    private final String pass;
    private final String db_name;
    private Connection connection;

    public sql_main(String host, String port, String user, String pass, String db_name) {
        /*
        // print out connection details
        System.out.println(
                "Host: " + host +
                "\nPort: " + port +
                "\nUser: " + user +
                "\nPassword: " + pass +
                "\nDatabase: " + db_name);
        */
        // store as class variables
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.db_name = db_name;
    }

    public boolean connectToDatabase() {
        // connect to database

        // connect to mysql database
        try {
            String connection_string = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.db_name;
            if (this.connection != null) {
                System.out.println("Using existing connection");
            } else {
                this.connection = DriverManager.getConnection(connection_string, this.user, this.pass);
                if (this.connection != null) {
                    System.out.println("Connected to database");
                } else {
                    System.out.println("Failed to connect to database");
                    throw new SQLException("Failed to connect to database");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        saveConnectionFields();

        // return true if successful
        return true;
    }

    public ResultSet execute_query(String query) throws SQLException {
        if (connection == null) {
            System.out.println("No connection to database, creating new connection");
            connectToDatabase();
            return null;
        }
        if (connection.isClosed()) {
            System.out.println("Connection is closed, Reconnecting...");
            connectToDatabase();
            return null;
        }
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Query executed");
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error executing query");
            e.printStackTrace();
        }
        return null;
    }

    public int countRows(String table_name) {
        String query = "SELECT COUNT(*) FROM " + table_name;
        try {
            ResultSet resultSet = execute_query(query);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void close_connection() throws SQLException {
        this.connection.close();
    }

    public Vector<String> getColumnNames(String table_name) throws SQLException {
        if (connection == null) {
            System.out.println("No connection to database, creating new connection");
            connectToDatabase();
            return null;
        }
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table_name);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        return columnNames;
    }

    private void saveConnectionFields() {
        // save connection details to file for later use
        FileUtils.writeToFile("connection_details.txt",
                "Host: " + this.host +
                        "\nPort: " + this.port +
                        "\nUser: " + this.user +
                        "\nPassword: " + this.pass +
                        "\nDatabase: " + this.db_name);
    }

}
