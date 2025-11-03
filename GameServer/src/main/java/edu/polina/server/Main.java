package edu.polina.server;

import edu.polina.accounts.AccountService;
import edu.polina.accounts.UserProfile;
import edu.polina.servlets.SignInServlet;
import edu.polina.servlets.SignUpServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(Main.class);

        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setBaseResource(ResourceFactory.root().newResource("public_html"));

        Handler handlers = new Handler.Sequence(resource_handler, context);
        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        log.info("Server started");
        server.join();
    }
}
