package rl.environment.generic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RLDictSpace extends RLSpace<HashMap<String, ? extends Serializable>> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */	
	static transient final String NAME = "Dict"; 
	
	public Map<String, RLSpace<?>> spaces;
	
	public RLDictSpace(Map<String, RLSpace<?>> spaces) {
		super(NAME);
		this.spaces = spaces;
	}

	@Override
	public HashMap<String, ? extends Serializable> sample(Random rnd) {
		var result = new HashMap<String, Serializable>();
		this.spaces.forEach((k, v) -> result.put(k, v.sample(rnd)));
		return result;
	}
}
