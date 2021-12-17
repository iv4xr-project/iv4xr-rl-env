package rl.environment.intrusion;

import eu.iv4xr.framework.spatial.Vec3;
import intrusionSimulation.ISAgentCommand;
import rl.environment.generic.RLAction;

/**
 * Movement action for the Intruder RL Agent.
 */
public class RLMoveAction extends RLAction<double[], ISAgentCommand> {
	/**
	 * Initialize with a (x, y) destination
	 *
	 * @param rawAction (x, y) destination as two-element array
	 */
	public RLMoveAction(double[] rawAction) {
		super(rawAction);
	}

	/**
	 * Translate the action as a MOVETO command for the Intrusion Simulation.
	 *
	 * @param agentId Identifier of the controlled agent.
	 * @return MOVETO command.
	 */
	@Override
	public ISAgentCommand asCommand(int agentId) {
		return ISAgentCommand.moveToCommand(agentId, new Vec3((float) rawAction[0],(float) rawAction[1], 0.0f));
	}

}
