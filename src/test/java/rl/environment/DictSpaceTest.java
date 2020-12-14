package rl.environment;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import rl.environment.generic.DictSpaceSerializer;
import rl.environment.generic.RLBoxSpace;
import rl.environment.generic.RLBoxSpace2D;
import rl.environment.generic.RLDictSpace;
import rl.environment.generic.RLDiscreteSpace;

public class DictSpaceTest {
	
	@Test
	public void dictSpaceTest() {
		var box = new RLBoxSpace(new double[]{-1.0,  -1.0}, new double[]{1.0, 1.0});
		var box2 = new RLBoxSpace2D(0.0, 1.0, 2, 3);
		var discrete = new RLDiscreteSpace(10);
		
		var spaces = Map.of("pos", box2, "id", discrete);
		var dict = new RLDictSpace(spaces);
		
		var sample = dict.sample(new Random());
		
		var dictOfDict = new RLDictSpace(Map.of("pos", box, "id", discrete, "dict", dict));
		Assertions.assertNotNull(dictOfDict.sample(new Random()));
		
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT)
				.registerTypeAdapter(RLDictSpace.class, new DictSpaceSerializer())
				.setPrettyPrinting()
				.create();
		System.out.println(gson.toJson(sample.get("pos")));
		System.out.println(gson.toJson(dictOfDict));
	}
}
