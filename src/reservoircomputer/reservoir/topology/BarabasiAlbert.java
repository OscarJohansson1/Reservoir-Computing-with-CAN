package reservoircomputer.reservoir.topology;
import reservoircomputer.reservoir.DrivenCell;
import reservoircomputer.reservoir.AutomataCell;

public class BarabasiAlbert extends RandomNetwork {
    public BarabasiAlbert(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
    }

    @Override
    public void generateGraph(int[] rules) {
        boolean success;
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
            automataCells.add(new AutomataCell(i, rules[ruleIndex], recordedHistoryLength));
        }

        AutomataCell currentNode;
        AutomataCell neighborNode;
        int firstIndex;
        for (int i = 0; i < tau; i++) {
            int ruleIndex = random.nextInt(rules.length);
            currentNode = new AutomataCell(n0 + i, rules[ruleIndex], recordedHistoryLength);
            for (int j = 0; j < m; j++) {
                do {
                    firstIndex = random.nextInt(n0 + i);
                    neighborNode = automataCells.get(firstIndex);
                    success = addNeighbors(currentNode, neighborNode);
                } while (!success);
            }
            automataCells.add(currentNode);
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
