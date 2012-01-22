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

    private String json;
    private transient JsonNode tree;

    public JsonModel(String json) {
        this.json = json;
    }

    // FIXME for models constructed this way, after serialization we lose them (tree is transient)
    public JsonModel(JsonNode tree) {
        this.tree = tree;
    }
    
    private static JsonNode parse(String json) {
        try {
            return new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode getJsonNode() {
        if (tree == null) {
            tree = parse(json);
        }
        return tree;
    }

    @Override
    public String getObject() {
        return json;
    }

    @Override
    public void setObject(String json) {
        this.tree = null;
        this.json = json;
    }

    @Override
    public void detach() {
        tree = null;
    }

    protected String propertyExpression(Component component) {
        return component.getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public IWrapModel<String> wrapOnInheritance(Component component) {
        return new AttachedJsonModel(component);
    }

    private class AttachedJsonModel extends JsonPropertyModel implements IWrapModel<String> {
        private static final long serialVersionUID = 1L;

        private final Component owner;

        public AttachedJsonModel(Component owner) {
            super(getJsonNode());
            this.owner = owner;
        }

        @Override
        protected String propertyExpression() {
            return JsonModel.this.propertyExpression(owner);
        }

        @Override
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
