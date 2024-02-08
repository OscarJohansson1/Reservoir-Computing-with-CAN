import Graphs.Graph;

import java.util.Random;

public class Classifier {
    public static void main(String[] args) {
        Classifier classifier = new Classifier();

        for (int i = 0; i < 10; i++) {
            System.out.println(classifier.aBitOfEverything(true , false, i));
        }
    }

    private double aBitOfEverything(boolean verbose, boolean plot, int iteration) {
        int numberOfNodes = 10;
        int numberOfInputNodes = 2; // Cannot change right now
        int history = 30;
        double learningRate = 0.01;
        int trainIterations = 1000;
        int trainSize = 400;
        int testSize = 100;
        int inputLength = 100;

        Graph graph = new Graph(numberOfNodes, numberOfInputNodes, history);
        graph.generateGraph();

        ReadoutLayer readoutLayer = new ReadoutLayer(numberOfNodes, history, learningRate, trainIterations);

        int[][] X = new int[trainSize][numberOfNodes * history];
        int[] y = new int[trainSize];

        for (int i = 0; i < trainSize; i++) {
            if (i % 2 == 1) {
                graph.updateNodesTTimes(generateRandomInput(inputLength));
                X[i] = graph.getHistory();
                y[i] = 0;
            } else {
                graph.updateNodesTTimes(generateHarderPeriodicInput(inputLength));
                X[i] = graph.getHistory();
                y[i] = 1;
            }
            graph.randomizeStateOfGraph();
        }

        readoutLayer.train(X, y);

        double sum = 0;
        for (int i = 0; i < testSize; i++) {
            if (i % 2 == 1) {
                graph.updateNodesTTimes(generateRandomInput(inputLength));
                int prediction = readoutLayer.predict(graph.getHistory());
                if (prediction == 0) {
                    sum++;
                }
                if (plot) {
                    if (i < 4) {
                        int[] h = graph.getHistory();
                        int[][] hPlot = new int[history][numberOfNodes];
                        for (int j = 0; j < h.length; j++) {
                            hPlot[j % history][j / history] = h[j];
                        }
                        GridPlotter.createAndShowGui(hPlot, iteration, "random");
                    }
                }
            } else {
                graph.updateNodesTTimes(generateHarderPeriodicInput(inputLength));
                int prediction = readoutLayer.predict(graph.getHistory());
                if (prediction == 1) {
                    sum++;
                }
                if (plot) {
                    if (i < 4) {
                        int[] h = graph.getHistory();
                        int[][] hPlot = new int[history][numberOfNodes];
                        for (int j = 0; j < h.length; j++) {
                            hPlot[j % history][j / history] = h[j];
                        }
                        GridPlotter.createAndShowGui(hPlot, iteration, "periodic");
                    }
                }
            }
            graph.randomizeStateOfGraph();
        }
        double[][] weights = readoutLayer.getAverageAndStdWeights();

        if (verbose) {
            graph.printGraphInfo(weights);
        }



        return sum / testSize;
    }

    private int[][] generateRandomInput(int length) {
        int[][] input = new int[length][2];
        for (int i = 0; i < length; i++) {
            if (Math.random() < 0.5) {
                input[i] = new int[]{1, 0}; // a
            } else {
                input[i] = new int[]{0, 1}; // b
            }
        }
        return input;
    }

    private int[][] generatePeriodicInput(int length) {
        int[][] input = new int[length][2];
        for (int i = 0; i < length; i++) {
            if (i / 10 % 2 == 1) {
                input[i] = new int[]{1, 0}; // a
            } else {
                input[i] = new int[]{0, 1}; // b
            }
        }
        return input;
    }

    private int[][] generateHarderPeriodicInput(int length) {
        Random random = new Random();
        int[][] input = new int[length][2];
        for (int i = 0; i < length; i++) {
            int period = 5; // random.nextInt(5) + 3;
            if (i % period == 1) {
                input[i] = new int[]{1, 0}; // a
            } else { // else random
                if (Math.random() < 0.5) {
                    input[i] = new int[]{1, 0}; // a
                } else {
                    input[i] = new int[]{0, 1}; // b
                }
            }
        }
        return input;
    }
}