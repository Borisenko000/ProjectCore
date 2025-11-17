package edu.database.jdbc.dao;

import edu.database.jdbc.dataSets.UsersDataSet;
import edu.database.jdbc.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAO {

    private Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public UsersDataSet get(String login) throws SQLException {
        return executor.execQuery("select * from users where login='" + login + "'", result -> {
            if(!result.next()) {
                return null;
            }
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(3));
        });
    }

    public long getUserId(String login) throws SQLException {
        return executor.execQuery("select * from users where login='" + login + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void insertUser(String login, String password) throws SQLException {
        executor.execUpdate("insert into users (login, password) values ('" + login + "', '" + password + "')");
    }

    public void createTable() throws SQLException {
        executor.execUpdate( "create table if not exists users (" +
                "id bigserial primary key, " +
                "login varchar(256), " +
                "password varchar(256)" +
                ")");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }

}
