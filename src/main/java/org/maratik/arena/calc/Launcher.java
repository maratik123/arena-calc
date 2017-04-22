package org.maratik.arena.calc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.maratik.arena.calc.service.Config;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Created by maratik.
 */
public class Launcher {
    private static final Logger LOG = LogManager.getLogger(Launcher.class);
    private static final int HTTP_PORT = 8080;
    private static final int MAX_THREADS = 100;
    private static final int MIN_THREADS = 10;

    public static void main(String[] args) {
        LOG.debug("Start main");

        try {
            new Launcher().startServer();
        } catch (Exception e) {
            LOG.error("Can not start server", e);
        }
    }

    private void startServer() throws Exception {
        LOG.debug("Initializing server");
        Server server = new Server(createThreadPool());
        server.setConnectors(new Connector[] { createConnector(HTTP_PORT, server) });

        server.setHandler(createServletContextHandler(createContext()));

        LOG.debug("Start server");
        server.start();
        LOG.debug("Waiting to finish server thread");
        server.join();
        LOG.debug("Exit from main thread");
    }

    private ServletContextHandler createServletContextHandler(WebApplicationContext webApplicationContext) throws IOException {
        LOG.debug("Creating servlet context handler");
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(webApplicationContext)), "/*");
        contextHandler.addEventListener(new ContextLoaderListener(webApplicationContext));
        contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
        return contextHandler;
    }

    private AnnotationConfigWebApplicationContext createContext() {
        LOG.debug("Creating application context");
        AnnotationConfigWebApplicationContext applicationContext =
            new AnnotationConfigWebApplicationContext();
        applicationContext.register(Config.class);

        return applicationContext;
    }

    private ServerConnector createConnector(int httpPort, @Nonnull Server server) {
        LOG.debug("Creating server connector for port = {}", httpPort);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(httpPort);
        return connector;
    }

    private QueuedThreadPool createThreadPool() {
        LOG.debug("Creating thread pool with max_threads = {} and min_threads = {}", MAX_THREADS, MIN_THREADS);
        return new QueuedThreadPool(MAX_THREADS, MIN_THREADS);
    }
}
