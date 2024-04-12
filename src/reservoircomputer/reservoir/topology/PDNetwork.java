package reservoircomputer.reservoir.topology;

import reservoircomputer.reservoir.AutomataCell;
import reservoircomputer.reservoir.DrivenCell;

import java.util.HashSet;
import java.util.Set;

public class PDNetwork extends RandomNetwork {
    private final int predecessors;
    public PDNetwork(int numberOfNodes, int numberOfInputNodes, int historyLength, double density, int predecessors) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
        this.predecessors = predecessors;
    }

    @Override
    public void generateGraph(int[] rules) {
        int ruleIndex = random.nextInt(rules.length);
        int rule = rules[ruleIndex];

        Set<Integer> numbers = new HashSet<>();
        AutomataCell current;

        for (int i = 0; i < numberOfInputNodes; i++) {
            drivenCells[i] = new DrivenCell(numberOfNodes + i + 100);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            automataCells.add(new AutomataCell(i, rule, recordedHistoryLength));
        }

        for (int i = 0; i < numberOfNodes; i++) {
            current = automataCells.get(i);
            numbers.clear();
            while (numbers.size() < predecessors) {
                int nextNumber = random.nextInt(numberOfNodes);
                if (nextNumber == i) continue;
                if (!numbers.contains(nextNumber)) {
                    current.addNeighbor(automataCells.get(nextNumber));
                    numbers.add(nextNumber);
                }
            }
        }

        for (DrivenCell cell : drivenCells) {
            int randomNumber = random.nextInt(numberOfNodes);
            current = automataCells.get(randomNumber);
            current.removeRandomNeighbor();
            current.addNeighbor(cell);
        }

        for (AutomataCell node : automataCells) {
            node.shuffleNeighbors();
        }

        for (AutomataCell automataCell : automataCells) {
            if (automataCell.getNeighborhoodSize() != predecessors) {
                throw new IllegalStateException("A node in the graph does not have the right amount of neighbors: " + automataCell.getNeighborhoodSize());
            }
        }
    }
}


