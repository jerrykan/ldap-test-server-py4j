package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;
import py4j.GatewayServer;

public class Server {
    private static GatewayServer gateway;

    private LdapServerResource server;

    public static void main(String[] args) throws Exception {
        boolean started = false;
        int inc = 0;

        do {
            try {
                gateway = new GatewayServer(new Server(), 25333+inc);
                gateway.start();
                started = true;
            } catch (py4j.Py4JNetworkException e) {
                if (inc > 9) {
                    System.err.println("Unable to find a free port to start gateway");
                    System.exit(1);
                }

                inc++;
            }
        } while (!started);

        System.out.println("Gateway server started on port "+gateway.getPort()+"!");
    }

    private Server() {}

    public void stop() {
        server.stop();
    }

    public void start(LdapConfiguration config) throws Exception {
        server = new LdapServerResourceCreator(config).getResource();
        server.start();
    }

    public int port() {
        return server.port();
    }
}
