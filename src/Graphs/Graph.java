package Graphs;

import java.util.*;

public class Graph {
    int time;
    int numberOfNodes;
    int numberOfInputNodes;
    int historyLength;
    Node[] nodes;
    InputNode[] inputNodes;
    Random random = new Random();

    public Graph(int numberOfNodes, int numberOfInputNodes, int historyLength) {
        this.time = 0;
        this.numberOfNodes = numberOfNodes;
        this.numberOfInputNodes = numberOfInputNodes;
        this.historyLength = historyLength; //TODO Change when we know more
        this.nodes = new Node[numberOfNodes];
        this.inputNodes = new InputNode[numberOfInputNodes];
    }

    public void generateGraphSetRules(int[] rules) {
        int firstNumber;
        int secondNumber;

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i] = new InputNode(numberOfNodes + i + 100);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            int ruleIndex = random.nextInt(rules.length);
            nodes[i] = new Node(i, rules[ruleIndex], historyLength);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node current = nodes[i];


            do {
                firstNumber = random.nextInt(numberOfNodes);
                secondNumber = random.nextInt(numberOfNodes);
            } while (firstNumber == i || secondNumber == i || firstNumber == secondNumber);

            current.addNeighbor(nodes[firstNumber]);
            current.addNeighbor(nodes[secondNumber]);
        }

        for (InputNode node : inputNodes) {
            int randomNumber = random.nextInt(numberOfNodes);
            nodes[randomNumber].changeRandomNeighbor(node);
        }

        for (Node node : nodes) {
            if (!node.checkNeighbors()) {
                throw new IllegalStateException("A node in the graph does not have two neighbors");
            }
        }
    }

    public void generateGraph() {
        int firstNumber;
        int secondNumber;
        int rule;

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i] = new InputNode(numberOfNodes + i + 100);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            rule = random.nextInt(256);
            nodes[i] = new Node(i, rule, historyLength);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node current = nodes[i];


            do {
                firstNumber = random.nextInt(numberOfNodes);
                secondNumber = random.nextInt(numberOfNodes);
            } while (firstNumber == i || secondNumber == i || firstNumber == secondNumber);

            current.addNeighbor(nodes[firstNumber]);
            current.addNeighbor(nodes[secondNumber]);
        }

        for (InputNode node : inputNodes) {
            int randomNumber = random.nextInt(numberOfNodes);
            nodes[randomNumber].changeRandomNeighbor(node);
        }

        /**
         *
        do {
            firstNumber = random.nextInt(numberOfNodes);
            secondNumber = random.nextInt(numberOfNodes);
        } while (firstNumber == secondNumber);

        nodes[firstNumber].changeRandomNeighbor(inputNodes[0]);
        nodes[secondNumber].changeRandomNeighbor(inputNodes[1]);

        **/

        for (Node node : nodes) {
            if (!node.checkNeighbors()) {
                throw new IllegalStateException("A node in the graph does not have two neighbors");
            }
        }

    }

    public void updateNodes(int[] inputState) {
        if (inputState.length != numberOfInputNodes) {
            throw new IllegalStateException("Wrong dimension on next input state");
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i].updateInputNode(inputState[i]);
        }

        for (Node node : nodes) {
            node.applyRule();
        }

        for (Node node : nodes) {
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
        int[] history = new int[historyLength * numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            int[] nodeHistory = nodes[i].getHistory();
            System.arraycopy(nodeHistory, 0, history, historyLength * i, historyLength);
        }
        return history;
    }

    public void randomizeStateOfGraph() {
        for (Node node : nodes) {
            node.randomizeState();
        }
    }

    public void printGraphInfo(double[][] weights) {
        int i = 0;
        for (Node node : nodes) {
            System.out.println("Identity: " + node.getIdentity() + ", Left: " + node.getLeftIdentity() +
                    ", Right: " + node.getRightIdentity() + ", Rule: " + node.getRule() +
                    ", Weight: " + String.format("%.4f", weights[0][i]) + " (" + String.format("%.4f", weights[1][i++]) + ")");
        }
    }
}
