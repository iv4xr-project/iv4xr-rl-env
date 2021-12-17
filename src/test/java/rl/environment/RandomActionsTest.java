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

		int numEpisodes = 5;
		int numSteps = 50;
		for (int episode = 0; episode < numEpisodes; episode++) {
			System.out.printf("Starting episode %s%n", episode);
			rlEnv.reset();
			for (int step = 0; step < numSteps; step++) {
				var randNum = rnd.nextInt();
				var action = new RLMoveAction(new double[]{-25.45, 266.85});
				if (randNum % 2 == 0) {
					action = new RLMoveAction(new double[]{-25.45, 266.85});
				}
				//var action = new RLMoveAction(rlEnv.getActionSpace().sample(rnd));
				var stepOutput = rlEnv.step(action);
				var nextObs = stepOutput.getNextObservation();
				double reward = stepOutput.getReward();
				boolean done = stepOutput.isDone();
				var info = stepOutput.getInfo();
				System.out.printf(
						"Current transition: (\n\ts: %s,\n\ta: %s,\n\tr: %f,\n\tns: %s,\n\td: %s\n) at tick %s%n",
						currentObs.getRawObservation(),
						Arrays.toString(action.getRawAction()),
						reward,
						nextObs.getRawObservation(),
						done,
						nextObs.getWomState().timestamp
				);

				currentObs = nextObs;

				if (done) {
					break;
				}
			}
		}
		
		rlEnv.close();		
	}
}
