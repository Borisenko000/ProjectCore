package edu.database.hibernate.userService;

import edu.database.hibernate.usersDAO.UsersDAO;
import edu.database.hibernate.usersEntity.UserEntity;

import java.util.List;

public class UserService {

    private final UsersDAO usersDao = new UsersDAO();

     public void createUser(String login, String password) {
         usersDao.insertUser(login, password);
     }

     public UserEntity getUserByLog(String login) {
         return usersDao.getUserbyLogin(login);
     }

     public UserEntity getUserById(Long id) {
         return usersDao.get(id);
     }

     public void delete(UserEntity user) {
         usersDao.delete(user.getId());
     }

     public List<UserEntity> getAllUsers() {
         return usersDao.findAll();
     }

}
