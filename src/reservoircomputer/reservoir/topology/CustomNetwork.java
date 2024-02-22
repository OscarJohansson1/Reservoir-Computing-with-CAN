package reservoircomputer.reservoir.topology;
import reservoircomputer.reservoir.DrivenCell;
import reservoircomputer.reservoir.AutomataCell;

public class CustomNetwork extends RandomNetwork {
    public CustomNetwork(int numberOfNodes, int numberOfInputNodes, int historyLength, double density) {
        super(numberOfNodes, numberOfInputNodes, historyLength, density);
    }

    @Override
    public void generateGraph(int[] rules) {
        int rule = rules[0];

        for (int i = 0; i < 10; i++) {
            automataCells.add(new AutomataCell(i, rule, recordedHistoryLength));
        }

        for (int i = 0; i < 2; i++) {
            drivenCells[i] = new DrivenCell(numberOfNodes + i + 100);
        }

        AutomataCell current = automataCells.get(0);
        current.addNeighbor(automataCells.get(7));
        current.addNeighbor(automataCells.get(1));
        current.addNeighbor(automataCells.get(4));
        current.addNeighbor(automataCells.get(9));

        current = automataCells.get(1);
        current.addNeighbor(automataCells.get(2));

        current = automataCells.get(2);
        current.addNeighbor(automataCells.get(5));
        current.addNeighbor(automataCells.get(6));
        current.addNeighbor(automataCells.get(1));
        current.addNeighbor(automataCells.get(3));

        current = automataCells.get(3);
        current.addNeighbor(automataCells.get(0));
        current.addNeighbor(automataCells.get(9));
        current.addNeighbor(automataCells.get(8));

        current = automataCells.get(4);
        current.addNeighbor(automataCells.get(3));

        current = automataCells.get(5);
        current.addNeighbor(automataCells.get(6));
        current.addNeighbor(automataCells.get(9));
        current.addNeighbor(automataCells.get(3));
        current.addNeighbor(automataCells.get(4));

        current = automataCells.get(6);
        current.addNeighbor(automataCells.get(9));
        current.addNeighbor(automataCells.get(8));
        current.addNeighbor(drivenCells[1]);

        current = automataCells.get(7);
        current.addNeighbor(automataCells.get(3));
        current.addNeighbor(automataCells.get(0));

        current = automataCells.get(8);
        current.addNeighbor(automataCells.get(0));
        current.addNeighbor(drivenCells[0]);
        current.addNeighbor(automataCells.get(3));

        current = automataCells.get(9);
        current.addNeighbor(automataCells.get(3));
        current.addNeighbor(automataCells.get(7));
        current.addNeighbor(automataCells.get(8));
        current.addNeighbor(automataCells.get(6));
    }
}

/*

Identity: 0, Neighbor: 7, 1, 4, 9, Rule: 11111011, Weight: -0,0004 (0,0000)
Identity: 1, Neighbor: 2, Rule: 00100100, Weight: -0,0001 (0,0014)
Identity: 2, Neighbor: 5, 6, 1, 3, Rule: 10011010, Weight: -0,0001 (0,0014)
Identity: 3, Neighbor: 0, 9, 8, Rule: 00011100, Weight: 0,0000 (0,0000)
Identity: 4, Neighbor: 3, Rule: 10001110, Weight: 0,0000 (0,0000)
Identity: 5, Neighbor: 6, 9, 3, 4, Rule: 10011011, Weight: 0,0001 (0,0021)
Identity: 6, Neighbor: 9, 8, 111, Rule: 10010110, Weight: -0,0000 (0,0023)
Identity: 7, Neighbor: 3, 0, Rule: 01001010, Weight: -0,0004 (0,0000)
Identity: 8, Neighbor: 0, 110, 3, Rule: 10101010, Weight: 0,0024 (0,0039)
Identity: 9, Neighbor: 3, 7, 8, 6, Rule: 00000011, Weight: -0,0003 (0,0018)

*/
