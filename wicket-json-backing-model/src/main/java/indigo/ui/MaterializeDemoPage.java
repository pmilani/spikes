package indigo.ui;

import indigo.ui.datatrait.UserTrait;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.mrbean.MrBeanModule;

public class MaterializeDemoPage extends WebPage {

    public MaterializeDemoPage() throws IOException {
        String userJson = IOUtils.toString(getClass().getResource("/user.json"));
        add(new Label("code", userJson));
        add(createInfoPanel("panel", userJson));
    }

    private WebMarkupContainer createInfoPanel(String id, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new MrBeanModule());
        
        UserTrait user = mapper.readValue(json, UserTrait.class);
        
        WebMarkupContainer p = new WebMarkupContainer("panel");
        p.add(new Label("lastName", user.getName().getLast()));
        p.add(new Label("firstName", user.getName().getFirst()));
        p.add(new Label("verified", user.getVerified() ? "verified" : "not verified"));
        return p;
    }
}
