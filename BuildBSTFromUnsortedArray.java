import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
 *  Following code works for any permutation of [1, 2, 3 ..... n]
 *  If there are large values or negative values, use coordinate compression to attain a permutation of [1, 2, 3 ..... n] 
 *  Expected Complexity - O(nlog(n)) using segment trees
 *  http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
 */
 
public class BuildBSTFromUnsortedArray {
    
    public static Node[] tree;
    public static int[] bst_left, bst_right, a;
    public static ArrayList<Integer>[] graph;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        
        int N = in.nextInt();
        a = new int[N+1];
        graph = new ArrayList[N+1];
        tree = new Node[4*N + 10];
        for (int i = 1; i <= N; i++) {
            a[i] = in.nextInt();
            graph[i] = new ArrayList<Integer>();
        }
        
        init(1, 1, N);
        modify(1, 1, N, 1); 
        bst_left = new int[N+1];
        bst_right = new int[N+1];
        bst_left[1] = 1;
        bst_right[1] = N;
        BSTNode<Integer> root = new BSTNode<Integer>(a[1]);
        BSTNode[] BST = new BSTNode[N+1];
        BST[1] = root;
        for(int i = 2; i <= N; i++) {
            int anc = get_node(1, a[i]);
            graph[anc].add(i);
            graph[i].add(anc);
            BST[i] = new BSTNode<Integer>(a[i]);
            if (a[anc] > a[i]) {
                bst_left[i] = bst_left[anc];
                bst_right[i] = a[anc] - 1;
                BST[anc].left = BST[i];
            } else {
                bst_left[i] = a[anc] + 1;
                bst_right[i] = bst_right[anc];
                BST[anc].right = BST[i];
            }
            if (bst_left[i] <= bst_right[i])
                modify(1, bst_left[i], bst_right[i], i);
        }
        //System.out.println(Arrays.toString(bst_left));
        //System.out.println(Arrays.toString(bst_right));
        //printGraph();
        BTreePrinter.printNode(root);
        in.close();
    }
    
    public static void init(int idx, int l, int r) {
        tree[idx] = new Node(l, r);
        if (l < r) {
            init(2*idx, l, (l + r) / 2);
            init(2*idx + 1, (l + r) / 2 + 1, r);
        }
    }

    public static void modify(int idx, int l, int r, int j) {
        if (tree[idx].left == l && tree[idx].right == r) {
            tree[idx].tag = j;
            return;
        }
        if(l <= Math.min(r, tree[2*idx].right))
            modify(2*idx, l, Math.min(r, tree[2*idx].right), j);
        if(Math.max(l, tree[2*idx + 1].left) <= r)
            modify(2*idx + 1, Math.max(l, tree[2*idx + 1].left), r, j);
    }

    public static int get_node(int idx, int j) {
        if (tree[idx].left == tree[idx].right)
            return tree[idx].tag;
        if (j <= tree[2*idx].right)
            return Math.max(tree[idx].tag, get_node(2*idx, j));
        return Math.max(tree[idx].tag, get_node(2*idx + 1, j));
    }
    
    public static void printGraph() {
        for (int i = 1; i < graph.length; i++) {
            System.out.print(a[i] + " --> ");
            for(Integer j: graph[i])
                System.out.print(a[j] + " ");
            System.out.println();
        }
        
    }
    
    public static class Node implements Comparable<Node> {
        int left, right, tag;

        public Node(int l, int r) {
            this.left = l;
            this.right = r;
            this.tag = 0;
        }

        public void print() {
            System.out.println("(" + left + " - " + right + ") " + tag);
        }

        public int compareTo(Node that) {
            return Integer.compare(this.tag, that.tag);
        }
    }
    
    public static class BSTNode<T extends Comparable<?>> {
        BSTNode<T> left, right;
        T data;
     
        public BSTNode(T data) {
            this.data = data;
        } 
    } 
     
    public static class BTreePrinter { 
     
        public static <T extends Comparable<?>> void printNode(BSTNode<T> root) {
            int maxLevel = BTreePrinter.maxLevel(root);
     
            printNodeInternal(Collections.singletonList(root), 1, maxLevel);
        } 
     
        private static <T extends Comparable<?>> void printNodeInternal(List<BSTNode<T>> nodes, int level, int maxLevel) {
            if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
                return; 
     
            int floor = maxLevel - level;
            int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
            int firstSpaces = (int) Math.pow(2, (floor)) - 1;
            int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;
     
            BTreePrinter.printWhitespaces(firstSpaces);
     
            List<BSTNode<T>> newNodes = new ArrayList<BSTNode<T>>();
            for (BSTNode<T> node : nodes) {
                if (node != null) {
                    System.out.print(node.data);
                    newNodes.add(node.left);
                    newNodes.add(node.right);
                } else { 
                    newNodes.add(null);
                    newNodes.add(null);
                    System.out.print(" ");
                } 
     
                BTreePrinter.printWhitespaces(betweenSpaces);
            } 
            System.out.println("");
     
            for (int i = 1; i <= endgeLines; i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    BTreePrinter.printWhitespaces(firstSpaces - i);
                    if (nodes.get(j) == null) {
                        BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                        continue; 
                    } 
     
                    if (nodes.get(j).left != null)
                        System.out.print("/");
                    else 
                        BTreePrinter.printWhitespaces(1); 
     
                    BTreePrinter.printWhitespaces(i + i - 1);
     
                    if (nodes.get(j).right != null)
                        System.out.print("\\");
                    else 
                        BTreePrinter.printWhitespaces(1); 
     
                    BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
                } 
     
                System.out.println("");
            } 
     
            printNodeInternal(newNodes, level + 1, maxLevel);
        } 
     
        private static void printWhitespaces(int count) {
            for (int i = 0; i < count; i++)
                System.out.print(" ");
        } 
     
        private static <T extends Comparable<?>> int maxLevel(BSTNode<T> node) {
            if (node == null)
                return 0; 
            return Math.max(BTreePrinter.maxLevel(node.left), BTreePrinter.maxLevel(node.right)) + 1;
        } 
     
        private static <T> boolean isAllElementsNull(List<T> list) {
            for (Object object : list) {
                if (object != null)
                    return false; 
            }
            return true; 
        }
    }
}
