package rl.environment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rl.environment.generic.RLEnvSpec;
import rl.environment.intrusion.RLIntrusionConfig;
import rl.environment.intrusion.RLIntrusionEnvironment;
import rl.environment.intrusion.RLMoveAction;

import java.util.Arrays;
import java.util.Random;

public class RandomActionsTest {

	@Test
	public void randomActionsTest() {
		long rndSeed = 1287821 ; // a prime and a palindrome :D
		Random rnd = new Random(rndSeed);

		var envSpec = new RLEnvSpec<>(
				"IntrusionSim",
				RLIntrusionConfig.ActionSpace,
				RLIntrusionConfig.ObservationSpace
		);
		var rlEnv = new RLIntrusionEnvironment(envSpec);
		
		var initObs = rlEnv.reset();
		var currentObs = initObs.clone();
		Assertions.assertNotNull(initObs);
		
		int numSteps = 20;
		for (int step = 0; step < numSteps; step++) {
			var action = new RLMoveAction(rlEnv.getActionSpace().sample(rnd));
			var stepOutput = rlEnv.step(action);
			var nextObs = stepOutput.getNextObservation();
			double reward  = stepOutput.getReward();
			boolean done = stepOutput.isDone();
			var info = stepOutput.getInfo();
			System.out.println(String.format(
					"Current transition: (s: %s, a: %s, r: %f, ns: %s, d: %s) at tick %s",
					Arrays.toString(currentObs.getRawObservation()),
					Arrays.toString(action.getRawAction()),
					reward,
					Arrays.toString(nextObs.getRawObservation()),
					done,
					nextObs.getWomState().tick
			));
			
			currentObs = nextObs;
			
			if (done) {
				break;
			}
		}
		
		rlEnv.close();		
	}
}
