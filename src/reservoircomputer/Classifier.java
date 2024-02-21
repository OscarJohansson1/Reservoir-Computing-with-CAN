package reservoircomputer;

import reservoircomputer.reservoir.topology.*;
import visualization.GridPlotter;

import java.util.Arrays;

public class Classifier {
    //
    // Reservoir Computing components
    //
    InputGenerator generator;
    RandomNetwork network;
    ReadoutLayer readoutLayer;
    int[][] trainData;
    int[] trainLabels;
    int[][] testData;
    int[] testLabels;
    //
    // Hyper parameters
    //
    // Tunable
    int nAutomataCells;
    double networkDensity;
    int[] rules;
    // Constant
    int nDrivenCells;
    int recordedHistoryLength;
    double learningRate;
    int trainingEpochs;
    int trainSize;
    int testSize;
    int inputDataLength;


    public Classifier(String vocabulary, boolean wildcards, int recordedHistoryLength, double learningRate,
                      int trainEpochs, int trainSize, int testSize, int inputDataLength) {
        this.generator = new InputGenerator(vocabulary, wildcards);
        this.recordedHistoryLength = recordedHistoryLength;
        this.learningRate = learningRate;
        this.trainingEpochs = trainEpochs;
        this.trainSize = trainSize;
        this.testSize = testSize;
        this.inputDataLength = inputDataLength;
        this.nDrivenCells = vocabulary.length();
        if (wildcards) nDrivenCells++;
    }

    public void setHyperParameters(int nAutomataCells, double networkDensity, int[] rules) {
        this.nAutomataCells = nAutomataCells;
        this.networkDensity = networkDensity;
        this.rules = rules;
    }

    public void initializeNetwork(String networkName) {
        switch (networkName) {
            case "ER":
                network = new ErdosRenyi(nAutomataCells, nDrivenCells, recordedHistoryLength, networkDensity);
                break;
            case "BA":
                network = new BarabasiAlbert(nAutomataCells, nDrivenCells, recordedHistoryLength, networkDensity);
                break;
            case "WS":
                network = new WattsStrogatz(nAutomataCells, nDrivenCells, recordedHistoryLength, networkDensity);
                break;
            case "2N":
                network = new TwoNeighbours(nAutomataCells, nDrivenCells, recordedHistoryLength, networkDensity);
                break;
            default:
                throw new IllegalArgumentException("Network name '" + networkName + "' does not exist.");
        }
        network.generateGraph(rules);
    }

    public void createData() {
        double distribution = 0.5;
        trainData = new int[trainSize][nAutomataCells * recordedHistoryLength];
        trainLabels = new int[trainSize];
        testData = new int[testSize][nAutomataCells * recordedHistoryLength];;
        testLabels = new int[testSize];

        for (int i = 0; i < trainSize; i++) {
            if (Math.random() < distribution) {
                network.updateNodesTTimes(generator.generateRandomData(inputDataLength));
                trainData[i] = network.getHistory();
                trainLabels[i] = 0;
            } else {
                network.updateNodesTTimes(generator.generatePeriodicRangeData(inputDataLength,
                        'a', 5, 10));
                trainData[i] = network.getHistory();
                trainLabels[i] = 1;
            }
            network.randomizeStateOfGraph();
        }

        for (int i = 0; i < testSize; i++) {
            if (Math.random() < distribution) {
                network.updateNodesTTimes(generator.generateRandomData(inputDataLength));
                testData[i] = network.getHistory();
                testLabels[i] = 0;
            } else {
                network.updateNodesTTimes(generator.generatePeriodicRangeData(inputDataLength,
                        'a', 5, 10));
                testData[i] = network.getHistory();
                testLabels[i] = 1;
            }
            network.randomizeStateOfGraph();
        }
    }

    public void trainNetwork() {
        readoutLayer = new ReadoutLayer(nAutomataCells, recordedHistoryLength, learningRate, trainingEpochs);
        readoutLayer.train(trainData, trainLabels);
    }

    public double testNetwork(boolean verbose, boolean plot) {
        double accuracyThresholdVerbose = 0.6;
        double sum = 0;
        double[][] weights;
        int prediction;

        for (int i = 0; i < testSize; i++) {
            prediction = readoutLayer.predict(testData[i]);
            if (prediction == testLabels[i]) sum++;
            if (plot) {
                if (i < 4) {
                    plotHistory(i);
                }
            }
        }

        double accuracy = sum / testSize;

        if (verbose && accuracy > accuracyThresholdVerbose) {
            weights = readoutLayer.getAverageAndStdWeights();
            network.printGraphInfo(weights);
        }

        return accuracy;
    }

    private void plotHistory(int iteration) {
        int[] history = network.getHistory();
        int[][] historyPlot = new int[recordedHistoryLength][nAutomataCells];
        for (int j = 0; j < history.length; j++) {
            historyPlot[j % recordedHistoryLength][j / recordedHistoryLength] = history[j];
        }
        String name;
        if (testLabels[iteration] == 0) {
            name = "random";
        } else {
            name = "periodic";
        }
        GridPlotter.createAndShowGui(historyPlot, iteration, name);
    }
}