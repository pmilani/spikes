package indigo.wicket.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonNode;

/**
 * Interprets an expression to navigate through property sub-nodes and arrays and get a value.
 */
public class JsonNodeResolver {
    
    private static Pattern DOT_PROPERTY_PATTERN = Pattern.compile("(\\w+)\\.(.+)");
    private static Pattern ARRAY_ELEMENT_PATTERN = Pattern.compile("(\\w+)\\[(\\d+)\\](?:\\.(.+))?");

    public static String asText(JsonNode node, String expression) {
        if (expression==null || expression.length()==0)
            throw new IllegalArgumentException("invalid expression");
        JsonNode valueNode = navigateNode(node, expression);
        if (valueNode != null) {
            return valueNode.asText();
        } else {
            return null;
        }
    }

    private static JsonNode navigateNode(JsonNode node, String expression) {
        Matcher m = DOT_PROPERTY_PATTERN.matcher(expression);
        if (m.matches()) {
            // navigate subnodes
            return navigateNode(node.with(m.group(1)), m.group(2));
        } 
        
        m = ARRAY_ELEMENT_PATTERN.matcher(expression);
        if (m.matches()) {
            // navigate array
            node = node.get(m.group(1));
            int index = Integer.valueOf(m.group(2));
            String subPath = m.group(3);
            if (subPath != null) {
                return navigateNode(node.get(index), subPath);
            } else {
                return node.get(index);
            }
        }
        
        // end of navigation
        return node.get(expression);
    }
}