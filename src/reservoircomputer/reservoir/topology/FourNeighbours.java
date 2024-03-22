package reservoircomputer.reservoir.topology;

import reservoircomputer.reservoir.AutomataCell;
import reservoircomputer.reservoir.DrivenCell;

import java.util.HashSet;
import java.util.Set;

public class FourNeighbours extends RandomNetwork {
    public FourNeighbours(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
    }

    @Override
    public void generateGraph(int[] rules) {
        Set<Integer> numbers = new HashSet<>();
        AutomataCell current;

        for (int i = 0; i < numberOfInputNodes; i++) {
            drivenCells[i] = new DrivenCell(numberOfNodes + i + 100);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            int ruleIndex = random.nextInt(rules.length);
            automataCells.add(new AutomataCell(i, rules[ruleIndex], recordedHistoryLength));
        }

        for (int i = 0; i < numberOfNodes; i++) {
            current = automataCells.get(i);
            numbers.clear();
            while (numbers.size() < 4) {
                int nextNumber = random.nextInt(numberOfNodes);
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
            if (automataCell.getNeighborhoodSize() != 4) {
                throw new IllegalStateException("A node in the graph does not have four neighbors");
            }
        }
    }
}


