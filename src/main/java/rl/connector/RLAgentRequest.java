package rl.connector;

import com.google.gson.JsonObject;

import rl.environment.generic.RLObservation;
import rl.environment.generic.RLAction;
import rl.environment.generic.RLStepOutput;
import rl.environment.intrusion.RLMoveAction;
import rl.environment.generic.RLEnvSpec;

public class RLAgentRequest<ResponseType> {
	/**
	 * Java can not determine the class of ResponseType at runtime.
	 * In this case, storing an instance of Class<ResponseType> to cast the response object is seen as good practice.
	 */
	public transient final Class<ResponseType> responseType;

	public RLAgentRequestType cmd;
	public Object arg;

	/**
	 * This constructor is based on the sendCommand method from JsonEnvironment
	 */
	private RLAgentRequest(Class<ResponseType> responseType, RLAgentRequestType cmd, Object arg) {
		// convert the command to string
		this.responseType = responseType;

		this.cmd = cmd;
		this.arg = arg;
	}

	/**
	 * This constructor is based on the sendCommand method from JsonEnvironment
	 */
	private RLAgentRequest(Class<ResponseType> responseType, RLAgentRequestType cmd) {
		// convert the command to string
		this.responseType = responseType;

		this.cmd = cmd;
		this.arg = null;
	}

	/**
	 * Disconnect from the agent
	 * @return success
	 */
	public static RLAgentRequest<Boolean> disconnect() {
		return new RLAgentRequest<>(Boolean.class, RLAgentRequestType.DISCONNECT);
	}

	/**
	 * Get an action from the agent
	 * @return action
	 */
	public static RLAgentRequest<RLMoveAction> getAction(RLObservation<?, ?> observation) {
		return new RLAgentRequest<>(RLMoveAction.class, RLAgentRequestType.GET_ACTION, observation);
	}

	/**
	 * Log returns to the agent
	 * @return success
	 */
	public static RLAgentRequest<Boolean> logReturns(RLStepOutput<?> stepOutput) {
		return new RLAgentRequest<>(Boolean.class, RLAgentRequestType.LOG_RETURNS, stepOutput);
	}
	
	/**
	 * Send the environment specifications to the agent
	 * @param spec
	 * @return success
	 */
	public static RLAgentRequest<Boolean> envSpec(RLEnvSpec<?> spec) {
		
		return new RLAgentRequest<Boolean>(Boolean.class, RLAgentRequestType.ENV_SPEC, spec);
	}
	
}
