# Master's Thesis Project: Reservoir Computing with Cellular Automata Networks
Author:
Oscar Johansson, Chalmers University of Technology, Sweden

Email: johaosc@chalmers.se

Supervisor: Zoran Konkoli, MC2, Chalmers University of Technology

Last updated: 2024-02-20

## Introduction
Reservoir Computing is a technique where we use a dynamical system to process input data and then classify it using a simple readout layer. This repository contains the code for my Master's thesis project. The focus of the project is on simulating cellular automata networks with different update rules and topologies to see which setup works best for recognizing patterns in text. We test various configurations of cellular automata to find the most effective way to classify text patterns.

## Usage
The code is written in Java. Open the repository in your IDE of choice. Change the type of experiment, hyperparamters, and network topology directly in the code (see below how). Then run the program using the main class CANReservoirSimulator.

#### CANReservoirSimulator
Main class which specify what experiment to run.

#### ExperimentRunner
Class responsible for experiment logic. Here, all experiments are defined, as well as the current setup of hyperparameters and network topology.

### Package: reservoircomputer
Code for the Reservoir Computing logic.

#### Classifier
Class responsible for how the different reservoir computing components interact. Here, we can change the distribution of input data and how we display information to the terminal.

#### InputGenerator (and InputEncoder)
Class responsible to generate the input according to a certain text structure, the input is also encoded accordingly in the encoder. Here, we can define exactly what kind of data to generate.

#### ReadoutLayer
Class responsible for the classification logic. Here, we use a linear combination of weights and a bias term to produce a weighted sum. The sum is then passed to an activation function (sigmoid function) to obtain a probability for the output. Lastly, the data is classified using a threshold on the sigmoid output. 

### Package: reservoir
Code for the Cellular Automata Networks.

#### DrivenCell and AutomataCell
Classes that manage the cells behavior and update rules. The driven cell is updated manually based on the input each time step, while the automata cell is updated based on its neighbors and transition rules.

#### Package: topology
A package with the abstract class RandomNetwork, which is a general implementation of a random cellular automata network, then the remaining classes in the package are concrete implementations based on different network topologies. As of now, there is support for topologies inspired by Erdos Renyi, scale-free networks, small-world network, and a custom network where each cell has exactly two neighbours.

## Installation
No installation needed. Only standard Java libraries are used.


