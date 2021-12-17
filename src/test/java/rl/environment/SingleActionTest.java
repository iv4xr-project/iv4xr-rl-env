package rl.environment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rl.environment.generic.RLEnvSpec;
import rl.environment.intrusion.RLIntrusionConfig;
import rl.environment.intrusion.RLIntrusionEnvironment;
import rl.environment.intrusion.RLMoveAction;

public class SingleActionTest {

    @Test
    public void singleActionTest() {
        var envSpec = new RLEnvSpec<>(
                "IntrusionSim",
                RLIntrusionConfig.ActionSpace,
                RLIntrusionConfig.ObservationSpace
        );
        var rlEnv = new RLIntrusionEnvironment(envSpec);
        var action = new RLMoveAction(new double[]{576.55, 394.85});
        var action2 = new RLMoveAction(new double[]{-423.45, 394.85});


        var obs = rlEnv.sendAction(action);
        Assertions.assertNotNull(obs);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        obs = rlEnv.sendAction(action2);
        Assertions.assertNotNull(obs);
        rlEnv.close();
    }
}
