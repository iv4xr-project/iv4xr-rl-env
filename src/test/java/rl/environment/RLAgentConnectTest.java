package rl.environment;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import rl.connector.RLAgentConfig;
import rl.connector.RLAgentRequest;
import rl.connector.RLAgentSocketConnector;
import rl.environment.generic.RLContinuousActionSpace;
import rl.environment.generic.RLEnvSpec;
import rl.environment.intrusion.RLIntrusionEnvironment;

public class RLAgentConnectTest {
	
	
	@Test
	public void sendSpecTest()
	{
		double[] actionMin = {-10.0, -10.0};
		double[] actionMax = {10.0, 10.0};
		var actionSpace = new RLContinuousActionSpace(actionMin, actionMax);
		var envSpec = new RLEnvSpec<>("IntrusionSim", actionSpace);
		
		var config = new RLAgentConfig();
		var connector = new RLAgentSocketConnector(config.host, config.port);
		var response = connector.getRLAgentResponse(RLAgentRequest.envSpec(envSpec));
		Assertions.assertEquals(response, true);
		
		
		var rlEnv = new RLIntrusionEnvironment(envSpec);
		
		var initObs = rlEnv.reset();
		var currentObs = initObs.clone();
		
		int numSteps = 50;
		for (int step = 0; step < numSteps; step++) {
			
			// Request the action from the remote RLAgent
			var action = connector.getRLAgentResponse(RLAgentRequest.getAction(initObs));
			
			// Step the environment
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
			
			// Send the transition data to the remote RLAgent
			response = connector.getRLAgentResponse(RLAgentRequest.logReturns(stepOutput));
			Assertions.assertEquals(response, true);
			
			currentObs = nextObs;
			
			if (done) {
				break;
			}
		}
		
		rlEnv.close();
		connector.close();
	}
	
}
