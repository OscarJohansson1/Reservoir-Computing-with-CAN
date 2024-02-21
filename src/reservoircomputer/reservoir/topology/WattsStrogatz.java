package reservoircomputer.reservoir.topology;
import reservoircomputer.reservoir.DrivenCell;
import reservoircomputer.reservoir.AutomataCell;

public class WattsStrogatz extends RandomNetwork {
    public WattsStrogatz(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
    }

    @Override
    public void generateGraph(int[] rules) {
        boolean success;
        int h = 2 * (int) Math.floor(density * (numberOfNodes - 1));
        int k = (int) density * numberOfNodes * (numberOfNodes - 1);
        int r = k - numberOfNodes * h / 2;

        for (int i = 0; i < numberOfNodes; i++) {
            int ruleIndex = random.nextInt(rules.length);
            automataCells.add(new AutomataCell(i, rules[ruleIndex], recordedHistoryLength));
        }

        h += 2;

        int firstIndex;
        AutomataCell currentNode;
        AutomataCell neighborNode;
        for (int i = 0; i < numberOfNodes; i++) {
            if (i == r + 1) {
                h -= 2;
            }
            currentNode = automataCells.get(i);
            for (int j = 1; j < h / 2; j++) {
                if (Math.random() < 0.9) {
                    neighborNode = automataCells.get((i + j) % numberOfNodes);
                    success = addNeighbors(currentNode, neighborNode);
                    if (success) continue;
                }
                do {
                    firstIndex = random.nextInt(numberOfNodes - h / 2 - 1);
                    neighborNode = automataCells.get((i + firstIndex) % numberOfNodes);
                    success = addNeighbors(currentNode, neighborNode);
                } while (!success);
            }
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            drivenCells[i] = new DrivenCell(numberOfNodes + i + 100);
        }

        for (DrivenCell node : drivenCells) {
            neighborNode = automataCells.get(random.nextInt(numberOfNodes));
            neighborNode.addNeighbor(node);
        }

        for (AutomataCell node : automataCells) {
            node.shuffleNeighbors();
        }
    }
}
