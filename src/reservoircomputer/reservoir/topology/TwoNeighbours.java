package reservoircomputer.reservoir.topology;

import reservoircomputer.reservoir.AutomataCell;
import reservoircomputer.reservoir.DrivenCell;

public class TwoNeighbours extends RandomNetwork {
    public TwoNeighbours(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
    }

    @Override
    public void generateGraph(int[] rules) {
        int firstNumber;
        int secondNumber;
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
            do {
                firstNumber = random.nextInt(numberOfNodes);
                secondNumber = random.nextInt(numberOfNodes);
            } while (firstNumber == i || secondNumber == i || firstNumber == secondNumber);
            current.addNeighbor(automataCells.get(firstNumber));
            current.addNeighbor(automataCells.get(secondNumber));
        }

        for (DrivenCell cell : drivenCells) {
            int randomNumber = random.nextInt(numberOfNodes);
            current = automataCells.get(randomNumber);
            current.removeRandomNeighbor();
            current.addNeighbor(cell);
        }

        for (AutomataCell automataCell : automataCells) {
            if (automataCell.getNeighborhoodSize() != 2) {
                throw new IllegalStateException("A node in the graph does not have two neighbors");
            }
        }
    }
}
