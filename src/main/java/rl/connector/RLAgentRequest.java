package rl.connector;

import rl.environment.generic.RLEnvSpec;
import rl.environment.generic.RLObservation;
import rl.environment.generic.RLStepOutput;
import rl.environment.intrusion.RLMoveAction;

import javax.lang.model.type.NullType;

/**
 * Base request class when connecting to a Python RL Agent.
 *
 * @param <ResponseType>
 */
public class RLAgentRequest<ResponseType> {
	/**
	 * Java can not determine the class of ResponseType at runtime.
	 * In this case, storing an instance of Class<ResponseType> to cast the response object is seen as good practice.
	 */
	public transient final Class<ResponseType> responseType;

	public RLAgentRequestType cmd;
	public Object arg;

	/**
	 * Initialize a request with response type, command and argument.
	 */
	private RLAgentRequest(Class<ResponseType> responseType, RLAgentRequestType cmd, Object arg) {
		this.responseType = responseType;
		this.cmd = cmd;
		this.arg = arg;
	}

	/**
	 * Initialize a request with response type, command and no argument.
	 */
	private RLAgentRequest(Class<ResponseType> responseType, RLAgentRequestType cmd) {
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
	 * @param spec environment specification.
	 * @return success
	 */
	public static RLAgentRequest<Boolean> envSpec(RLEnvSpec<?, ?> spec) {	
		return new RLAgentRequest<>(Boolean.class, RLAgentRequestType.ENV_SPEC, spec);
	}

	/**
	 * Send a request to the agent without waiting for a response.
	 * @param requestType type of the request.
	 * @param arg argument of the request.
	 */
	public static RLAgentRequest<NullType> plainRequest(RLAgentRequestType requestType, Object arg) {
		return new RLAgentRequest<>(NullType.class, requestType, arg);
	}
}
