package rl.environment.generic;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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