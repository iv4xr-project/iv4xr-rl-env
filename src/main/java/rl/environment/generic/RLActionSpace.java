package rl.environment.generic;

import java.util.Random;

public abstract class RLActionSpace<T> {
	protected long rndseed = 1287821 ; // a prime and a palindrome :D
	protected transient Random rnd = new Random(rndseed);
	
	public abstract T sample();
}
