package rl.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import zmq.ZError;

import org.zeromq.ZMQ;
import org.zeromq.SocketType;
import org.zeromq.ZContext;


public class RLAgentSocketConnector {
	
	
	/**
	 * COPIED FROM APLIB to freeze the interface
	 * An instance of this class is used to record a command that an agent sent to the
	 * real environment through an instance of {@link Environment}. This is so that
	 * the Environment can store what was the last command sent for the purpose of
	 * instrumentation.
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
		.create();
	
	// initialise socket and input output streams
	ZContext context;
	ZMQ.Socket socket;
	
	public RLAgentSocketConnector(String address, int port) {
		
		int maxWaitTime = 20000;
		System.out.println(String.format("Trying to connect with client on %s:%s (will time-out after %s seconds).", address, port, maxWaitTime/1000));
		long startTime = System.nanoTime();

		while (!socketReady() && millisElapsed(startTime) < maxWaitTime)
		{
			// establish a connection
			try {
				context = new ZContext();
				socket = context.createSocket(SocketType.REQ);
				socket.connect("tcp://"+address+":"+port);
				System.out.println("Just connected to Policy Server"); 
			}
			catch (ZError.CtxTerminatedException u) {
				System.out.println(u);
			}
			catch(ZError.IOException i) {
				System.out.println(i);
			}
			catch (ZError.InstantiationException i) {
				System.out.println(i);				
			}
		}

		if(socketReady()) {
			System.out.println(String.format("Connected with server on %s:%s", address, port));
		}
		else {
			System.out.println(String.format("Could not establish a connection with server."));
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

	public boolean closeSocket()
	{
		try {
			if (socket != null)
				socket.close();
			
			System.out.println(String.format("Disconnected from the host."));
		} catch (ZError.IOException e) {
			System.out.println(e.getMessage());
			System.out.println(String.format("Could not disconnect from the host by closing the socket."));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Close the socket and input output streams
	 */
	public boolean close() {

		// try to disconnect
		boolean success = true;//getISResponse(ISRequest.disconnect());

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
	 * Send the specified command to the environment. This method also anticipates
	 * that the environment is populated by multiple reactive entities, so the
	 * command includes an ID if addressing a specific entity in the environment is
	 * needed.
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
	 * Override this method to implement an actual Environment.
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
    
	private String printMessage(String message){
		System.out.println("SENDING:" + message);
		return null;
	}
	
	/**
	 * This method provides a higher level wrapper over Environment.sendCommand. It
	 * calls Environment.sendCommand which in turn will call ISSocketEnvironement.sendCommand_
	 * It will also cast the json back to type T.
	 * @param request
	 * @param <T> any response type
	 * @return response
	 */
	public <T> T getRLAgentResponse(RLAgentRequest<T> request) {
		String message = (String) sendCommand("APlib", "IS", "request", gson.toJson(request));
		/*if(!close())
			System.out.println(String.format("An error occurred while closing the connection"));*/
		return (T) gson.fromJson(message, request.responseType);
	}
}
