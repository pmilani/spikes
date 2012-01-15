package indigo.wicket.model;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class JsonNodeResolverTest {
    
    public static String SOURCE = "{\n" + 
    		"  \"name\" : { \"first\" : \"Joe\", \"last\" : \"Sixpack\" },\n" + 
    		"  \"gender\" : \"MALE\",\n" + 
    		"  \"verified\" : false,\n" + 
    		"  \"phones\" : [\"001234567\",\"002345678\"],\n" + 
    		"  \"parents\" : [ {\"first\":\"Tom\", \"last\":\"Sixpack\"}, {\"first\":\"Jane Q.\", \"last\":\"Public\"} ]"+
    		"}";
    
    JsonNode tree;
    
    @Before
    public void parseTree() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tree = mapper.readTree(SOURCE);
        assertNotNull(tree);
    }
    
    @Test
    public void return_null_when_property_not_found() throws Exception {
        assertNull(JsonNodeResolver.asText(tree, "address"));
    }

    @Test
    public void resolve_value_direct_child() {
        assertEquals("MALE", JsonNodeResolver.asText(tree, "gender"));
    }
    
    @Test
    public void find_object_direct_child() {
        assertNotNull(JsonNodeResolver.asText(tree, "name"));
    }
    
    @Test
    public void resolve_property_second_level_child() {
        assertEquals("Joe", JsonNodeResolver.asText(tree, "name.first"));
    }

    @Test
    public void find_array() {
        assertNotNull(JsonNodeResolver.asText(tree, "phones"));
    }
    
    @Test
    public void resolve_array_element() {
        assertEquals("001234567", JsonNodeResolver.asText(tree, "phones[0]"));
        assertEquals("002345678", JsonNodeResolver.asText(tree, "phones[1]"));
    }
    
    @Test
    public void return_null_for_array_indexes_out_of_bounds() {
        assertNull(JsonNodeResolver.asText(tree, "phones[2]"));
    }

    @Test
    public void resolve_resolve_properties_within_arrays() {
        assertEquals("Jane Q.", JsonNodeResolver.asText(tree, "parents[1].first"));
    }
}