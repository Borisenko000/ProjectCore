package edu.database;

import edu.database.jdbc.dataSets.UsersDataSet;

public class TestDB {
    public static void main(String[] args) {
        DBService dbService = DBService.getInstance();
        dbService.addUser("Polina", "12345");
        dbService.addUser("Anton", "54321");
        dbService.addUser("Maria", "Mari2023");
        UsersDataSet user = dbService.getUser("Polina");
        System.out.println(user.getId() + user.getName() + user.getPassword());
    }
}
