package org.maratik;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * Created by maratik.
 */
public class Launcher {
    private static final Logger LOG = LogManager.getLogger(Launcher.class);
    private static final int HTTP_PORT = 8080;

    public static void main(String[] args) {
        LOG.debug("Start main");

        try {
            new Launcher().startServer();
        } catch (Exception e) {
            LOG.error("Can not start server", e);
        }
    }

    private void startServer() throws Exception {
        Server server = new Server(createThreadPool());
        server.setConnectors(new Connector[] { createConnector(HTTP_PORT, server) });
        server.start();
        server.join();

        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            server.stop();
        });
    }

    private Connector createConnector(int httpPort, Server server) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(httpPort);
        return connector;
    }

    private ThreadPool createThreadPool() {
        return new QueuedThreadPool(100, 10);
    }
}
