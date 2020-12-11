package rl.environment;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import rl.environment.generic.RLContinuousActionSpace;
import rl.environment.generic.RLEnvSpec;
import rl.environment.intrusion.RLIntrusionEnvironment;
import rl.environment.intrusion.RLMoveAction;

public class RandomActionsTest {

	@Test
	public void randomActionsTest() {
		double[] actionMin = {-10.0, -10.0};
		double[] actionMax = {10.0, 10.0};
		var actionSpace = new RLContinuousActionSpace(actionMin, actionMax);
		var envSpec = new RLEnvSpec<>("IntrusionSim", actionSpace);
		var rlEnv = new RLIntrusionEnvironment(envSpec);
		
		var initObs = rlEnv.reset();
		var currentObs = initObs.clone();
		Assertions.assertNotNull(initObs);
		
		int numSteps = 10;
		for (int step = 0; step < numSteps; step++) {
			var action = new RLMoveAction(actionSpace.sample());
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
