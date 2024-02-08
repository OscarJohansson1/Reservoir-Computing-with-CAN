import java.util.Iterator;

public class InputEncoder implements Iterable<int[]> {
    private final String sentence;
    private static final int ALPHABET_SIZE = 28; // 26 letters + space + other symbols

    /**
     * Do not repeat characters
     * @param sentence Input sentence
     */
    public InputEncoder(String sentence) {
        this.sentence = sentence.toLowerCase();
    }

    /**
     * Repeat characters n times
     * @param sentence Input sentence
     * @param n Number of times to repeat
     */
    public InputEncoder(String sentence, int n) {
        StringBuilder repeated = new StringBuilder();

        for (char c : sentence.toCharArray()) {
            repeated.append(String.valueOf(c).repeat(Math.max(0, n)));
        }

        sentence = repeated.toString();
        this.sentence = sentence.toLowerCase();
    }

    private int[] oneHotEncode(char character) {
        int[] encoding = new int[ALPHABET_SIZE];
        if (character >= 'a' && character <= 'z') {
            encoding[character - 'a'] = 1;
        } else if (character == ' ') {
            encoding[26] = 1;
        } else {
            encoding[27] = 1;
        }
        return encoding;
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<int[]>() {
            private int currentIndex = 0;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < sentence.length();
            }

            @Override
            public int[] next() {
                int[] encodedChar = oneHotEncode(sentence.charAt(currentIndex));
                currentIndex++;
                return encodedChar;
            }

        };
    }
}
