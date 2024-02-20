package Graphs;

import java.util.*;

public class MultiNode implements iNode {
    private final int identity;
    private int currentState;
    private int nextState;
    private final String binaryRule;
    private List<iNode> neighbors;
    private final Queue<Integer> history;
    private final int historyLength;


    public MultiNode(int identity, int rule, int historyLength) {
        this.identity = identity;
        this.currentState = Math.random() < 0.5 ? 0 : 1;
        this.nextState = -1;
        String binaryString = Integer.toBinaryString(rule);
        this.binaryRule = String.format("%8s", binaryString).replace(' ', '0');
        this.neighbors = new ArrayList<>();
        this.history = new LinkedList<>();
        this.historyLength = historyLength;
    }

    public MultiNode(int identity, int rule) {
        // Call the other constructor with a default historyLength value
        this(identity, rule, 10);
    }

    public void addNeighbor(iNode neighbor) {
        // Has left / right neighbour
        neighbors.add(neighbor);
    }

    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public int getIdentity() {
        return identity;
    }

    public String getNeighborIdentities() {
        StringBuilder sb = new StringBuilder();

        if (neighbors.isEmpty()) {
            sb.append("None  ");
        } else {
            for (iNode neighbor : neighbors) {
                sb.append(neighbor.getIdentity()).append(", ");
            }
        }

        return sb.substring(0, Math.max(0, sb.length() - 2));
    }

    public boolean isNeighbor(iNode node) {
        int identity = node.getIdentity();
        for (iNode neighbor : neighbors) {
            if (neighbor.getIdentity() == identity) {
                return true;
            }
        }
        return false;
    }

    public String getRule() {
        return binaryRule;
    }

    public void randomizeState() {
        currentState = Math.random() < 0.5 ? 0 : 1;
    }

    public int getNumNeighbors() {
        return neighbors.size();
    }

    public void removeAllNeighbors() {
        neighbors = new ArrayList<>();
    }

    public void removeNeighbor(int identity) {
        iNode nodeToRemove = null;
        for (iNode neighbor : neighbors) {
            if (neighbor.getIdentity() == identity) {
                nodeToRemove = neighbor;
                break;
            }
        }
        if (nodeToRemove == null) {
            throw new IllegalStateException("Tries to remove node as neighbor, that is not a neighbor");
        }
        neighbors.remove(nodeToRemove);
    }

    public void shuffleNeighbors() {
        Collections.shuffle(neighbors);
    }

    public int applySingleRule(int leftState, int rightState) {
        int centerState = currentState;

        // Convert the three states into a number between 0 and 7
        int index = (leftState << 2) | (centerState << 1) | rightState;

        // Use the number to get the corresponding bit from the rule's binary representation
        return binaryRule.charAt(7 - index) - '0';
    }

    public void applyRule() {
        int len = neighbors.size();
        if (len == 0) {
            nextState = currentState;
        } else if (len == 1) {
            nextState = neighbors.get(0).getCurrentState();
        } else {
            double sum = 0;
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    sum += applySingleRule(neighbors.get(i).getCurrentState(), neighbors.get(j).getCurrentState());
                }
            }

            if (sum / len > 0.5) {
                nextState = 1;
            } else {
                nextState = 0;
            }
        }
    }

    public void updateNode() {
        if (nextState == -1) {
            throw new IllegalStateException("Tries to update node when next state has not been set");
        } else {
            if (history.size() >= historyLength) {
                history.poll();
            }
            history.offer(currentState);
            currentState = nextState;
            nextState = -1;
        }
    }

    public int[] getHistory() {
        int[] historyArray = new int[history.size()];
        int i = 0;
        for (Integer value : history) {
            historyArray[i++] = value;
        }
        return historyArray;
    }
}

