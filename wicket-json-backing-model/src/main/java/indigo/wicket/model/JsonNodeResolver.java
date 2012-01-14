package indigo.wicket.model;

import org.codehaus.jackson.JsonNode;

public class JsonNodeResolver {

    public static String asText(JsonNode node, String expression) {
        if (expression==null || expression.length()==0)
            throw new IllegalArgumentException("invalid expression");
        if (expression.contains(".")) {
            int index = expression.indexOf(".");
            String relpath = expression.substring(0, index);
            return asText(node.with(relpath), expression.substring(index+1));
        } else {
            return node.get(expression).asText();
        }
    }
}