package rl.environment.generic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Dictionary, i.e. key (String) - value (RLSpace) map, of simpler spaces.
 */
public class RLDictSpace extends RLSpace<HashMap<String, ? extends Serializable>> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */	
	static transient final String NAME = "Dict"; 
	
	public Map<String, RLSpace<?>> spaces;

	/**
	 * Initialize from a key (String) - value (RLSpace) map of spaces.
	 *
	 * @param spaces map of sub-spaces.
	 */
	public RLDictSpace(Map<String, RLSpace<?>> spaces) {
		super(NAME);
		this.spaces = spaces;
	}

	/**
	 * Get a random element from this space.
	 * Uniform sampling is privileged.
	 *
	 * @param rnd random generator.
	 * @return a random element from this space.
	 */
	@Override
	public HashMap<String, ? extends Serializable> sample(Random rnd) {
		var result = new HashMap<String, Serializable>();
		this.spaces.forEach((k, v) -> result.put(k, v.sample(rnd)));
		return result;
	}
}
