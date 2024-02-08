
public class ReadoutLayer {
    private final double THRESHOLD = 0.5;
    private final double[] weights; // Model weights
    private final double learningRate; // Learning rate for gradient descent
    private final int iterations; // Number of iterations for training
    private final int historyLength;
    private final int numberOfNodes;

    public ReadoutLayer(int numberOfNodes, int historyLength, double learningRate, int iterations) {
        this.weights = new double[numberOfNodes * historyLength + 1]; // +1 for bias term
        this.learningRate = learningRate;
        this.iterations = iterations;
        this.historyLength = historyLength;
        this.numberOfNodes = numberOfNodes;
    }

    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    public void train(int[][] X, int[] y) {
        for (int iter = 0; iter < iterations; iter++) {
            double[] gradients = new double[weights.length];

            // Calculate gradients for each weight
            for (int i = 0; i < X.length; i++) {
                double predicted = predictProbability(X[i]);
                int error = y[i] - (int) Math.round(predicted);
                for (int j = 0; j < weights.length - 1; j++) {
                    gradients[j] += error * X[i][j];
                }
                gradients[weights.length - 1] += error; // Bias term gradient
            }

            // Update weights
            for (int j = 0; j < weights.length; j++) {
                weights[j] += learningRate * gradients[j] / X.length;
            }
        }
    }

    private double predictProbability(int[] x) {
        double z = weights[weights.length - 1]; // Bias term
        for (int i = 0; i < x.length; i++) {
            z += weights[i] * x[i];
        }
        return sigmoid(z);
    }

    public int predict(int[] x) {
        double probability = predictProbability(x);
        return probability >= THRESHOLD ? 1 : 0;
    }

    public double[][] getAverageAndStdWeights() {
        double[][] results = new double[2][numberOfNodes]; // First row for averages, second row for std devs
        for (int node = 0; node < numberOfNodes; node++) {
            double sum = 0;
            double[] nodeWeights = new double[historyLength];

            // Compute the sum and fill the nodeWeights array
            for (int i = 0; i < historyLength; i++) {
                double weight = weights[node * historyLength + i];
                sum += weight;
                nodeWeights[i] = weight;
            }

            double average = sum / historyLength;
            results[0][node] = average; // Store average

            // Compute standard deviation
            double sumOfSquares = 0;
            for (int i = 0; i < historyLength; i++) {
                sumOfSquares += Math.pow(nodeWeights[i] - average, 2);
            }
            double standardDeviation = Math.sqrt(sumOfSquares / historyLength);
            results[1][node] = standardDeviation; // Store std deviation
        }

        return results;
    }
}
