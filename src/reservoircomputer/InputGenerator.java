package reservoircomputer;

import java.util.Arrays;
import java.util.Random;

public class InputGenerator {
    InputEncoder encoder;
    String vocabulary;
    boolean wildcards;
    int dataLength;
    Random random = new Random();
    public InputGenerator(String vocabulary, boolean wildcards) {
        this.vocabulary = vocabulary;
        this.wildcards = wildcards;
        encoder = new InputEncoder(vocabulary, wildcards);
        dataLength = vocabulary.length();
        if (wildcards) this.dataLength++;
    }

    public int[][] generateRandomlyDistributedData(int nDataPoints, double distribution) {
        int[][] data = new int[nDataPoints][dataLength];
        char sampledChar;
        for (int i = 0; i < nDataPoints; i++) {
            if (Math.random() < distribution) {
                sampledChar = 'a';
            } else {
                sampledChar = 'b';
            }
            data[i] = encoder.oneHotEncode(sampledChar);
        }
        return data;
    }

    public int[][] generateAlternatingData(int nDataPoints) {
        int[][] data = new int[nDataPoints][dataLength];
        char sampledChar;
        if (Math.random() < 0.5) {
            sampledChar = 'a';
        } else {
            sampledChar = 'b';
        }
        data[0] = encoder.oneHotEncode(sampledChar);
        for (int i = 1; i < nDataPoints; i++) {
            if (sampledChar == 'a') {
                sampledChar = 'b';
            } else {
                sampledChar = 'a';
            }
            data[i] = encoder.oneHotEncode(sampledChar);
        }
        return data;
    }

    public int[][] generateRandomData(int nDataPoints) {
        int[][] data = new int[nDataPoints][dataLength];

        for (int i = 0; i < nDataPoints; i++) {
            char sampledChar = getRandomChar();
            data[i] = encoder.oneHotEncode(sampledChar);
        }
        return data;
    }

    public int[][] generatePeriodicData(int nDataPoints, char character, int period) {
        int[][] data = new int[nDataPoints][2];
        for (int i = 0; i < nDataPoints; i++) {
            if (i % period == 1) {
                data[i] = encoder.oneHotEncode(character);
            } else {
                char sampledChar = getRandomChar();
                data[i] = encoder.oneHotEncode(sampledChar);
            }
        }
        return data;
    }

    public int[][] generatePeriodicRangeData(int nDataPoints, char character, int minPeriod, int maxPeriod) {
        int[][] data = new int[nDataPoints][2];
        int period = random.nextInt(maxPeriod - minPeriod) + minPeriod;
        for (int i = 0; i < nDataPoints; i++) {
            if (i % period == 1) {
                data[i] = encoder.oneHotEncode(character);
            } else {
                char sampledChar = getRandomChar();
                data[i] = encoder.oneHotEncode(sampledChar);
            }
        }
        return data;
    }

    private char getRandomChar() {
        int randomIndex = random.nextInt(vocabulary.length());
        return vocabulary.charAt(randomIndex);
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
