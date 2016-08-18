import java.util.ArrayList;
import java.util.Scanner;

/*
 *  Following code works for any permutation of [1, 2, 3 ..... n]
 *  If there are large values or negative values, use coordinate compression to attain a permutation of [1, 2, 3 ..... n] 
 *  Expected Complexity - O(nlog(n)) using segment trees
 */
 
public class BuildBSTFromUnsortedArray {
    
    public static Node[] tree;
    public static int[] bst_left, bst_right;
    public static ArrayList<Integer>[] graph;
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        
        int N = in.nextInt();
        int[] a = new int[N+1];
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
        for(int i = 2; i <= N; i++) {
            int anc = get_node(1, a[i]);
            graph[anc].add(i);
            graph[i].add(anc);
            if (a[anc] > a[i]) {
                bst_left[i] = bst_left[anc];
                bst_right[i] = a[anc] - 1;
            } else {
                bst_left[i] = a[anc] + 1;
                bst_right[i] = bst_right[anc];
            }
            if (bst_left[i] <= bst_right[i])
                modify(1, bst_left[i], bst_right[i], i);
        }
        //System.out.println(Arrays.toString(bst_left));
        //System.out.println(Arrays.toString(bst_right));
        printGraph();
        in.close();
    }
    
    public static void init(int pos, int l, int r) {
        tree[pos] = new Node(l, r);
        if (l < r) {
            init(pos + pos, l, (l + r) / 2);
            init(pos + pos + 1, (l + r) / 2 + 1, r);
        }
    }

    public static void modify(int pos, int l, int r, int j) {
        if (tree[pos].left == l && tree[pos].right == r)
            tree[pos].tag = j;
        else if(l <= Math.min(r, tree[pos + pos].right))
            modify(pos + pos, l, Math.min(r, tree[pos + pos].right), j);
        else if(Math.max(l, tree[pos + pos + 1].left) <= r)
            modify(pos + pos + 1, Math.max(l, tree[pos + pos + 1].left), r, j);
    }

    public static int get_node(int pos, int j) {
        if (tree[pos].left == tree[pos].right)
            return tree[pos].tag;
        if (j <= tree[pos + pos].right)
            return Math.max(tree[pos].tag, get_node(pos + pos, j));
        return Math.max(tree[pos].tag, get_node(pos + pos + 1, j));
    }
    
    public static void printGraph() {
        for (int i = 1; i < graph.length; i++) {
            System.out.print(i + " --> ");
            for(Integer j: graph[i])
                System.out.print(j + " ");
            System.out.println();
        }
        
    }
    
    public static class Node implements Comparable<Node> {
        int left, right, tag;

        public Node(int l, int r) {
            this.left = l;
            this.right = r;
        }

        public void print() {
            //out.println(next + " " + dist + " ");
        }

        public int compareTo(Node that) {
            return Integer.compare(this.tag, that.tag);
        }
    }
}
