# iv4XR RL Environment library

This library defines a general interface to define Reinforcement Learning (RL) environments through the
iv4XR framework and a connector to work with remote Deep Reinforcement Learning agents in Python.

An example Reinforcement Learning environment is implemented, based on the iv4XR-IntrusionSimulation
system under test.

An illustration of usage is available in the project's Wiki:
https://github.com/iv4xr-project/iv4xr-rl-env/wiki/Results

## Installation and prerequisites

This project requires Java 15.

This project uses as dependencies the Jeromq and GSON libraries with Maven.
This project depends on the iv4xr-core library. See the aplib repository to generate an
executable JAR with maven. This JAR must be added as one of the project's local dependencies.

This project is currently tested with aplib 1.4.0 version.

To use the `intrusion` package and examples, the iv4XR-IntrusionSimulation must be imported
with this project.

## Usage

Examples of usage are provided in the `test` sources.

To use the RLAgentConnectTest, you will also need to run Python RL Agent code from the iv4xr-rl-trainer
package.

## Main Features

This plugin to the iv4xr-core project is aimed at defining Deep Reinforcement Learning environments with
the System Under Test (SUT). It is compliant with the widely used Gym interface in RL Research. A Gym RL Environment
provides the following methods:
```
state = reset()
state, reward, done, info = step(action)
```
To ease writing general algorithms, each environment can also provide a specification of its input/output formats with
the generic space representations. An environment specifies its Observation space and Action space. We support the common
Discrete (a set of N values), Box (a cartesian product of intervals in R^d) and Dict (a key-value map of spaces) 
spaces of Gym. With this plugin, RL States are adapted from the iv4xr Word Object Model (WOM) and RL Actions are translated
as iv4xr SUT commands. The reward and end condition of the environment are defined by a predicate over the RL State,
or directly over the WOM. Thus, the programmer of the RL Environment defines the end goal of the agent, but the agent is free 
to interact with the environment throughout its training procedure. 

## Code architecture

The iv4xr-rl-env plugin is organized with 3 main subpackages:
- The `generic` package describes the main representations of RL Environment and Spaces.
- The `intrusion` package contains the RL training environment associated with the Thales AVS
  powerplant intrusion SUT, providing an example of usage of the generic package.
- The `connector` package allows connecting to a Python DRL agent. The control loop can be managed
  either by the Python DRL agent (i.e. classic Gym control loop, useful for training), or by the
  iv4xr environment (i.e. Policy Server interface, useful for exploitation/deployment).
