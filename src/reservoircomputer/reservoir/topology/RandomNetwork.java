package reservoircomputer.reservoir.topology;

import reservoircomputer.reservoir.DrivenCell;
import reservoircomputer.reservoir.AutomataCell;

import java.util.*;

public abstract class RandomNetwork {
    int time;
    int numberOfNodes;
    int numberOfInputNodes;
    double density;
    int recordedHistoryLength;
    List<AutomataCell> automataCells;
    DrivenCell[] drivenCells;
    Random random = new Random();

    public RandomNetwork(int numberOfNodes, int numberOfInputNodes, int recordedHistoryLength, double density) {
        this.time = 0;
        this.numberOfNodes = numberOfNodes;
        this.numberOfInputNodes = numberOfInputNodes;
        this.density = density;
        this.recordedHistoryLength = recordedHistoryLength;
        this.automataCells = new ArrayList<>();
        this.drivenCells = new DrivenCell[numberOfInputNodes];
    }

    public abstract void generateGraph(int[] rules);

    public void updateNodes(int[] inputState) {
        if (inputState.length != numberOfInputNodes) {
            throw new IllegalStateException("Wrong dimension on next input state");
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            drivenCells[i].updateInputNode(inputState[i]);
        }

        for (AutomataCell node : automataCells) {
            node.applyRule();
        }

        for (AutomataCell node : automataCells) {
            node.updateNode();
        }

        time++;
    }

    public void updateNodesTTimes(int[][] inputState) {
        for (int[] inputs : inputState) {
            updateNodes(inputs);
        }
    }

    public int[] getHistory() {
        int[] history = new int[recordedHistoryLength * numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            int[] nodeHistory = automataCells.get(i).getHistory();
            System.arraycopy(nodeHistory, 0, history, recordedHistoryLength * i, recordedHistoryLength);
        }
        return history;
    }

    public void randomizeStateOfGraph() {
        for (AutomataCell node : automataCells) {
            node.randomizeState();
        }
    }

    public void printGraphInfo(double[][] weights) {
        int i = 0;
        for (AutomataCell node : automataCells) {
            System.out.println("Identity: " + node.getIdentity() + ", Neighbor: " + node.getNeighborIdentities() +
                    ", Rule: " + node.getRule() +
                    ", Weight: " + String.format("%.4f", weights[0][i]) + " (" + String.format("%.4f", weights[1][i++]) + ")");
        }
    }

    void addLinkRandomOrientation(AutomataCell a, AutomataCell b) {
        if (Math.random() < 0.5) {
            a.addNeighbor(b);
        } else {
            b.addNeighbor(a);
        }
    }

    boolean addNeighbors(AutomataCell a, AutomataCell b) {
        boolean ab = a.isNeighbor(b);
        boolean ba = b.isNeighbor(a);
        if (ab && ba) {
            return false; // Already 2-way neighbors
        } else if (ab) {
            b.addNeighbor(a);
        } else if (ba) {
            a.addNeighbor(b);
        } else {
            addLinkRandomOrientation(a, b);
        }
        return true;
    }
}

