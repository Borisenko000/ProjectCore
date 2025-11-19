package edu.database.luquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseRunner {

    public static void runMigrations() {
        String url = System.getProperty("db.url");
        String user = System.getProperty("db.user");
        String pass = System.getProperty("db.password");

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update((String) null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении миграций Liquibase", e);
        }
    }
}
