package Graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Node implements iNode {
    private final int identity;
    private int currentState;
    private int nextState;
    private final String binaryRule;
    private iNode leftNeighbor;
    private iNode rightNeighbor;
    private final Queue<Integer> history;
    private final int historyLength;


    public Node(int identity, int rule, int historyLength) {
        this.identity = identity;
        this.currentState = Math.random() < 0.5 ? 0 : 1;
        this.nextState = -1;
        String binaryString = Integer.toBinaryString(rule);
        this.binaryRule = String.format("%8s", binaryString).replace(' ', '0');
        this.leftNeighbor = null;
        this.rightNeighbor = null;
        this.history = new LinkedList<>();
        this.historyLength = historyLength;
    }

    public Node(int identity, int rule) {
        // Call the other constructor with a default historyLength value
        this(identity, rule, 10);
    }

    public void addNeighbor(Node neighbor) {
        // Has left / right neighbour
        boolean left = leftNeighbor == null;
        boolean right = rightNeighbor == null;

        if (left && right) {
            if (Math.random() < 0.5) {
                leftNeighbor = neighbor;
            } else {
                rightNeighbor = neighbor;
            }
        } else if (left) {
            leftNeighbor = neighbor;
        } else if (right) {
            rightNeighbor = neighbor;
        } else {
            throw new IllegalStateException("Cannot add more than two neighbors");
        }
    }

    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public int getIdentity() {
        return identity;
    }

    public int getLeftIdentity() {
        return leftNeighbor.getIdentity();
    }

    public int getRightIdentity() {
        return rightNeighbor.getIdentity();
    }

    public String getRule() {
        return binaryRule;
    }

    public void randomizeState() {
        currentState = Math.random() < 0.5 ? 0 : 1;
    }

    public boolean checkNeighbors() {
        return leftNeighbor != null && rightNeighbor != null;
    }

    public void removeNeighbors() {
        leftNeighbor = null;
        rightNeighbor = null;
    }

    public void changeRandomNeighbor(iNode newNeighbor) {
        if (Math.random() < 0.5) {
            leftNeighbor = newNeighbor;
        } else {
            rightNeighbor = newNeighbor;
        }
    }

    public void applyRule() {
        int leftState = leftNeighbor.getCurrentState();
        int centerState = currentState;
        int rightState = rightNeighbor.getCurrentState();

        // Convert the three states into a number between 0 and 7
        int index = (leftState << 2) | (centerState << 1) | rightState;

        // Use the number to get the corresponding bit from the rule's binary representation
        nextState = binaryRule.charAt(7 - index) - '0';
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
