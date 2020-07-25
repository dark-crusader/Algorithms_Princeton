
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final Map<Integer, String> idNoun;        // Provides mapping between id and synset nouns
    private final Map<String, Set<Integer>> nounId;   // Provides mapping between a noun and its ids
    private int totalNouns;                     // counts total number of nouns
    private SAP sap;                            // SAP datatype to perform operations on digraph

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkArgs(synsets);
        checkArgs(hypernyms);

        totalNouns = 0;
        idNoun = new HashMap<>();
        nounId = new HashMap<>();
        readSynset(synsets);
        readHypernyms(hypernyms);
    }

    // Reads the nouns
    private void readSynset(String synset) {
        In in = new In(synset);
        String input;
        while (in.hasNextLine()) {
            input = in.readLine();
            String[] parts = input.split(",");
            if (parts.length < 2) continue;
            totalNouns++;
            int id = Integer.parseInt(parts[0]);
            idNoun.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            for (String noun : nouns) {
                if (!nounId.containsKey(noun)) {
                    nounId.put(noun, new HashSet<>());
                }
                nounId.get(noun).add(id);
            }
        }
    }

    // Read noun relations
    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        String input;
        Digraph digraph = new Digraph(totalNouns);
        while (in.hasNextLine()) {
            input = in.readLine();
            String[] strings = input.split(",");
            if (strings.length < 2) continue;
            int source = Integer.parseInt(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                digraph.addEdge(source, Integer.parseInt(strings[i]));
            }
        }
        // Checking if the digraph is not a DAG
        if (new DirectedCycle(digraph).hasCycle()) {
            throw new IllegalArgumentException("No cycles allowed");
        }
        // Checking if DAG has only one root
        int root = 0;
        for (int i = 0; i < digraph.V(); ++i) {
            if (digraph.outdegree(i) == 0) {
                ++root;
                if (root > 1) {
                    throw new IllegalArgumentException(
                            "Only one root allowed. Input has more than one.");
                }
            }
        }
        sap = new SAP(digraph);
    }

    private void checkArgs(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null arguments not allowed.");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkArgs(word);
        return nounId.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        checkArgs(nounA);
        checkArgs(nounB);
        return sap.length(nounId.get(nounA), nounId.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        checkArgs(nounA);
        checkArgs(nounB);
        return idNoun.get(sap.ancestor(nounId.get(nounA), nounId.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}