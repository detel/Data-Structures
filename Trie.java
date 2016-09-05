import java.io.*;
import java.util.*;

public class Trie {
    public static InputReader in;
    public static PrintWriter out;
    public static final int MOD = (int)1e9 + 7;
    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);
        
        TrieNode root = new TrieNode();
        
        out.println(contains(root, "deepit"));
        insertString(root, "deepit");
        out.println(contains(root, "deepit"));
        
        out.close();
    }
    
    public static class TrieNode {
        int count;
        boolean isLeaf;
        TrieNode[] children;
        
        public TrieNode() {
            this.count = 0;
            this.isLeaf = false;
            this.children = new TrieNode[26];
        }
    }

    public static void insertString(TrieNode root, String s) {
        TrieNode curr = root;
        for (char ch : s.toCharArray()) {
            int index = (int)ch - (int)'a';
            if(curr.children[index] == null)
                curr.children[index] = new TrieNode();
            curr = curr.children[index];
            curr.count++;
        }
        if(curr != null)
            curr.isLeaf = true;
    }
    
    public static boolean contains(TrieNode root, String s) {
        TrieNode curr = root;
        for (char ch : s.toCharArray()) {
            int index = (int)ch - (int)'a';
            if (curr.children[index] == null)
                return false;
            curr = curr.children[index];
        }
        return curr.isLeaf;
    }
    
    public static void removeString(TrieNode root, String s) {
        TrieNode curr = root;
        for (char ch : s.toCharArray()) {
            int index = (int)ch - (int)'a';
            TrieNode child = curr.children[index];
            if(child == null)
                return;
            if(child.count == 1){
                curr.children[index] = null;
                return;
            } else {
                child.count--;
                curr = child;
            }
        }
        if(curr != null)
            curr.isLeaf = false;
    }
    
    //maximization of xor - only for binary strings
    public static String max(TrieNode root, String s){
        StringBuilder result = new StringBuilder();
        TrieNode curr = root;
        for (char ch : s.toCharArray()) {
            int index = (int)ch- (int)'a';
            if(curr.children[index^1] != null && curr.children[index^1].count > 0) {
                result.append(1);
                curr = curr.children[index^1];
            } else if(curr.children[index] != null && curr.children[index].count > 0) {
                result.append(0);
                curr = curr.children[index];
            } else {
                return result.toString();
            }
        }
        return result.toString();
    }
    
    public static void printSorted(TrieNode node, String s) {
        for (char ch = 0; ch < node.children.length; ch++) {
            TrieNode child = node.children[ch];
            if (child != null)
                printSorted(child, s + ch);
        }
        if (node.isLeaf) {
            System.out.println(s);
        }
    }
    
    static class Node implements Comparable<Node>{
        int u, v;
        long score;
        
        public Node (int u, int v) {
            this.u = u;
            this.v = v;
        }
        
        public void print() {
            System.out.println(u + " " + v + " " + score);
        }
        
        public int compareTo(Node that) {
           return Long.compare(this.score, that.score);
        }
    }
    
    static class InputReader {

        private InputStream stream;
        private byte[] buf = new byte[8192];
        private int curChar, snumChars;
        private SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int snext() {
            if (snumChars == -1)
                throw new InputMismatchException();
            if (curChar >= snumChars) {
                curChar = 0;
                try {
                    snumChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (snumChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public int nextInt() {
            int c = snext();
            while (isSpaceChar(c))
                c = snext();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = snext();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = snext();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public long nextLong() {
            int c = snext();
            while (isSpaceChar(c))
                c = snext();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = snext();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = snext();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public int[] nextIntArray(int n) {
            int a[] = new int[n];
            for (int i = 0; i < n; i++)
                a[i] = nextInt();
            return a;
        }

        public String readString() {
            int c = snext();
            while (isSpaceChar(c))
                c = snext();
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = snext();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null)
                return filter.isSpaceChar(c);
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);
        }
    }
}  
