package rl.environment.intrusion;

import rl.environment.generic.RLBoxSpace;
import rl.environment.generic.RLDictSpace;
import rl.environment.generic.RLSpace;

import java.util.HashMap;
import java.util.Map;

/**
 * General configuration of the RL Intrusion Environment.
 *
 * This helps us define the shape and the boundaries of the Observation
 * and Action spaces.
 */
public class RLIntrusionConfig
{
    /**
     * Simulated time elapsed between two decision steps (in seconds).
     */
    public static final double stepInterval = 5.0;
    /**
     * Time acceleration requested to the simulator.
     */
    public static final double timeAccelerationFactor = 5.0;
    /**
     * Synchronization delay (in real time) while querying the simulation time.
     */
    public static final double refreshInterval = 0.001;

    private final static double[] actionMin = {-538.35, -499.75};
    private final static double[] actionMax = {538.35, 499.75};
    public static final double[] goalPosition = {168.20, -224.11};

    public static final int numGuards = 6;
    public static final int numCameras = 14;
    public static final int numDetections = numGuards + numCameras;

    private final static double maxVisionRange = 200; // used for observation space specs
    //public final static double intruderVisionRange = 100;
    public final static double guardVisionRange = 50;
    public final static double cameraVisionRange = 80;

    public static final RLBoxSpace ActionSpace = new RLBoxSpace(
           actionMin, actionMax
    );

    public static double[] computeObservationMin() {
        double[] obsMin = new double[2 + 2 * numDetections]; // position and detections of positions
        for (int i = 0; i < 1 + numDetections; ++i) {
            obsMin[2*i] = actionMin[0];
            obsMin[2*i+1] = actionMin[1];
        }
        return obsMin;
    }

    public static double[] computeObservationMax() {
        double[] obsMax = new double[2 + 2 * numDetections]; // position and detections of positions
        for (int i = 0; i < 1 + numDetections; ++i) {
            obsMax[2*i] = actionMax[0];
            obsMax[2*i+1] = actionMax[1];
        }
        return obsMax;
    }


    private static double[] tiledArray(double[] input, int tileNumber) {
        double[] tiled = new double[tileNumber * input.length];
        for (int i = 0; i < tileNumber; ++i) {
            System.arraycopy(input, 0, tiled, i * input.length, input.length);
        }
        return tiled;
    }

    public static final double[] intruderObsMin, intruderObsMax, guardObsMin, guardObsMax;

    public static final RLDictSpace ObservationSpace;
    static {
        Map<String, RLSpace<?>> obsSpaces = new HashMap<>();
        intruderObsMin = new double[]{actionMin[0], actionMin[1], -Math.PI};
        intruderObsMax = new double[]{actionMax[0], actionMax[1], Math.PI};
        // For the sake of compatibility, we include the (static) vision FOV and range of guards and cameras
        guardObsMin = new double[]{actionMin[0], actionMin[1], -Math.PI, 0, 0};
        guardObsMax = new double[]{actionMax[0], actionMax[1], Math.PI, 2 * Math.PI, maxVisionRange};
        obsSpaces.put("intruder", new RLBoxSpace(intruderObsMin, intruderObsMax));
        obsSpaces.put("goal", new RLBoxSpace(actionMin, actionMax));
        obsSpaces.put("guard", new RLBoxSpace(
                tiledArray(guardObsMin, numGuards), tiledArray(guardObsMax, numGuards))
        );
        obsSpaces.put("fixed_guard", new RLBoxSpace(
                tiledArray(guardObsMin, numCameras), tiledArray(guardObsMax, numCameras))
        );
        ObservationSpace = new RLDictSpace(obsSpaces);
    }

}
