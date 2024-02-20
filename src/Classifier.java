import Graphs.Graph;
import Graphs.MultiGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Classifier {
    public static void main(String[] args) {
        Classifier classifier = new Classifier();

        //for (int i = 0; i < 256; i++) {
        //    classifier.loadToFile(i, 100);
        //}

        //classifier.findMax();
        classifier.someExamples(100, false);

        //for (int i = 0; i < 10; i++) {
        //    System.out.println(classifier.aBitOfEverything(false , false, i));
        //}
    }

    private void findMax() {
        double max = 0;
        double current;
        for (int i = 0; i < 1000000; i++) {
            current = aBitOfEverything(false, false, 0, null);
            if (current > max) {
                max = current;
            }
            if (i % 100 == 0) {
                System.out.println("Iteration: " + i + ", Max: " + max);
            }
        }
    }

    private void loadToFile(int rule, int iterations) {
        double[] data = new double[iterations];
        for (int i = 0; i < iterations; i++) {
            data[i] = aBitOfEverything(false , false, 0, null);
            //if (i % 100 == 0) {
            //    System.out.println("Iteration: " + i);
            //}
        }

        // File to write the data
        String filename = "numbers.txt";

        // Use try-with-resources to ensure the FileWriter is closed properly
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(rule + ",");
            for (double d : data) {
                writer.write(d + ","); // Write each number to the file, followed by a newline
            }
            writer.write("\n");
            System.out.println("Rule: " + rule + " written to file");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void someExamples(int examples, boolean verbose) {
        int[] classO = new int[]{0, 32, 96, 128, 160, 233, 235, 250, 251, 254, 255};
        int[] classA = new int[]{4, 8, 12, 19, 23, 35, 40, 50, 51, 55, 60, 64, 68, 72, 76, 77, 104, 132, 136, 140, 153, 156, 168, 178, 179, 192,195, 196, 198, 200, 201, 203, 204, 205, 206, 207, 217, 218, 219, 220, 221, 222, 223, 224, 232, 234, 236, 237, 238, 239, 248, 249, 252, 253};
        int[] classB = new int[]{6, 22, 28, 36, 38, 44, 54, 57, 70, 90, 99, 100, 102, 105, 107, 108, 110, 121, 122, 124, 137, 147, 157, 164, 165, 199, 215};
        int[] classC = new int[]{13, 14, 20, 43, 49, 59, 78, 79, 84, 92, 93, 106, 113, 115, 134, 142, 143, 148, 150, 158, 159, 172, 193, 202, 212, 213, 214, 216, 225, 228};
        int[] classD = new int[]{2, 7, 17, 21, 27, 31, 39, 47, 52, 53, 56, 63, 66, 69, 71, 81, 83, 87, 119, 120, 130, 139, 141, 144, 152, 154, 155, 162, 171, 173, 176, 177, 180, 184, 185, 186, 187, 188, 189, 190, 194, 197, 209, 210, 211, 226, 227, 230, 231, 241, 242};
        int[] classE = new int[]{3, 10, 15, 16, 24, 34, 42, 34, 42, 46, 48, 58, 74, 80, 85, 88, 94, 98, 112, 114, 117, 133, 138, 163, 169, 170, 174, 175, 191, 208, 229, 240, 243, 244, 245, 246, 247};
        int[] classF = new int[]{18, 25, 26, 30, 33, 37, 41, 45, 61, 62, 67, 73, 75, 82, 86, 89, 91, 97, 101, 103, 109, 118, 123, 126, 129, 131, 135, 145, 146, 149, 151, 161, 166, 167, 181, 182, 183};
        int[] classG = new int[]{1, 5, 9, 11, 29, 65, 95, 111, 116, 125, 127};

        int[] tmp = combineArrays(classE, classG);
        tmp = combineArrays(tmp, classF);
        tmp = combineArrays(tmp, classC);
        tmp = combineArrays(tmp, classB);
        tmp = combineArrays(tmp, classA);
        tmp = combineArrays(tmp, classO);
        int[] rules = combineArrays(tmp, classD);

        double sum = 0;
        double accuracy;
        double max = 0;
        for (int i = 0; i < examples; i++) {
            System.out.println(trainMultiGraph(verbose, false, 0, rules, 0.5, 1));
            //System.out.println(aBitOfEverything(verbose, false, i, rules));
        }
            /**
            accuracy =
            if (accuracy >= max) {
                if (max == 1) {
                    max += 1;
                } else {
                    max = accuracy;
                }
            }
            if (i < examples / 10) {
                System.out.println(accuracy);
            }
            sum += accuracy;
        }
        System.out.println("Final accuracy: " + sum / examples + " Max value: " + max);**/
    }

    private int[] combineArrays(int[] a, int[] b) {
        int length = a.length + b.length;
        int[] combined = new int[length];

        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);

        Arrays.sort(combined);

        return combined;
    }

    private double trainMultiGraph(boolean verbose, boolean plot, int iteration, int[] rules, double density, int topology) {
        int numberOfNodes = 15;
        int numberOfInputNodes = 2; // Cannot change right now
        int history = 20;
        double learningRate = 0.01;
        int trainIterations = 1000;
        int trainSize = 400;
        int testSize = 100;
        int inputLength = 1000;

        MultiGraph graph = new MultiGraph(numberOfNodes, numberOfInputNodes, history, density);
        graph.generateGraph(rules, topology);

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

        double accuracy = sum / testSize;

        if (accuracy > 0.6) {
            graph.printGraphInfo(weights);
        }

        return accuracy;
    }


    private double aBitOfEverything(boolean verbose, boolean plot, int iteration, int[] rules) {
        int numberOfNodes = 30;
        int numberOfInputNodes = 2; // Cannot change right now
        int history = 20;
        double learningRate = 0.01;
        int trainIterations = 1000;
        int trainSize = 400;
        int testSize = 100;
        int inputLength = 1000;

        InputEncoder encoder = new InputEncoder();

        Graph graph = new Graph(numberOfNodes, numberOfInputNodes, history);
        graph.generateGraphSetRules(rules);

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

        double accuracy = sum / testSize;

        if (accuracy > 0.6) {
            graph.printGraphInfo(weights);
        }

        return accuracy;
    }

    private int[][] generateRandomInput(int length) {
        int[][] input = new int[length][2];
        for (int i = 0; i < length; i++) {
            if (Math.random() < 0.6) {
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