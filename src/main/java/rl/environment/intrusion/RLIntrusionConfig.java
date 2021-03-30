package rl.environment.intrusion;

import rl.environment.generic.RLBoxSpace;

public class RLIntrusionConfig
{
    private final static double[] actionMin = {137385.19069053, 440852.48632809};
    private final static double[] actionMax = {138461.69572571, 441851.79820635};

    public static int numDetections = 5;

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

    // TODO: specify an observation that contains the detections
    public static final RLBoxSpace ObservationSpace = new RLBoxSpace(
            computeObservationMin(), computeObservationMax()
    );
}
