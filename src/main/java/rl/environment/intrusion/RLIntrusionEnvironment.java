package rl.environment.intrusion;

import intrusionSimulation.*;
import rl.environment.generic.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the RL Environment for the Power plant Intrusion simulation.
 */
public class RLIntrusionEnvironment extends RLEnvironment<
		RLMoveAction, RLIntrusionObservation, RLDummyReward, RLDummyEndCondition, RLBoxSpace, RLDictSpace> {

	protected IntrusionSimulationEnvironment simulationEnvironment;
	protected ExsuEnvironment exsuEnvironment;
	protected int stepNumber = 0;

	protected int playerAgentId = 2;
	protected boolean initialized = false;

	protected long lastStepTime = 0; // Timestamp of the previous decision step
	protected long nextStepTime = 0; // Requested timestamp of the next decision step
	protected long startStepTime = 0;
	
	public RLIntrusionEnvironment(RLEnvSpec<RLBoxSpace, RLDictSpace> envSpec) {
		super(envSpec);
		this.rewardFunction = new RLDummyReward();
		this.endCondition = new RLDummyEndCondition();

		//this.exsuEnvironment = new ExsuEnvironment();
		this.simulationEnvironment = new IntrusionSimulationEnvironment();
		// TODO: assess it automatically, or manage it in the lower level connectors
		this.playerAgentId = 2;
		this.initialized = false;
		this.stepNumber = 0;

		this.lastStepTime = 0;
		this.nextStepTime = 0;
		this.startStepTime = 0;
	}

	public ISObservation sendAction(RLMoveAction action) {
		ISObservation obs = simulationEnvironment.sendRequest(
				ISRequest.command(action.asCommand(playerAgentId))
		);
		return obs;
	}

	/**
	 * Reset this environment to its initial state.
	 *
	 * @return initial observation.
	 */
	@Override
	public RLIntrusionObservation reset() {
		if (!initialized)
		{
//			boolean res = exsuEnvironment.getISResponse(ISRequest.startSimulation());
//			if (!res) {
//				throw new RuntimeException("Unable to start environment");
//			}
			simulationEnvironment.sendRequest(ISRequest.setTimeFactor(RLIntrusionConfig.timeAccelerationFactor));
			if (simulationEnvironment.useSeStar) {
				simulationEnvironment.sendRequest(ISRequest.restartSimulation());
			}
			initialized = true;
		}
		else
		{
			if (simulationEnvironment.useSeStar) {
				simulationEnvironment.sendRequest(ISRequest.restartSimulation());
			}
//			boolean res = exsuEnvironment.getISResponse(ISRequest.restartSimulation());
//			if (!res) {
//				throw new RuntimeException("Unable to reset environment");
//			}
		}
		
		ISObservation obs = simulationEnvironment.sendRequest(
				ISRequest.command(ISAgentCommand.doNothing(playerAgentId))
		);
		this.stepNumber = 0;
		this.lastStepTime = obs.timestamp;
		this.nextStepTime = obs.timestamp;
		this.startStepTime = obs.timestamp;
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}*/
		
		initialObservation = new RLIntrusionObservation(obs);
		return initialObservation;
	}

	/**
	 * Execute one decision step in this environment, and get its output as
	 * next state, reward, done, info.
	 *
	 * This implementation also manages the synchronization with the
	 * simulation time to ensure a constant delay between two decisions
	 * of the agent.
	 *
	 * @param action action provided by the RL Agent.
	 * @return step output as next observation, reward, done and info.
	 */
	@Override
	public RLStepOutput<RLIntrusionObservation> step(RLMoveAction action) {
		assert(initialized);

		if (this.lastStepTime != this.nextStepTime) {
			long simulatedTime;
			long sleepMillis = (long) (1000 * RLIntrusionConfig.refreshInterval);
			do {
				simulatedTime = (long) (1000 * simulationEnvironment.sendRequest(
						ISRequest.getSimulationTime()
				));
				try {
					Thread.sleep(sleepMillis);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			} while (simulatedTime < this.nextStepTime);
		}

		// Previous synchronization mechanism with clock time
		/*if (System.currentTimeMillis() - this.lastStepTime < this.stepInterval * 1000) {
				long remainingMillis = (long) (
						this.stepInterval * 1000 - (System.currentTimeMillis() - this.lastStepTime)
				);
				try {
					Thread.sleep(remainingMillis);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
		}
		this.lastStepTime = System.currentTimeMillis();*/

		ISObservation obs = simulationEnvironment.sendRequest(
				ISRequest.command(action.asCommand(playerAgentId))
		);
		long previousSynchronizationDelay = (obs.timestamp - this.nextStepTime);
		long totalSynchronizationDelay = obs.timestamp - (long) (this.startStepTime + 1000 * RLIntrusionConfig.stepInterval * this.stepNumber);
		// TODO: use a logger
		System.out.println("Delay with previous obs: " + previousSynchronizationDelay + " ms");
		System.out.println("Delay since beginning obs: " + totalSynchronizationDelay + " ms");
		// Milliseconds precision (in simulated time)
		this.nextStepTime += (long) (1000 * RLIntrusionConfig.stepInterval);
		this.lastStepTime = obs.timestamp;
		this.stepNumber += 1;

		// ISEnvironment needs to send MOVETO, then observe with DONOTHING to see the agent's movement
		/*obs = simulationEnvironment.getISResponse(
				ISRequest.command(ISAgentCommand.doNothing(playerAgentId))
		);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}*/
		RLIntrusionObservation newObservation = new RLIntrusionObservation(obs);
		double reward = this.rewardFunction.computeReward(currentObservation, action, newObservation);
		boolean done = this.endCondition.isDone(newObservation);
		Map<String, String> info = new HashMap<>();
		if (obs.isDetected) {
			info.put("is_detected", "true");
			done = true;
		}
		else {
			info.put("is_detected", "false");
		}
		info.put("previous_delay", Long.toString(previousSynchronizationDelay));
		info.put("total_delay", Long.toString(totalSynchronizationDelay));
		currentObservation = newObservation;
		return new RLStepOutput<>(newObservation, reward, done, info);
	}
	
	public void close() {	
//		boolean res = exsuEnvironment.getISResponse(ISRequest.pauseSimulation());
//		if (!res) {
//			throw new RuntimeException("Unable to stop environment");
//		}
		
		if (!simulationEnvironment.closeSocket()) {
			throw new RuntimeException(("Server refuses to close the socket exchange"));
		}
//		if (!exsuEnvironment.close()) {
//			throw new RuntimeException(("Server refuses to close the socket exchange"));
//		}
	}

}
