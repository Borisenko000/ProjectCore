package edu.polina.servlets;

import com.google.gson.Gson;
import edu.polina.accounts.AccountService;
import edu.polina.accounts.UserProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }



    //sign up
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null || password == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (accountService.getUserByLogin(login) != null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("User " + login + " is already registered");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        accountService.addNewUser(new UserProfile(login, password));
        Gson gson = new Gson();
        String json = gson.toJson(accountService.getUserByLogin(login));
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
