package indigo.ui;

import indigo.wicket.model.JsonModel;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class JsonModelDemoPage extends WebPage {
	
	public JsonModelDemoPage() throws IOException {
		String userJson = IOUtils.toString(getClass().getResource("/user.json"));
		add(new Label("code", userJson));
		add(createInfoPanel("panel", userJson));
    }

	private WebMarkupContainer createInfoPanel(String id, String json) {
		WebMarkupContainer p = new WebMarkupContainer("panel", new JsonModel(json));
		p.add(new Label("name.last"));
		p.add(new Label("name.first"));
		p.add(new Label("verified"));
	    return p;
    }
}
