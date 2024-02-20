package reservoircomputer;

import java.util.Iterator;

public class InputEncoder {
    private final String vocabulary;
    private final boolean wildcards;
    private int alphabetSize;


    public InputEncoder(String vocabulary, boolean wildcards) {
        this.vocabulary = vocabulary.toLowerCase();
        this.wildcards = wildcards;
        this.alphabetSize = vocabulary.length();
        if (wildcards) this.alphabetSize++;
    }

    public int[] oneHotEncode(char character) {
        int[] encoding = new int[alphabetSize];
        int index = vocabulary.indexOf(character);
        if (index != -1) {
            encoding[index] = 1;
            return encoding;
        }
        if (wildcards) {
            encoding[alphabetSize - 1] = 1;
            return encoding;
        } else {
            throw new IllegalArgumentException("Tried to encode '" + character + "', but character not in vocabulary");
        }
    }
}
