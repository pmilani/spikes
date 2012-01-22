package indigo.wicket.model;


import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.codehaus.jackson.JsonNode;

@SuppressWarnings("rawtypes")
public class JsonDataProvider implements IDataProvider {
    private static final long serialVersionUID = 1L;
    private final JsonNode arrayNode;

    public JsonDataProvider(JsonNode arrayNode) {
        if (!arrayNode.isArray()) {
            throw new IllegalArgumentException("json array required");
        }
        this.arrayNode = arrayNode;
    }
    
    public JsonDataProvider(JsonModel model) {
        this(model.getJsonNode());
    }
    
    public JsonDataProvider(JsonModel model, String subpath) {
        this(model.getJsonNode().get(subpath));
    }
    
    @Override
    public void detach() {
    }

    @Override
    public Iterator iterator(int first, int count) {
        Iterator<JsonNode> it = arrayNode.iterator();
        while (first-- > 0) {   // fix starting position
            it.next();
        }
        return it;
    }

    @Override
    public int size() {
        return arrayNode.size();
    }

    @Override
    public IModel model(Object object) {
        JsonNode node = (JsonNode) object;
        if (node.isObject() || node.isArray()) {
            // construct a model to access nested object
            return new JsonModel(node); 
        } else {
            // primitive value: create new JSON to access it with key "value" 
            // that has to be used in the wicket:id
            return new JsonModel("{\"value\": \""+ node.asText()+"\"}");
        }
    }
    
}