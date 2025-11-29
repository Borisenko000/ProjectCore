package edu.database;

import edu.database.jdbc.dao.UsersDAO;
import edu.database.jdbc.dataSets.UsersDataSet;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private final Connection connection;
    private static DBService dbService;

    public DBService() {
        String db = System.getProperty("db.type");
        if (db.equals("postgres")) {
            this.connection = getPostgresConnection();
        }
        else if (db.equals("mysql")) {
            this.connection = getMysqlConnection();
        }
        else {
            this.connection = getPostgresConnection();
        }
    }

    public static DBService getInstance() {
        if (dbService == null) {
            dbService = new DBService();
        }
        return dbService;
    }

    public UsersDataSet getUser(String login) throws DBException {
        try {
            return (new UsersDAO(connection).get(login));
        } catch (SQLException e) {
            throw  new DBException(e);
        }
    }

    public long addUser(String name, String password) throws DBException {
        try {
            connection.setAutoCommit(false);
            UsersDAO dao = new UsersDAO(connection);
            dao.insertUser(name, password);
            connection.commit();
            return dao.getUserId(name);
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void cleanUp() throws  DBException {
        UsersDAO dao = new UsersDAO(connection);
        try {
            dao.dropTable();
        }  catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getMysqlConnection() {
        String url = System.getProperty("db.url");
        String user = System.getProperty("db.user");
        String password = System.getProperty("db.password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Cannot connect to MySQL" + e.getMessage(), e);
        }
    }

     public static Connection getPostgresConnection() {
        String url = System.getProperty("db.url");
        String user = System.getProperty("db.user");
        String password = System.getProperty("db.password");

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Cannot connect to PostgresSQL" + e.getMessage(), e);
        }
     }

}
