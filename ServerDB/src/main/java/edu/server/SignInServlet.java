package edu.server;

import edu.database.DBService;
import edu.database.jdbc.dataSets.UsersDataSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //sign in
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException  {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        var db = DBService.getInstance();
        UsersDataSet user= db.getUser(login);
        UserProfile profile = accountService.getUserByLogin(login);

        if (profile == null && user == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (user.getPassword().equals(pass) && user.getName().equals(login)) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Authorized: " + login);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Incorrect login or password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}

