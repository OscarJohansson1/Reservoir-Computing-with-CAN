# Data

The data files from the numerous experiments that has been run can be found in this folder. The data files contains the classification accuracy for each Graph-Rule pair. Each file has 100 graphs generated based on the same topology. There are 3 files for each topology, each representing a run with a different type of text structure for the input.

The files are named in the format XXY.txt or XXXY.txt, where XX/XXX corresponds to the topology, and Y to the input type.

## Topologies

The graph topologies that are considered consists of the following:

* Erdős–Rényi (ER)
Edges between nodes appear with a random probability.
* Barabási–Albert (BA)
Scale-free networks, where the presence of hubs are common.
* Watts–Strogatz (WS)
Small-world network, where edges between close neighbours are common, but with a few random long-distance connections.
* Fixed Predecessor Network* (PD-X)
Each node has a fixed number of predecessors (edges directed in to the node), but a variable number of successors (edges directed out of the node). X is the number of predecessors, and predecessors are allocated randomly. *This type of network is introduced in the thesis.

## Hyperparameters

The hyper parameters used for the experiments are as follows:

### General

Automata cells: 20

### Reservoir Computing Specific

Learning rate: 0.01

Training Episodes: 1000

Train Size: 400

Test Size: 100

History for readout layer: the last 20 states of all cells (20x20)

### Text Input Specific

Vocabulary: "ab"

Input length: 100

Input structures: 
- (A) Random Text, p('a') = p('b') = 0.5
- (B) Random Text, p('a') = 0.7 and p('b') = 0.3
- (C) Alternating text between 'a' and 'b'
- (D) Alternating text between 'a' and 'b', with noise

In the different data files, the system classifies whether the text is random (A) or has a structure in accordance to (B), (C), or (D).

### Topology Specific

Target Density (ER/BA/WS): 0.16

WS, Long-Distance Connection Probability: 0.1

Predecessors (PD-2/PD-3/PD-4): 2/3/4

Corresponding Density (PD-2/PD-3/PD-4): 0.11/0.16/0.21

## Previous data

There is also a folder for previous data that was not used for the thesis. The previous data consists of either preliminary results or results that cannot be used due to an error in the data collection process for that experiment run. 
