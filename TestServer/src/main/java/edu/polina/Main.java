package edu.polina;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(Main.class);
        Server server = new Server();
        SslContextFactory.Server ssl = new SslContextFactory.Server();
        ssl.setKeyStorePath("C:/Users/Professional/cert-lab/server.p12");
        ssl.setKeyStorePassword("123456");
        ssl.setKeyStoreType("PKCS12");
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        ServerConnector httpConnector = new ServerConnector(server,
                new SslConnectionFactory(ssl, "http/1.1"),
                new HttpConnectionFactory(https));
        httpConnector.setPort(8443);
        server.addConnector(httpConnector);
        Frontend frontend = new Frontend();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/mirror");
        server.setHandler(context);
        server.start();
        log.info("Server started");
        server.join();
    }
}