package indigo.less.stylesheet;

import java.io.IOException;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.io.*;

import com.asual.lesscss.*;

public class LessStylesheetFilter implements Filter {
    private static final long serialVersionUID = 1L;
    
    private String lessSourcesPath;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        lessSourcesPath = filterConfig.getInitParameter("lessSourcesPath");
        if (lessSourcesPath == null) {
            lessSourcesPath = "/webapp";
        }
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

    /**
     * Override this for finer control on locating LESS source files.
     * @param pathInfo from the request
     * @return to the LESS source file
     */
    protected URL getSourceUrlForRequestedPath(String pathInfo) {
        String styleSource = FilenameUtils.getFullPath(pathInfo) + FilenameUtils.getBaseName(pathInfo) + ".less";
		return getClass().getResource(lessSourcesPath + styleSource);
    }

    private boolean generateCss(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException {
        LessEngine engine = new LessEngine();
        try {
            URL url = getSourceUrlForRequestedPath(req.getPathInfo());
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
