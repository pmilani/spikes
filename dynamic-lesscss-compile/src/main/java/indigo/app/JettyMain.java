package indigo.app;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;

public class JettyMain {

    public static void main(String[] args) throws Exception {
        new JettyMain().start();
    }

    private void start() throws Exception {
        Server server = new Server(8080);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	    context.setContextPath("/");
	    context.setResourceBase("src/main/webapp");
	    
	    context.addFilter(LessStylesheetFilter.class, "*.css", FilterMapping.ALL);
	    context.addServlet(DefaultServlet.class, "/*");
	    
	    server.setHandler(context);
	    server.start();
	    server.join();
    }
}
