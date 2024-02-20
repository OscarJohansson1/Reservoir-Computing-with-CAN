package Graphs;

import java.util.*;

public class MultiGraph {
    int time;
    int numberOfNodes;
    int numberOfInputNodes;
    double density;
    int historyLength;
    List<MultiNode> nodes;
    InputNode[] inputNodes;
    Random random = new Random();

    public MultiGraph(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        this.time = 0;
        this.numberOfNodes = numberOfNodes;
        this.numberOfInputNodes = numberOfInputNodes;
        this.density = density;
        this.historyLength = historyLength; //TODO Change when we know more
        this.nodes = new ArrayList<>();
        this.inputNodes = new InputNode[numberOfInputNodes];
    }

    public void generateGraph(int[] rules, int topology) {
        if (topology == 1) {
            generateErdosRenyi(rules);
        } else if (topology == 2) {
            generateBarabasiAlbert(rules);
        } else if (topology == 3) {
            generateWattsStrogatz(rules);
        } else {
            throw new IllegalStateException("No such topology exists");
        }

    }

    public void generateErdosRenyi(int[] rules) {
        int ruleIndex = random.nextInt(rules.length);
        MultiNode currentNode = new MultiNode(0, rules[ruleIndex], historyLength);
        nodes.add(currentNode);

        MultiNode neighborNode;
        for (int i = 1; i < numberOfNodes; i++) {
            ruleIndex = random.nextInt(rules.length);
            currentNode = new MultiNode(i, rules[ruleIndex], historyLength);

            neighborNode = nodes.get(random.nextInt(nodes.size()));
            nodes.add(currentNode);

            addLinkRandomOrientation(currentNode, neighborNode);
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i] = new InputNode(numberOfNodes + i + 100);
        }

        for (InputNode node : inputNodes) {
            neighborNode = nodes.get(random.nextInt(numberOfNodes));
            neighborNode.addNeighbor(node);
        }

        int k = numberOfNodes - 1;
        int firstIndex;
        int secondIndex;
        MultiNode a;
        MultiNode b;
        while (k < density * numberOfNodes * (numberOfNodes - 1)) {
            do {
                firstIndex = random.nextInt(numberOfNodes);
                secondIndex = random.nextInt(numberOfNodes);
            } while (firstIndex == secondIndex);
            a = nodes.get(firstIndex);
            b = nodes.get(secondIndex);
            if (!(a.isNeighbor(b) || b.isNeighbor(a))) {
                addLinkRandomOrientation(a, b);
                k++;
            } else if (a.isNeighbor(b)) {
                b.addNeighbor(a);
                k++;
            } else if (b.isNeighbor(a)) {
                a.addNeighbor(b);
                k++;
            }
        }

        for (MultiNode node : nodes) {
            node.shuffleNeighbors();
        }
    }

    public void generateBarabasiAlbert(int[] rules) {
        int n = numberOfNodes;
        int n0;
        int m;
        if (density < 0.25) {
            n0 = (int) Math.floor((n + 1 - Math.sqrt(Math.pow(n+1,2)-4*(density * n * (n-1)) + 1))/2);
            m = n0;
        } else {
            n0 = (int) Math.floor((2*n + 1 - Math.sqrt(Math.pow(2*n+1,2)-8*(density * n * (n-1)) + 1))/4);
            m = 2*n0;
        }

        int tau = n - n0;

        for (int i = 0; i < n0; i++) {
            int ruleIndex = random.nextInt(rules.length);
            nodes.add(new MultiNode(i, rules[ruleIndex], historyLength));
        }

        MultiNode currentNode;
        MultiNode neighborNode;
        int firstIndex;
        for (int i = 0; i < tau; i++) {
            int ruleIndex = random.nextInt(rules.length);
            currentNode = new MultiNode(n0 + i, rules[ruleIndex], historyLength);
            for (int j = 0; j < m; j++) {
                while (true) {
                    firstIndex = random.nextInt(n0 + i);
                    neighborNode = nodes.get(firstIndex);
                    if (!(currentNode.isNeighbor(neighborNode) || neighborNode.isNeighbor(currentNode))) {
                        addLinkRandomOrientation(currentNode, neighborNode);
                        break;
                    } else if (currentNode.isNeighbor(neighborNode)) {
                        neighborNode.addNeighbor(currentNode);
                        break;
                    } else if (neighborNode.isNeighbor(currentNode)) {
                        currentNode.addNeighbor(neighborNode);
                        break;
                    }
                }
            }
            nodes.add(currentNode);
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i] = new InputNode(numberOfNodes + i + 100);
        }

        for (InputNode node : inputNodes) {
            neighborNode = nodes.get(random.nextInt(numberOfNodes));
            neighborNode.addNeighbor(node);
        }

        for (MultiNode node : nodes) {
            node.shuffleNeighbors();
        }
    }

    public void generateWattsStrogatz(int[] rules) {
        int h = 2 * (int) Math.floor(density * (numberOfNodes - 1));
        int k = (int) density * numberOfNodes * (numberOfNodes - 1);
        int r = k - numberOfNodes * h / 2;

        for (int i = 0; i < numberOfNodes; i++) {
            int ruleIndex = random.nextInt(rules.length);
            nodes.add(new MultiNode(i, rules[ruleIndex], historyLength));
        }

        h += 2;

        int firstIndex;
        MultiNode currentNode;
        MultiNode neighborNode;
        for (int i = 0; i < numberOfNodes; i++) {
            if (i == r + 1) {
                h -= 2;
            }
            currentNode = nodes.get(i);
            for (int j = 1; j < h / 2; j++) {
                if (Math.random() < 0.9) {
                    neighborNode = nodes.get((i + j) % numberOfNodes);
                    addLinkRandomOrientation(currentNode, neighborNode);
                } else {
                    while (true) {
                        firstIndex = random.nextInt(numberOfNodes - h / 2 - 1);
                        neighborNode = nodes.get((i + firstIndex) % numberOfNodes);
                        if (!(currentNode.isNeighbor(neighborNode) || neighborNode.isNeighbor(currentNode))) {
                            addLinkRandomOrientation(currentNode, neighborNode);
                            break;
                        } else if (currentNode.isNeighbor(neighborNode)) {
                            neighborNode.addNeighbor(currentNode);
                            break;
                        } else if (neighborNode.isNeighbor(currentNode)) {
                            currentNode.addNeighbor(neighborNode);
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i] = new InputNode(numberOfNodes + i + 100);
        }

        for (InputNode node : inputNodes) {
            neighborNode = nodes.get(random.nextInt(numberOfNodes));
            neighborNode.addNeighbor(node);
        }

        for (MultiNode node : nodes) {
            node.shuffleNeighbors();
        }
    }

    private void addLinkRandomOrientation(MultiNode a, MultiNode b) {
        if (Math.random() < 0.5) {
            a.addNeighbor(b);
        } else {
            b.addNeighbor(a);
        }
    }


    public void updateNodes(int[] inputState) {
        if (inputState.length != numberOfInputNodes) {
            throw new IllegalStateException("Wrong dimension on next input state");
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes[i].updateInputNode(inputState[i]);
        }

        for (MultiNode node : nodes) {
            node.applyRule();
        }

        for (MultiNode node : nodes) {
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
            int[] nodeHistory = nodes.get(i).getHistory();
            System.arraycopy(nodeHistory, 0, history, historyLength * i, historyLength);
        }
        return history;
    }

    public void randomizeStateOfGraph() {
        for (MultiNode node : nodes) {
            node.randomizeState();
        }
    }

    public void printGraphInfo(double[][] weights) {
        int i = 0;
        for (MultiNode node : nodes) {
            System.out.println("Identity: " + node.getIdentity() + ", Neighbor: " + node.getNeighborIdentities() +
                    ", Rule: " + node.getRule() +
                    ", Weight: " + String.format("%.4f", weights[0][i]) + " (" + String.format("%.4f", weights[1][i++]) + ")");
        }
    }
}

