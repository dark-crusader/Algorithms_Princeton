
public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException("null cannot be passed as argument");
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = "";
        int outcastDist = 0;
        for (String noun : nouns) {
            int dist = 0;
            for (String word : nouns) {
                if (wordNet.isNoun(word))
                    dist += wordNet.distance(noun, word);
            }
            if (dist > outcastDist) {
                outcast = noun;
                outcastDist = dist;
            }
        }
        return outcast;
    }

    // unit tests
    public static void main(String[] args) {

    }
}