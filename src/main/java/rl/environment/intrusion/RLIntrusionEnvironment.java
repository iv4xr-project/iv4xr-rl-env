package rl.environment.intrusion;

import java.util.HashMap;
import java.util.Map;

import intrusionSimulation.ExsuEnvironment;
import intrusionSimulation.ISAgentCommand;
import intrusionSimulation.ISRequest;
import intrusionSimulation.IntrusionSimulationEnvironment;
import rl.environment.generic.RLContinuousActionSpace;
import rl.environment.generic.RLEnvSpec;
import rl.environment.generic.RLEnvironment;
import rl.environment.generic.RLStepOutput;
import world.LegacyObservation;

public class RLIntrusionEnvironment extends RLEnvironment<RLMoveAction, RLIntrusionObservation, RLDummyReward, RLDummyEndCondition, RLContinuousActionSpace> {

	protected IntrusionSimulationEnvironment simulationEnvironment;
	protected ExsuEnvironment exsuEnvironment;
	
	protected int playerAgentId = 1;
	protected boolean initialized = false; 
	
	public RLIntrusionEnvironment(RLEnvSpec<RLContinuousActionSpace> envSpec) {
		super(envSpec);
		this.rewardFunction = new RLDummyReward();
		this.endCondition = new RLDummyEndCondition();
		
		this.exsuEnvironment = new ExsuEnvironment();
		this.simulationEnvironment = new IntrusionSimulationEnvironment();
		this.playerAgentId = 1; // TODO: assess it automatically	
		this.initialized = false;
	}

	@Override
	public RLIntrusionObservation reset() {
		if (!initialized)
		{
			boolean res = exsuEnvironment.getISResponse(ISRequest.startSimulation());
			if (!res) {
				throw new RuntimeException("Unable to start environment");
			}		
			initialized = true;
		}
		else
		{
			boolean res = exsuEnvironment.getISResponse(ISRequest.restartSimulation());
			if (!res) {
				throw new RuntimeException("Unable to reset environment");
			}
		}
		
		LegacyObservation obs = simulationEnvironment.getISResponse(
				ISRequest.command(ISAgentCommand.doNothing(playerAgentId))
		);
		
		initialObservation = new RLIntrusionObservation(obs);
		return initialObservation;
	}

	@Override
	public RLStepOutput<RLIntrusionObservation> step(RLMoveAction action) {
		assert(initialized);
		LegacyObservation obs = simulationEnvironment.getISResponse(
				ISRequest.command(action.asCommand(playerAgentId))
		);
		// ISEnvironment needs to send MOVETO, then observe with DONOTHING to see the agent's movement
		obs = simulationEnvironment.getISResponse(
				ISRequest.command(ISAgentCommand.doNothing(playerAgentId))
		);
		RLIntrusionObservation newObservation = new RLIntrusionObservation(obs);
		double reward = this.rewardFunction.computeReward(currentObservation, action, newObservation);
		boolean done = this.endCondition.isDone(newObservation);
		Map<String, String> info = new HashMap<String, String>();
		currentObservation = newObservation;
		return new RLStepOutput<RLIntrusionObservation>(newObservation, reward, done, info);
	}
	
	public void close() {	
		boolean res = exsuEnvironment.getISResponse(ISRequest.pauseSimulation());
		if (!res) {
			throw new RuntimeException("Unable to stop environment");
		}
		
		if (!simulationEnvironment.closeSocket()) {
			throw new RuntimeException(("Server refuses to close the socket exchange"));
		}
		if (!exsuEnvironment.close()) {
			throw new RuntimeException(("Server refuses to close the socket exchange"));
		}
	}

}
