
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static LinkedList<Character> getExtendedAscii() {
        LinkedList<Character> ascii = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            ascii.add((char) i);
        }
        return ascii;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> ascii = getExtendedAscii();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = ascii.indexOf(c);
            BinaryStdOut.write(index, 8);
            ascii.remove(index);
            ascii.addFirst(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> ascii = getExtendedAscii();
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = ascii.get(index);
            BinaryStdOut.write(c, 8);
            ascii.remove(index);
            ascii.addFirst(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException();
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}