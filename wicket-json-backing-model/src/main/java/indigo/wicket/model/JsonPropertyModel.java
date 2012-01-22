package indigo.wicket.model;


import org.apache.wicket.model.AbstractPropertyModel;
import org.codehaus.jackson.JsonNode;

class JsonPropertyModel extends AbstractPropertyModel<String> {
    private static final long serialVersionUID = 1L;

    private final JsonNode jsonNode;

    public JsonPropertyModel(JsonNode jsonNode) {
        super(jsonNode);
        this.jsonNode = jsonNode;
    }

    @Override
    protected String propertyExpression() {
        return null;
    }

    @Override
    public String getObject() {
        return JsonNodeResolver.asText(jsonNode, getPropertyExpression());
    }

}