package edu.database.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResourceHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
