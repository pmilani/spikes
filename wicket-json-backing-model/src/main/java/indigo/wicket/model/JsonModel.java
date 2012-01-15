package indigo.wicket.model;

import java.io.IOException;


import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Model for simple access to JSON data.<br>
 * This model should be set on a parent component, and let the children have a null model.
 * The children id is used as an <i>expression</i> to navigate the JSON tree and read a value.<br>
 * All values are returned as text.
 */
public class JsonModel implements IComponentInheritedModel<String> {
    private static final long serialVersionUID = 1L;

    private final String json;
    private transient JsonNode tree;

    public JsonModel(String json) {
        this.json = json;
    }

    public JsonNode getJsonNode() {
        if (tree == null) {
            try {
                tree = new ObjectMapper().readTree(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tree;
    }

    public String getObject() {
        return json;
    }

    public void setObject(String object) {
        throw new UnsupportedOperationException();
    }

    public void detach() {
        tree = null;
    }

    protected String propertyExpression(Component component) {
        return component.getId();
    }

    @SuppressWarnings("unchecked")
    public IWrapModel<String> wrapOnInheritance(Component component) {
        return new AttachedJsonModel(component);
    }

    private class AttachedJsonModel extends JsonPropertyModel implements IWrapModel<String> {
        private static final long serialVersionUID = 1L;

        private final Component owner;

        public AttachedJsonModel(Component owner) {
            super(JsonModel.this);
            this.owner = owner;
        }

        @Override
        protected String propertyExpression() {
            return JsonModel.this.propertyExpression(owner);
        }

        public IModel<String> getWrappedModel() {
            return JsonModel.this;
        }

        @Override
        public void detach() {
            super.detach();
            JsonModel.this.detach();
        }
    }
}
