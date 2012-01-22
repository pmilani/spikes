package indigo.ui;

import indigo.wicket.model.JsonDataProvider;
import indigo.wicket.model.JsonModel;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

public class JsonModelDemoPage extends WebPage {

    public JsonModelDemoPage() throws IOException {
        String userJson = IOUtils.toString(getClass().getResource("/user.json"));
        add(new Label("code", userJson));
        add(createInfoPanel("panel", userJson));
    }

    private WebMarkupContainer createInfoPanel(String id, String json) {
        final JsonModel model = new JsonModel(json);
        WebMarkupContainer p = new WebMarkupContainer("panel", model);
        p.add(new Label("name.last"));
        p.add(new Label("name.first"));
        p.add(new Label("verified"));
        
        JsonDataProvider provider = new JsonDataProvider(model, "phones");
        @SuppressWarnings("unchecked")
        DataView<String> phones = new DataView<String>("phoneList", provider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(Item<String> item) {
                item.add(new Label("value"));
            }
        };
        p.add(phones);
        
        return p;
    }
}