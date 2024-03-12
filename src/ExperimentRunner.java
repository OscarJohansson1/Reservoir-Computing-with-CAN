import reservoircomputer.Classifier;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExperimentRunner {
    private final Classifier classifier;

    public ExperimentRunner() {
        this.classifier = new Classifier("ab", false, 20, 0.01,
                1000, 400, 100, 1000);
    }

    public void timeExperiment() {
        int iterations = 10;
        long startTime = System.nanoTime();

        long ERTime1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            singleRun(10, 0.3, getAllRules(), "ER");
        }
        long ERTime2 = System.nanoTime();

        System.out.println("Average time (ER): " + (ERTime2 - ERTime1) / iterations / 1000000 + " milliseconds");

        long BATime1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            singleRun(10, 0.3, getAllRules(), "BA");
        }
        long BATime2 = System.nanoTime();

        System.out.println("Average time (BA): " + (BATime2 - BATime1) / iterations / 1000000 + " milliseconds");

        long WSTime1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            singleRun(10, 0.3, getAllRules(), "WS");
        }
        long WSTime2 = System.nanoTime();

        System.out.println("Average time (WS): " + (WSTime2 - WSTime1) / iterations / 1000000 + " milliseconds");

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("Elapsed time (total): " + elapsedTime / 1000000 + " milliseconds");
    }


    private double singleRun(int nAutomataCells, double networkDensity, int[] rules, String networkName) {
        classifier.setHyperParameters(nAutomataCells, networkDensity, rules);
        classifier.initializeNetwork(networkName, false);
        classifier.createData();
        classifier.trainNetwork();
        return classifier.testNetwork(false, false);
    }

    private void initializeRun(int nAutomataCells, double networkDensity, String networkName) {
        classifier.setHyperParameters(nAutomataCells, networkDensity, getAllRules());
        classifier.initializeNetwork(networkName, true);
    }

    private void setRunRule(int rule) {
        classifier.changeNetworkRule(rule);
        classifier.createData();
    }

    private double run() {
        classifier.trainNetwork();
        return classifier.testNetwork(false, false);
    }

    public void rulesXNetwork() {
        // File to write the data
        String filename = "RNX2Nsd2.txt";
        double accuracy;
        for (int i = 0; i < 100; i++) {
            try (FileWriter writer = new FileWriter(filename, true)) {
                writer.write(i + ",");
                initializeRun(20, 0.2, "2N");
                for (int j = 0; j < 256; j++) {
                    setRunRule(j);
                    accuracy = run();
                    if (j == 255) {
                        writer.write(String.valueOf(accuracy));
                    } else {
                        writer.write(accuracy + ",");
                    }
                }
                writer.write("\n");
                System.out.println("Data for graph #" + i + " written to file");
            } catch (IOException e) {
                System.out.println("An error occurred when writing to file.");
                e.printStackTrace();
            }
        }
    }


    private double singleRun() {
        return singleRun(2, 0.3, getAllRules(), "CC");
    }

    public void testAllRulesOnCustomNetwork() {
        for (int i = 0; i < 256; i++) {
            double sum = 0;
            double extraSum = 0;
            int extraCount = 0;
            double current;
            for (int n = 0; n < 20; n++) {
                current = singleRun(10, 0.3, new int[]{i}, "CC");
                if (current > 0.6) {
                    extraSum += current;
                    extraCount++;
                }
                sum += current;
            }
            System.out.println("Rule: " + i + " Average: " + String.format("%.2f", sum / 20.0) + " Extra*: " + String.format("%.2f", extraSum) + " / " + extraCount + " = " + String.format("%.2f", extraSum / extraCount));
        }
    }

    public void findMax() {
        double max = 0;
        double current;
        for (int i = 0; i < 1000000; i++) {
            current = singleRun();
            if (current > max) {
                max = current;
            }
            if (i % 100 == 0) {
                System.out.println("Iteration: " + i + ", Max: " + max);
            }
        }
    }

    public void writeToFile() {
        List<Integer> rules = Arrays.asList(15, 85, 117, 245);
        // File to write the data
        String filename = "distribution.txt";
        double distribution;
        double current;
        double max = 0;
        String[] topologies = new String[]{"BA", "ER", "WS", "2N"};

        for (String type : topologies) {
            //if (!rules.contains(i)) {
            //    continue;
            //}
            // Use try-with-resources to ensure the FileWriter is closed properly

            try (FileWriter writer = new FileWriter(filename, true)) {
                writer.write(type + ",");
                for (int j = 0; j < 100; j++) {
                    distribution = (double) j / 100;
                    for (int k = 0; k < 4; k++) {
                        current = singleRun(20, 0.2, new int[]{rules.get(k)}, type);
                        if (current > max) {
                            max = current;
                        }
                    }
                    if (j == 100 - 1) {
                        writer.write(String.valueOf(max));
                    } else {
                        writer.write(max + ",");
                    }
                    max = 0;
                }
                writer.write("\n");
                System.out.println("Data for Type: " + type + " written to file");
            } catch (IOException e) {
                System.out.println("An error occurred when writing to file.");
                e.printStackTrace();
            }

        }

    }

    public void averageOverXRuns(int examples) {
        double sum = 0;
        double max = 0;
        double current;
        for (int i = 0; i < examples; i++) {
            current = singleRun();
            if (current > max) {
                max = current;
            }
            sum += current;
        }
        System.out.println("Average: " + sum / examples + " Max: " + max);
    }

    public void someExamples(int examples, int rule) {
        for (int i = 0; i < examples; i++) {
            System.out.println(singleRun(50, 0.2, getAllRules(), "ER"));
        }
    }

    private int[] getAllRules() {
        int[] rules = new int[256];

        for(int i = 0; i < 256; i++) {
            rules[i] = i;
        }
        return rules;
    }

    /*
    Rules:
    int[] classO = new int[]{0, 32, 96, 128, 160, 233, 235, 250, 251, 254, 255};
        int[] classA = new int[]{4, 8, 12, 19, 23, 35, 40, 50, 51, 55, 60, 64, 68, 72, 76, 77, 104, 132, 136, 140, 153, 156, 168, 178, 179, 192,195, 196, 198, 200, 201, 203, 204, 205, 206, 207, 217, 218, 219, 220, 221, 222, 223, 224, 232, 234, 236, 237, 238, 239, 248, 249, 252, 253};
        int[] classB = new int[]{6, 22, 28, 36, 38, 44, 54, 57, 70, 90, 99, 100, 102, 105, 107, 108, 110, 121, 122, 124, 137, 147, 157, 164, 165, 199, 215};
        int[] classC = new int[]{13, 14, 20, 43, 49, 59, 78, 79, 84, 92, 93, 106, 113, 115, 134, 142, 143, 148, 150, 158, 159, 172, 193, 202, 212, 213, 214, 216, 225, 228};
        int[] classD = new int[]{2, 7, 17, 21, 27, 31, 39, 47, 52, 53, 56, 63, 66, 69, 71, 81, 83, 87, 119, 120, 130, 139, 141, 144, 152, 154, 155, 162, 171, 173, 176, 177, 180, 184, 185, 186, 187, 188, 189, 190, 194, 197, 209, 210, 211, 226, 227, 230, 231, 241, 242};
        int[] classE = new int[]{3, 10, 15, 16, 24, 34, 42, 34, 42, 46, 48, 58, 74, 80, 85, 88, 94, 98, 112, 114, 117, 133, 138, 163, 169, 170, 174, 175, 191, 208, 229, 240, 243, 244, 245, 246, 247};
        int[] classF = new int[]{18, 25, 26, 30, 33, 37, 41, 45, 61, 62, 67, 73, 75, 82, 86, 89, 91, 97, 101, 103, 109, 118, 123, 126, 129, 131, 135, 145, 146, 149, 151, 161, 166, 167, 181, 182, 183};
        int[] classG = new int[]{1, 5, 9, 11, 29, 65, 95, 111, 116, 125, 127};
     */

    private int[] combineArrays(int[] a, int[] b) {
        int length = a.length + b.length;
        int[] combined = new int[length];

        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);

        Arrays.sort(combined);

        return combined;
    }
}
