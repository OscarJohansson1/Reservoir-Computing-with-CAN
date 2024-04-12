import reservoircomputer.Classifier;
import reservoircomputer.reservoir.topology.PDNetwork;
import reservoircomputer.reservoir.topology.RandomNetwork;

import java.util.Arrays;

public class GeneticAlgorithm {

    private final Classifier classifier;
    private RandomNetwork[] networks;
    private double[] networkAccuracies;
    private final int[] rules;

    public GeneticAlgorithm() {
        this.classifier = new Classifier("ab", false, 20, 0.01,
                1000, 400, 100, 1000);
        this.rules = getAllRules();
    }

    public void run() {
        initiateNetworks(100);
        while (true) {
            evaluateNetworks();
            if (networkAccuracies[0] > 0.99) break;
            if (networkAccuracies[0] > 0.9) {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Rank: " + i);
                    System.out.println("Accuracy: " + networkAccuracies[i]);
                    networks[i].printGraphInfo();
                }
            }
            updateNetworks();
        }
        for (RandomNetwork network : networks) {
            network.printGraphInfo();
        }
    }

    private void initiateNetworks(int numberOfNetworks) {
        classifier.setHyperParameters(20, 0, rules);
        networks = new RandomNetwork[numberOfNetworks];
        networkAccuracies = new double[numberOfNetworks];
        for (int i = 0; i < numberOfNetworks; i++) {
            RandomNetwork newNetwork = new PDNetwork(20, 2, 20, 0, 2);
            newNetwork.generateGraph(rules);
            networks[i] = newNetwork;
        }
    }

    private void evaluateNetworks() {
        for (int i = 0; i < networks.length; i++) {
            classifier.setNetwork(networks[i]);
            classifier.createData('a');
            classifier.trainNetwork();
            networkAccuracies[i] = classifier.testNetwork(false, false);
        }

        Pair[] pairs = new Pair[networks.length];

        // Populate the pairs array
        for (int i = 0; i < networks.length; i++) {
            pairs[i] = new Pair(networkAccuracies[i], networks[i]);
        }

        // Sort the pairs array
        Arrays.sort(pairs);

        // Extract the sorted values back into the original arrays
        for (int i = 0; i < pairs.length; i++) {
            networkAccuracies[i] = pairs[i].valueFromFirstArray;
            networks[i] = pairs[i].valueFromSecondArray;
        }

        // Output the sorted arrays
        System.out.println("Sorted array1: " + Arrays.toString(networkAccuracies));
        System.out.println("Sorted array2 according to array1: " + Arrays.toString(networks));
    }

    private void updateNetworks() {
        for (int i = 0; i < networks.length; i++) {
            if (i > 0.7 * networks.length) {
                // Random Samples
                RandomNetwork newNetwork = new PDNetwork(20, 2, 20, 0, 2);
                newNetwork.generateGraph(rules);
                networks[i] = newNetwork;
            } else if (i > 0.1 * networks.length) {
                // Mutation (should be crossover too, but I'll wait with that)
                networks[i].mutateNetwork(2);
            } // The remaining samples are Elites
        }
        System.out.println("Update complete");
    }

    static class Pair implements Comparable<Pair> {
        double valueFromFirstArray;
        RandomNetwork valueFromSecondArray;

        // Constructor
        public Pair(double valueFromFirstArray, RandomNetwork valueFromSecondArray) {
            this.valueFromFirstArray = valueFromFirstArray;
            this.valueFromSecondArray = valueFromSecondArray;
        }

        // Compare based on the value from the first array
        @Override
        public int compareTo(Pair other) {
            return Double.compare(other.valueFromFirstArray, this.valueFromFirstArray);
        }
    }

    private int[] getAllRules() {
        int[] rules = new int[256];

        for(int i = 0; i < 256; i++) {
            rules[i] = i;
        }
        return rules;
    }
}
