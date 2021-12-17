package rl.environment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rl.connector.RLAgentConfig;
import rl.connector.RLAgentRequest;
import rl.connector.RLAgentRequestType;
import rl.connector.RLAgentSocketConnector;
import rl.environment.generic.RLEnvSpec;
import rl.environment.intrusion.RLIntrusionConfig;
import rl.environment.intrusion.RLIntrusionEnvironment;
import rl.environment.intrusion.RLMoveAction;

import java.util.Arrays;

public class RLAgentConnectTest {
	
	@Test
	public void serverModeTest()
	{
		var envSpec = new RLEnvSpec<>(
				"IntrusionSim",
				RLIntrusionConfig.ActionSpace,
				RLIntrusionConfig.ObservationSpace
		);
		var config = new RLAgentConfig();
		var connector = new RLAgentSocketConnector(config.host, config.port, RLAgentSocketConnector.Mode.SERVER);

		var rlEnv = new RLIntrusionEnvironment(envSpec);

		int maxEpisodes = 10;
		int currentEpisodes = -1;
		while (currentEpisodes < maxEpisodes) {
			var request = connector.pollRequest();
			System.out.println("Received request: " + request.cmd + " " + request.arg);
			if (request.cmd == RLAgentRequestType.GET_SPEC) {
				connector.sendRLAgentResponse(envSpec);
			} else if (request.cmd == RLAgentRequestType.RESET) {
				var state = rlEnv.reset();
				connector.sendRLAgentResponse(state);
				currentEpisodes += 1;
			} else if (request.cmd == RLAgentRequestType.STEP) {
				var stepOutput = rlEnv.step((RLMoveAction) request.arg);
				connector.sendRLAgentResponse(stepOutput);
			}
		}

	}


	@Test
	public void clientModeTest()
	{
		var envSpec = new RLEnvSpec<>(
				"IntrusionSim",
				RLIntrusionConfig.ActionSpace,
				RLIntrusionConfig.ObservationSpace
		);
		var config = new RLAgentConfig();
		var connector = new RLAgentSocketConnector(config.host, config.port, RLAgentSocketConnector.Mode.CLIENT);
		var response = connector.getRLAgentResponse(RLAgentRequest.envSpec(envSpec));
		Assertions.assertEquals(response, true);
		
		
		var rlEnv = new RLIntrusionEnvironment(envSpec);
		
		var initObs = rlEnv.reset();
		var currentObs = initObs.clone();
		
		int numSteps = 20;
		for (int step = 0; step < numSteps; step++) {
			System.out.println("Waiting for action...");
			// Request the action from the remote RLAgent
			var action = connector.getRLAgentResponse(RLAgentRequest.getAction(currentObs));
			
			// Step the environment
			var stepOutput = rlEnv.step(action);
			
			var nextObs = stepOutput.getNextObservation();
			double reward  = stepOutput.getReward();
			boolean done = stepOutput.isDone();
			var info = stepOutput.getInfo();
			System.out.printf(
					"Current transition: (s: %s, a: %s, r: %f, ns: %s, d: %s) at tick %s%n",
					currentObs.getRawObservation(),
					Arrays.toString(action.getRawAction()),
					reward,
					nextObs.getRawObservation(),
					done,
					nextObs.getWomState().timestamp
			);
			
			// Send the transition data to the remote RLAgent
			response = connector.getRLAgentResponse(RLAgentRequest.logReturns(stepOutput));
			Assertions.assertEquals(response, true);
			
			currentObs = nextObs;
			
			if (done) {
				System.out.println("done");
				break;
			}
		}
		
		rlEnv.close();
		connector.close();
	}
	
}
