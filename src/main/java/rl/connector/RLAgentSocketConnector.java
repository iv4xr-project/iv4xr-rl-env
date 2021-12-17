package rl.connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import rl.environment.generic.DictSpaceSerializer;
import rl.environment.generic.RLDictSpace;
import rl.environment.intrusion.RLMoveAction;
import zmq.ZError;

import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connector of the iv4XR RL Environment to a Python RL Agent.
 * This connector can use two modes.
 * With SERVER mode, the Gym interface is provided to the RL Agent
 * through RESET and STEP orders. This is the preferred mode for
 * training.
 * With CLIENT mode, iv4XR runs the control loop and asks the RL Agent
 * for actions with the GET_ACTION request. It can also provide state,
 * reward and information through the LOG_RETURNS request. This can
 * prove useful while deploying a trained RL Agent.
 *
 * For interoperability, The ZeroMQ library is used with TCP Socket as backend
 * and JSON as message format.
 */
public class RLAgentSocketConnector {

	public enum Mode {
		SERVER, CLIENT
	};
	
	/**
	 * An elementary operation with the RL Agent. This is mostly used for wrapping
	 * JSON serialization.
	 */
	static public class RLAgentOperation {
		
		/**
		 * A unique id identifying whoever invokes this operation. E.g. agentid.actionid.
		 */
		public String invokerId ;
		
		/**
		 * A unique id identifying the entity in the real environment to which this operation
		 * is targeted.
		 */
		public String targetId ;
		
		/**
		 * The name of the command that this operation represents.
		 */
		public String command ;
		
		/**
		 * The argment of the operation, if any.
		 */
		public Object arg ;
		
		/**
		 * Used to store the result of the operation, if any.
		 */
		public Object result = null ;
		
		public RLAgentOperation(String invokerId, String targetId, String command, Object arg) {
			this.invokerId = invokerId ;
			this.targetId = targetId ;
			this.command = command ;
			this.arg = arg ;
		}
	}
	
	private static final Logger LOGGER = Logger.getLogger(RLAgentSocketConnector.class.getName());
	public static final Level logLevel = Level.FINEST; // Use info to see the message logs
	
	// transient modifiers should be excluded, otherwise they will be send with json
	private static Gson gson = new GsonBuilder()
		.serializeNulls()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT)
		.registerTypeAdapter(RLDictSpace.class, new DictSpaceSerializer())
		.create();
	
	// initialise socket and input output streams
	ZContext context;
	ZMQ.Socket socket;

	/**
	 * Initialize the connector with the given mode.
	 *
	 * @param address serving address if SERVER mode, RL Agent address if CLIENT mode.
	 * @param port listening port if SERVER mode, RL Agent port if CLIENT mode.
	 * @param mode SERVER of CLIENT mode.
	 */
	public RLAgentSocketConnector(String address, int port, Mode mode) {
		boolean isConnected = false;
		if (mode == Mode.CLIENT) {
			int maxWaitTime = 20000;
			System.out.printf("Trying to connect with client on %s:%s (will time-out after %s seconds).%n", address, port, maxWaitTime/1000);
			long startTime = System.nanoTime();

			while ((!socketReady() || !isConnected) && millisElapsed(startTime) < maxWaitTime) {

				// establish a connection
				try {
					context = new ZContext();
					socket = context.createSocket(SocketType.REQ);
					isConnected = socket.connect("tcp://" + address + ":" + port);
					System.out.println("Just connected to Policy Server");
				} catch (ZError.CtxTerminatedException | ZError.IOException | ZError.InstantiationException u) {
					System.out.println(u);
				}
			}

			if(socketReady() && isConnected) {
				System.out.printf("Connected with server on %s:%s%n", address, port);
			}
			else {
				System.out.println("Could not establish a connection with server.");
			}
		} else if (mode == Mode.SERVER) {
			try {
				context = new ZContext();
				socket = context.createSocket(SocketType.REP);
				socket.bind("tcp://" + address + ":" + port);
			} catch (ZError.CtxTerminatedException | ZError.IOException | ZError.InstantiationException u) {
				System.out.println(u);
			}
		}
	}
	
	/**
	 * @return true if the socket and input output streams are not null
	 */
	private boolean socketReady(){
		return socket != null;
	}

	/**
	 * @param startTimeNano the start time in long
	 * @return the elapsed time from the start time converted to milliseconds
	 */
	private float millisElapsed(long startTimeNano){
		return (System.nanoTime() - startTimeNano) / 1000000f;
	}
	
	/**
	 * Close the socket and input output streams
	 */
	public boolean close() {

		// try to disconnect
		boolean success = getRLAgentResponse(RLAgentRequest.disconnect());

		if(success){
			try {
				if (socket != null)
					socket.close();
				
				System.out.println(String.format("Disconnected from the host."));
			} catch (ZError.IOException e) {
				System.out.println(e.getMessage());
				System.out.println(String.format("Could not disconnect from the host by closing the socket."));
				return false;
			}
		}
		else {
			System.out.println(String.format("Client does not respond to a disconnection request."));
		}
		return success;
	}
	
	
	/**
	 * Send commands to the RL Agent and get responses, when in CLIENT mode.
	 * 
	 * @param invokerId A unique ID identifying the invoker of this method, e.g. in
	 *                  the format agentid.actionid.
	 * @param targetId  The unique ID of the object in the real environment to which
	 *                  the command is directed.
	 * @param command   The name of the command.
	 * @param arg       The arguments to be passed along with the command.
	 * @return an object returned by the real environment as the result of the command, if any.
	 * 
	 * <p>The method may also throws a runtime exception.
	 */
	public Object sendCommand(
			         String invokerId,
			         String targetId,
			         String command,
			         Object arg
			         ) {
		var cmd = new RLAgentOperation(invokerId,targetId,command,arg);
		var response = sendCommand_(cmd);
		cmd.result = response;
		return response ;
	}
	
	/**
	 * Send commands to the RL Agent and get responses, when in CLIENT mode.
	 * 
	 * @param cmd representing the command to send to the real environment.
	 * @return an object that the real environment sends back as the result of the
	 *         command, if any.
	 */
    protected Object sendCommand_(RLAgentOperation cmd) {
    	String message = (String) cmd.arg;
    	JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
		String type = "";
		
		switch (cmd.command) {
		case "debug":
			return printMessage(message);
		case "request":
			try {
				// write to the socket
				socket.send(message);
				LOGGER.log(logLevel, "message sent: " + message);
				
				String messageReceived = socket.recvStr();
				LOGGER.log(logLevel, "message received: " + messageReceived);
				
				// read from the socket
				return messageReceived;
			} catch (ZError.IOException ex) {
				System.out.println("I/O error: " + ex.getMessage());
				return null;
			}
		}
		
		throw new IllegalArgumentException();
	}

	/**
	 * Poll the next request from the RL Agent, when this connector is in
	 * SERVER mode.
	 *
	 * @return request from the RL Agent.
	 */
	public RLAgentRequest<?> pollRequest() {
		try {
			String messageReceived = socket.recvStr();
			var agentOperation = gson.fromJson(messageReceived, RLAgentOperation.class);
			var commandType = RLAgentRequestType.valueOf(agentOperation.command);
			if (commandType == RLAgentRequestType.GET_SPEC || commandType == RLAgentRequestType.RESET) {
				return RLAgentRequest.plainRequest(commandType, agentOperation.arg);
			} else if (commandType == RLAgentRequestType.STEP) {
				return RLAgentRequest.plainRequest(commandType, gson.fromJson(String.valueOf(agentOperation.arg), RLMoveAction.class));
			} else {
				throw new IllegalArgumentException("Illegal command received: " + agentOperation.command);
			}
		} catch (ZError.IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
			return null;
		}
	}
    
	private String printMessage(String message){
		System.out.println("SENDING:" + message);
		return null;
	}
	
	/**
	 * This method provides a higher level wrapper over sendCommand. Main entry
	 * point of the connector, when in CLIENT mode.
	 *
	 * @param request request to the RL Agent.
	 * @param <T> any response type
	 * @return response
	 */
	public <T> T getRLAgentResponse(RLAgentRequest<T> request) {
		String message = (String) sendCommand("APlib", "IS", "request", gson.toJson(request));
		/*if(!close())
			System.out.println(String.format("An error occurred while closing the connection"));*/
		return (T) gson.fromJson(message, request.responseType);
	}

	/**
	 * Send a response to the RL Agent (as JSON), when in SERVER mode.
	 *
	 * @param response response object.
	 * @param <T> response object type.
	 */
	public <T> void sendRLAgentResponse(T response) {
		String jsonMessage = gson.toJson(response);
		// write to the socket
		try {
			socket.send(jsonMessage);
			LOGGER.log(logLevel, "message sent: " + jsonMessage);
		} catch (ZError.IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
	}
}
