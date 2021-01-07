package rl.environment.intrusion;

import eu.iv4xr.framework.spatial.Vec3;
import intrusionSimulation.ISAgentCommand;
import rl.environment.generic.RLAction;

public class RLMoveAction extends RLAction<double[], ISAgentCommand> {

	public RLMoveAction(double[] rawAction) {
		super(rawAction);
	}

	@Override
	public ISAgentCommand asCommand(int agentId) {
		return ISAgentCommand.moveToCommand(agentId, new Vec3((float) rawAction[0],(float) rawAction[1], 0.0f));
	}

}
