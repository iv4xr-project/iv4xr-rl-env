package rl.environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rl.environment.generic.*;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Random;

public class DictSpaceTest {
	
	@Test
	public void dictSpaceTest() {
		var box = new RLBoxSpace(new double[]{-1.0,  -1.0}, new double[]{1.0, 1.0});
		var box2 = new RLBoxSpace2D(0.0, 1.0, 2, 3);
		var discrete = new RLDiscreteSpace(10);
		
		var spaces = Map.of("pos", box2, "id", discrete);
		// Javac needs the explicit cast while eclipse compiler doesn't
		var dict = new RLDictSpace((Map<String, RLSpace<?>>) spaces);
		
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
