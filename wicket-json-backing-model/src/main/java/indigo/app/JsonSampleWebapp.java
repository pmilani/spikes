package indigo.app;

import indigo.ui.Homepage;
import indigo.ui.JsonModelDemoPage;
import indigo.ui.MaterializeDemoPage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class JsonSampleWebapp extends WebApplication {

    @Override
    protected void init() {
        mountBookmarkablePage("/jsonmodel", JsonModelDemoPage.class);
        mountBookmarkablePage("/materialize", MaterializeDemoPage.class);
    }
	
    @Override
    public Class<? extends Page> getHomePage() {
        return Homepage.class;
    }

}
