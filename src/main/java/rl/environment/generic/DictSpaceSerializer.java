package rl.environment.generic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * JSON Serializer for RLDictSpace, helps for interoperability with Python.
 */
public class DictSpaceSerializer implements JsonSerializer<RLDictSpace> {
	@Override
	public JsonElement serialize(RLDictSpace spa, Type type, JsonSerializationContext context) {
		
        JsonObject obj = new JsonObject();
        obj.add("name", context.serialize(spa.name));
        JsonObject spacesObject = new JsonObject();
        spa.spaces.forEach((k, v) -> spacesObject.add(k, context.serialize(v, v.getClass())));
        obj.add("spaces", spacesObject);
        
		return obj;
	}
}