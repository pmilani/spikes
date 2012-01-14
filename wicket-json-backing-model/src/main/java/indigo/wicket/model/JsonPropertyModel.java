package indigo.wicket.model;


import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

class JsonPropertyModel extends AbstractPropertyModel<String> implements IWrapModel<String> {
    private static final long serialVersionUID = 1L;

    private final JsonModel jsonModel;

    public JsonPropertyModel(JsonModel jsonTreeModel) {
        super(jsonTreeModel);
        this.jsonModel = jsonTreeModel;
    }

    @Override
    protected String propertyExpression() {
        return null;
    }

    @Override
    public String getObject() {
        return JsonNodeResolver.asText(jsonModel.getJsonNode(), getPropertyExpression());
    }

    @SuppressWarnings("unchecked")
    public IModel<String> getWrappedModel() {
        return (IModel<String>) getTarget();
    }
}