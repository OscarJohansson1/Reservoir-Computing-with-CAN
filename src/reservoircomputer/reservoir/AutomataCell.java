package reservoircomputer.reservoir;

import java.util.*;

public class AutomataCell implements Cell {
    private final int identity;
    private int currentState;
    private int nextState;
    private final String binaryRule;
    private List<Cell> neighbors;
    private final Queue<Integer> history;
    private final int historyLength;
    private final Random random = new Random();


    public AutomataCell(int identity, int rule, int historyLength) {
        this.identity = identity;
        this.currentState = Math.random() < 0.5 ? 0 : 1;
        this.nextState = -1;
        String binaryString = Integer.toBinaryString(rule);
        this.binaryRule = String.format("%8s", binaryString).replace(' ', '0');
        this.neighbors = new ArrayList<>();
        this.history = new LinkedList<>();
        this.historyLength = historyLength;
    }

    public void addNeighbor(Cell neighbor) {
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
            for (Cell neighbor : neighbors) {
                sb.append(neighbor.getIdentity()).append(", ");
            }
        }

        return sb.substring(0, Math.max(0, sb.length() - 2));
    }

    public boolean isNeighbor(Cell node) {
        int identity = node.getIdentity();
        for (Cell neighbor : neighbors) {
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

    public int getNeighborhoodSize() {
        return neighbors.size();
    }

    public void removeAllNeighbors() {
        neighbors = new ArrayList<>();
    }

    public void removeRandomNeighbor() {
        int cellToRemoveIndex = random.nextInt(neighbors.size());
        neighbors.remove(cellToRemoveIndex);
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
        int zeros = 0;
        int ones = 0;
        int value;
        if (len == 0) {
            nextState = currentState;
        } else if (len == 1) {
            nextState = neighbors.get(0).getCurrentState();
        } else {
            // No symmetry
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    value = applySingleRule(neighbors.get(i).getCurrentState(), neighbors.get(j).getCurrentState());
                    if (value ==  0) {
                        zeros++;
                    } else {
                        ones++;
                    }
                }
            }
            /*
            // Symmetry
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if (i != j) {
                        sum += applySingleRule(neighbors.get(i).getCurrentState(), neighbors.get(j).getCurrentState());
                        count++;
                    }
                }
            }*/
            if (zeros > ones) {
                nextState = 0;
            } else {
                nextState = 1;
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

