package edu.server;

import com.google.gson.Gson;
import edu.database.hibernate.userService.UserService;
import edu.database.hibernate.usersEntity.UserEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UsersServlet extends HttpServlet {

    UserService userService = new UserService();
    Gson gson = new Gson();

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null || password == null) {
            response.setContentType("text/plain;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Login and password required");
            return;
        }
        if (userService.getUserByLog(login) != null) {
            response.setContentType("text/plain;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().println("User with login " + login + " already exists");
            return;
        }
        userService.createUser(login, password);
        response.setContentType("text/plain;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().println("User " + login + " is created");
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String login = request.getParameter("login");
        if (id != null) {
            try {
                UserEntity user = userService.getUserById(Long.valueOf(id));
                if (user == null) {
                    response.setContentType("text/plain;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("User isn't exist");
                    return;
                }
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(gson.toJson(user));
                return;
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("id must be a number");
                return;
            }
        }
        if (login != null) {
            UserEntity user = userService.getUserByLog(login);
            if (user == null) {
                response.setContentType("text/plain;charset=utf-8");
                response.getWriter().println("User isn't exist");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(gson.toJson(user));
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        List<UserEntity> users= userService.getAllUsers();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(gson.toJson(users));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        if (login == null) {
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().println("Login required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserEntity user = userService.getUserByLog(login);
        if (user == null) {
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().println("User doesn't exist");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        userService.delete(user);
        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().println("User " + login + " is deleted");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
