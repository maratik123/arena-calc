package org.maratik.arena.calc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Slf4jRequestLog;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by maratik.
 */
public class Launcher {
    private static final Logger LOG = LogManager.getLogger(Launcher.class);
    private static final int DEFAULT_HTTP_PORT = 8080;
    private static final int MAX_THREADS = 100;
    private static final int MIN_THREADS = 10;
    private static final String HTTP_PORT_PROPERTY = "http.port";
    private static final String APPLICATION_PROPERTIES = "/application.properties";

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
        server.setConnectors(new Connector[]{createConnector(getPort(), server)});

        server.setHandler(createServletContextHandler(createContext()));

        server.setRequestLog(createRequestLog());

        LOG.debug("Start server");
        server.start();
        LOG.debug("Waiting to finish server thread");
        server.join();
        LOG.debug("Exit from main thread");
    }

    private Slf4jRequestLog createRequestLog() {
        Slf4jRequestLog requestLog = new Slf4jRequestLog();
        requestLog.setLoggerName("access.log");
        return requestLog;
    }

    private ServletContextHandler createServletContextHandler(@Nonnull WebApplicationContext webApplicationContext) throws IOException {
        LOG.debug("Creating servlet context handler");
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(webApplicationContext)), "/*");
        contextHandler.addEventListener(new ContextLoaderListener(webApplicationContext));
        return contextHandler;
    }

    private XmlWebApplicationContext createContext() {
        LOG.debug("Creating application context");
        XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
        applicationContext.setConfigLocation("classpath:/bean.xml");
        applicationContext.registerShutdownHook();

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

    private int getPort() {
        String httpPortStr = System.getenv(HTTP_PORT_PROPERTY);
        if (StringUtils.isEmpty(httpPortStr)) {
            try (Reader reader = new InputStreamReader(Launcher.class.getResourceAsStream(APPLICATION_PROPERTIES), StandardCharsets.UTF_8)) {
                Properties properties = new Properties();
                properties.load(reader);
                httpPortStr = properties.getProperty(HTTP_PORT_PROPERTY);
            } catch (IOException e) {
                LOG.debug("Can not find {}", APPLICATION_PROPERTIES);
            }
        }
        return StringUtils.isEmpty(httpPortStr) ? DEFAULT_HTTP_PORT : Integer.parseInt(httpPortStr);
    }
}
