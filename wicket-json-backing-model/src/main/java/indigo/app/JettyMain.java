package indigo.app;

import org.apache.wicket.protocol.http.WicketServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyMain {

	public static void main(String[] args) throws Exception {
	    new JettyMain().start();
    }

	private void start() throws Exception {
	    Server server = new Server(8080);
	    
	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	    context.setContextPath("/");
	    
	    ServletHolder wicket = new ServletHolder(WicketServlet.class);
	    wicket.setInitParameter("applicationClassName", JsonSampleWebapp.class.getName());
	    wicket.setInitOrder(1);
	    context.addServlet(wicket, "/*");
	    
	    server.setHandler(context);
	    server.start();
	    server.join();
    }
}
