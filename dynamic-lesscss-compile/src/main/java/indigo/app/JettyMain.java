package indigo.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyMain {

    public static void main(String[] args) throws Exception {
        new JettyMain().start();
    }

    private void start() throws Exception {
        Server server = new Server(8080);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	    context.setContextPath("/");
	    context.setResourceBase("src/main/webapp");
	    
	    context.addServlet(LessStyleServlet.class, "/dyncss/*");
	    context.addServlet(DefaultServlet.class, "/*");
	    
	    server.setHandler(context);
	    server.start();
	    server.join();
    }
}
