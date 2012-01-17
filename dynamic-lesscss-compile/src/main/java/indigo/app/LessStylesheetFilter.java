package indigo.app;

import java.io.IOException;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.io.*;

import com.asual.lesscss.*;

public class LessStylesheetFilter implements Filter {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	HttpServletResponse resp = (HttpServletResponse) response;
    	
        if (req.getPathInfo() != null) {
            try {
                if (!generateCss(req, resp)) {
                    chain.doFilter(request, response);
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private URL getResourceUrl(HttpServletRequest req) {
        String styleSource = FilenameUtils.getFullPath(req.getPathInfo()) + FilenameUtils.getBaseName(req.getPathInfo()) + ".less";
		return getClass().getResource("/webapp" + styleSource);
    }

    private boolean generateCss(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException {
        LessEngine engine = new LessEngine();
        try {
            URL url = getResourceUrl(req);
            if (url == null) {
                return false;
            }
            String css = engine.compile(url);
            IOUtils.write(css, resp.getOutputStream());
            resp.getOutputStream().flush();
            return true;
        } catch (LessException e) {
            throw new RuntimeException(e);
        }
    }
}
