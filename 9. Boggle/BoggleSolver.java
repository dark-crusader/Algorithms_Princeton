

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver
{
    private static final int[] RI = {1, 0, -1, -1, -1, 0, 1, 1};
    private static final int[] CI = {1, 1, 1, 0, -1, -1, -1, 0};
    private Set<String> wordsFound;
    private boolean[][] traversed;
    private BoggleBoard boggleBoard;
    private final NodeTrie root;

    private class NodeTrie {
        private NodeTrie[] children = new NodeTrie[26];
        private boolean leafNode = false;
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new NodeTrie();
        for (String string: dictionary) {
            add(string);
        }
    }

    private void add(String s) {
        NodeTrie node = root;
        char[] arr = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (node.children[arr[i] - 'A'] == null) {
                node.children[arr[i] - 'A'] = new NodeTrie();
            }
            node = node.children[arr[i] - 'A'];
        }
        node.leafNode = true;
    }

    private boolean contains(String s) {
        NodeTrie node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            NodeTrie next = node.children[c - 'A'];
            if (next == null) {
                return false;
            }
            node = next;
        }
        return node.leafNode;
    }

    private void dfs(int i, int j, String match, NodeTrie curr) {
        char c = boggleBoard.getLetter(i, j);
        if (curr == null || curr.children[c - 'A'] == null) {
            return;
        }
        // Updated String
        String str;
        if (c == 'Q') {
            str = match + "QU";
            curr = curr.children['Q' - 'A'];
            if (curr.children['U' - 'A'] == null) {
                return;
            }
            curr = curr.children['U' - 'A'];
        } else {
            str = match + c;
            curr = curr.children[c - 'A'];
        }

        // If found a word
        if (curr.leafNode && str.length() > 2) {
            wordsFound.add(str);
        }

        // DFS in all possible directions
        traversed[i][j] = true;
        for (int k = 0; k < 8; k++) {
            int nextRow = i + RI[k];
            int nextCol = j + CI[k];
            if (validIndices(nextRow, nextCol) && !traversed[nextRow][nextCol]) {
                dfs(nextRow, nextCol, str, curr);
            }
        }
        traversed[i][j] = false;
    }

    private boolean validIndices(int row, int col) {
        return row >= 0 && row < boggleBoard.rows() && col >= 0 && col < boggleBoard.cols();
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        wordsFound = new HashSet<>();
        boggleBoard = board;
        int row = board.rows();
        int col = board.cols();
        traversed = new boolean[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                dfs(i, j, "", root);
            }
        }
        boggleBoard = null;
        traversed = null;
        Set<String> res = wordsFound;
        wordsFound = null;
        return res;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word)) {
            int len = word.length();
            if (len < 3) {
                return 0;
            }
            if (len < 5) {
                return 1;
            }
            if (len < 7) {
                return len - 3;
            }
            if (len == 7) {
                return 5;
            }
            return 11;
        } else {
            return 0;
        }
    }
}