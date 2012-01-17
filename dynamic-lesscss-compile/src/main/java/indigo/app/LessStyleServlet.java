package indigo.app;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

public class LessStyleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Map<String,String> cachedCss = new ConcurrentHashMap<String, String>();
    private static Map<String,Long> cachedModTime = new ConcurrentHashMap<String, Long>();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() != null) {
            try {
                if (isResourceNewerThanCache(req)) {
                    generateCss(req, resp);
                } else {
                    sendCachedCss(req, resp);
                }
                resp.getOutputStream().flush();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            super.doGet(req, resp);
        }
    }
    
    private void sendCachedCss(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        IOUtils.write(cachedCss.get(req.getPathInfo()), resp.getOutputStream());
    }

    private boolean isResourceNewerThanCache(HttpServletRequest req) throws URISyntaxException {
        Long modTime = cachedModTime.get(req.getPathInfo());
        if (modTime == null) {
            return true;
        }
        File file = new File(getResourceUrl(req).toURI());
        return file.lastModified() > modTime;
    }
    
    private URL getResourceUrl(HttpServletRequest req) {
        return getClass().getResource("/webapp" + req.getPathInfo());
    }

    private void generateCss(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException {
        LessEngine engine = new LessEngine();
        try {
            URL url = getResourceUrl(req);
            if (url == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String css = engine.compile(url);
            updateCachedCss(req, css);
            
            IOUtils.write(css, resp.getOutputStream());
        } catch (LessException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCachedCss(HttpServletRequest req, String css) throws URISyntaxException {
        File file = new File(getResourceUrl(req).toURI());
        cachedModTime.put(req.getPathInfo(), file.lastModified());
        cachedCss.put(req.getPathInfo(), css);
    }

}
