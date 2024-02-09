import Graphs.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Classifier {
    public static void main(String[] args) {
        Classifier classifier = new Classifier();

        //classifier.loadToFile(1000);
        //classifier.findMax();
        classifier.someExamples(10, false);

        //for (int i = 0; i < 10; i++) {
        //    System.out.println(classifier.aBitOfEverything(false , false, i));
        //}
    }

    private void findMax() {
        double max = 0;
        double current;
        for (int i = 0; i < 1000000; i++) {
            current = aBitOfEverything(false, false, 0);
            if (current > max) {
                max = current;
            }
            if (i % 100 == 0) {
                System.out.println("Iteration: " + i + ", Max: " + max);
            }
        }
    }

    private void loadToFile(int iterations) {
        double[] data = new double[1000];
        for (int i = 0; i < iterations; i++) {
            data[i] = aBitOfEverything(false , false, 0);
            if (i % 100 == 0) {
                System.out.println("Iteration: " + i);
            }
        }

        // File to write the data
        String filename = "numbers.txt";

        // Use try-with-resources to ensure the FileWriter is closed properly
        try (FileWriter writer = new FileWriter(filename)) {
            for (double d : data) {
                writer.write(d + "\n"); // Write each number to the file, followed by a newline
            }
            System.out.println("Data written to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void someExamples(int examples, boolean verbose) {
        for (int i = 0; i < examples; i++) {
            System.out.println(aBitOfEverything(verbose, false, 0));
        }
    }

    private double aBitOfEverything(boolean verbose, boolean plot, int iteration) {
        int numberOfNodes = 10;
        int numberOfInputNodes = 2; // Cannot change right now
        int history = 20;
        double learningRate = 0.01;
        int trainIterations = 1000;
        int trainSize = 400;
        int testSize = 100;
        int inputLength = 100;

        InputEncoder encoder = new InputEncoder();

        Graph graph = new Graph(numberOfNodes, numberOfInputNodes, history);
        graph.generateGraph();

        ReadoutLayer readoutLayer = new ReadoutLayer(numberOfNodes, history, learningRate, trainIterations);

        int[][] X = new int[trainSize][numberOfNodes * history];
        int[] y = new int[trainSize];

        for (int i = 0; i < trainSize; i++) {
            if (i % 2 == 1) {
                graph.updateNodesTTimes(generateRandomInput(inputLength)); // change input
                X[i] = graph.getHistory();
                y[i] = 0;
            } else {
                graph.updateNodesTTimes(generateHarderPeriodicInput(inputLength)); // change input
                X[i] = graph.getHistory();
                y[i] = 1;
            }
            graph.randomizeStateOfGraph();
        }

        readoutLayer.train(X, y);

        double sum = 0;
        for (int i = 0; i < testSize; i++) {
            if (i % 2 == 1) {
                graph.updateNodesTTimes(generateRandomInput(inputLength)); // Change input here
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
                graph.updateNodesTTimes(generateHarderPeriodicInput(inputLength)); // Change input here
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

        double score = sum / testSize;

        if (score > 0.9) {
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
            int period = random.nextInt(5) + 5;
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

    private String getTrainSentencesAI(int i) {
        String[] sentences = {
                "The sun dipped below the horizon.",
                "A cat slept in the sunbeam.",
                "Leaves rustled in the breeze.",
                "Stars twinkled in the night sky.",
                "A gentle rain began to fall.",
                "Children laughed in the distance.",
                "A dog barked somewhere far off.",
                "The moon cast a silvery glow.",
                "Waves crashed against the shore.",
                "Birds chirped in the morning light.",
                "A door creaked open slowly.",
                "Footsteps echoed in the empty hall.",
                "The clock struck midnight.",
                "A car honked in the busy street.",
                "Lightning lit up the sky.",
                "Thunder rumbled ominously.",
                "A flower bloomed overnight.",
                "The wind howled through the trees.",
                "A book lay forgotten on the bench.",
                "Footprints led down to the water.",
                "The air smelled of rain.",
                "A key turned in the lock.",
                "Whispers filled the dark room.",
                "A candle flickered in the window.",
                "The city slept under a blanket of snow.",
                "A train whistle sounded in the distance.",
                "Leaves crunched underfoot.",
                "A bell chimed in the distance.",
                "The river flowed silently.",
                "Smoke rose from the chimney.",
                "The road stretched out ahead.",
                "Mist hung over the fields.",
                "A bird took flight suddenly.",
                "The bridge arched over the river.",
                "A shadow moved in the darkness.",
                "The garden was alive with color.",
                "Ice formed on the windowpane.",
                "The path was lined with flowers.",
                "A fence ran along the field.",
                "The beach was deserted."
        };

        return sentences[i];
    }

    private String getTrainSentencesMe(int i) {
        String[] sentences = {
                "Sentences are made of words.",
                "Words are made of letters.",
                "Sometimes there are symbols in sentences.",
                "What is the difference between words.",
                "Can I write something fun.",
                "I dream of becoming an author.",
                "Actually I don't know if I do.",
                "I like to write random sentences a lot.",
                "This is a short sentence.",
                "This is a slightly longer sentence.",
                "Words, sentences and paragraphs are things.",
                "Do words exist.",
                "Letters can also be sent as mail.",
                "But a letter can also be a character.",
                "A character can be a person.",
                "A person is not a book, nor a letter.",
                "How many sentences do I have to write.",
                "My hobby is to write random sentences.",
                "Isn't random sentences fun to write.",
                "I should've just a pre-made dataset.",
                "But at least I am soon done writing sentences.",
                "This is another sentence I wrote.",
                "I am doing something fun.",
                "Fun is made of letters.",
                "How many words have I written?",
                "This has to be close to the end right.",
                "Letters, words and I don't know what.",
                "I think this is really fun.",
                "What is a sentence exactly?",
                "How come this is so much fun.",
                "This is another word in a sentence.",
                "I know a lot of letters.",
                "I'm starting to think letters is not the same as characters.",
                "But I'm pretty sure it is.",
                "This is another fun sentence isn't it.",
                "Writing 40 sentences takes a while.",
                "But this is close to the last word I write.",
                "I dream of becoming the best author.",
                "Just look at how well I write.",
                "Finally, the last sentence."
        };

        return sentences[i];
    }

    private String getTestSentencesAI(int i) {
        String[] sentences = {
                "A gate swung gently in the wind.",
                "The forest was dense and dark.",
                "A lantern hung above the door.",
                "Footsteps approached slowly.",
                "The mountain stood tall against the sky.",
                "A pen lay forgotten on the desk.",
                "The room was filled with silence.",
                "A map was spread out on the table.",
                "The sun set in a blaze of color.",
                "The night was clear and cold."};
        return sentences[i];
    }

    private String getTestSentencesMe(int i) {
        String[] sentences = {
                "This is a sentence.",
                "I like to write things.",
                "Writing is so much fun.",
                "It is fun to put letters after each other.",
                "Words form sentences.",
                "This is not a sentence.",
                "I have a dream to write more.",
                "I like to write random sentences.",
                "This is a test sentence right.",
                "This is so much fun, I love it."};
        return sentences[i];
    }
}