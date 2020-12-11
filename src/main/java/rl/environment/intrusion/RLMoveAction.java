package rl.environment.intrusion;

import helperclasses.datastructures.Vec3;
import intrusionSimulation.ISAgentCommand;
import rl.environment.generic.RLAction;

public class RLMoveAction extends RLAction<double[], ISAgentCommand> {

	public RLMoveAction(double[] rawAction) {
		super(rawAction);
	}

	@Override
	public ISAgentCommand asCommand(int agentId) {
		return ISAgentCommand.moveToCommand(agentId, new Vec3(rawAction[0], rawAction[1], 0.0));
	}

}
