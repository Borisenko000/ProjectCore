package edu.server;

import edu.database.DBService;
import edu.database.luquibase.LiquibaseRunner;
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
        LiquibaseRunner.runMigrations();
        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new UsersServlet()), "/user");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setBaseResource(ResourceFactory.root().newResource("public_html"));

        Handler handlers = new Handler.Sequence(resource_handler, context);
        Server server = new Server(8080);
        server.setHandler(handlers);
        DBService dbService = DBService.getInstance();
        dbService.printConnectInfo();


        server.start();
        log.info("Server started");
        server.join();

    }
}