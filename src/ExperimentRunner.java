import reservoircomputer.Classifier;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExperimentRunner {
    private final Classifier classifier;

    public ExperimentRunner() {
        this.classifier = new Classifier("ab", false, 20, 0.01,
                1000, 400, 100, 100);
    }

    private double singleRun(int nAutomataCells, double networkDensity, int[] rules, String networkName) {
        classifier.setHyperParameters(nAutomataCells, networkDensity, rules);
        classifier.initializeNetwork(networkName);
        classifier.createData();
        classifier.trainNetwork();
        return classifier.testNetwork(false, false);
    }

    private double singleRun() {
        return singleRun(10, 0.3, getAllRules(), "ER");
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

    public void writeToFile(int iterations) {
        //List<Integer> rules = Arrays.asList(15, 85, 117, 245);
        // File to write the data
        String filename = "numbers.txt";

        for (int i = 0; i < 256; i++) {
            //if (!rules.contains(i)) {
            //    continue;
            //}
            // Use try-with-resources to ensure the FileWriter is closed properly
            try (FileWriter writer = new FileWriter(filename, true)) {

                writer.write(i + ",");
                for (int j = 0; j < iterations; j++) {
                    if (j == iterations - 1) {
                        writer.write(String.valueOf(singleRun(20, 0.3, new int[]{i}, "WS")));
                    } else {
                        writer.write(singleRun(20, 0.3, new int[]{i}, "WS") + ",");
                    }
                }
                writer.write("\n");
                System.out.println("Data for Rule " + i + " written to file");
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

    public void someExamples(int examples) {
        for (int i = 0; i < examples; i++) {
            System.out.println(singleRun(10, 0.3, new int[]{i}, "ER"));
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
