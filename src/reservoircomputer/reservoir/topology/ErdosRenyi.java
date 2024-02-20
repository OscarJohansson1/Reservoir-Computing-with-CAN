package reservoircomputer.reservoir.topology;
import reservoircomputer.reservoir.DrivenCell;
import reservoircomputer.reservoir.AutomataCell;

public class ErdosRenyi extends RandomNetwork {
    public ErdosRenyi(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
    }

    @Override
    public void generateGraph(int[] rules) {
        int ruleIndex = random.nextInt(rules.length);
        AutomataCell currentNode = new AutomataCell(0, rules[ruleIndex], recordedHistoryLength);
        automataCells.add(currentNode);

        AutomataCell neighborNode;
        for (int i = 1; i < numberOfNodes; i++) {
            ruleIndex = random.nextInt(rules.length);
            currentNode = new AutomataCell(i, rules[ruleIndex], recordedHistoryLength);

            neighborNode = automataCells.get(random.nextInt(automataCells.size()));
            automataCells.add(currentNode);

            addLinkRandomOrientation(currentNode, neighborNode);
        }

        for (int i = 0; i < numberOfInputNodes; i++) {
            drivenCells[i] = new DrivenCell(numberOfNodes + i + 100);
        }

        for (DrivenCell node : drivenCells) {
            neighborNode = automataCells.get(random.nextInt(numberOfNodes));
            neighborNode.addNeighbor(node);
        }

        int k = numberOfNodes - 1;
        int firstIndex;
        int secondIndex;
        AutomataCell a;
        AutomataCell b;
        while (k < density * numberOfNodes * (numberOfNodes - 1)) {
            do {
                firstIndex = random.nextInt(numberOfNodes);
                secondIndex = random.nextInt(numberOfNodes);
            } while (firstIndex == secondIndex);
            a = automataCells.get(firstIndex);
            b = automataCells.get(secondIndex);
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

        for (AutomataCell node : automataCells) {
            node.shuffleNeighbors();
        }
    }
}
