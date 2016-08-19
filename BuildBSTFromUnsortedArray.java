import java.util.ArrayList;
import java.util.Scanner;

/*
 *  Following code works for any permutation of [1, 2, 3 ..... n]
 *  If there are large values or negative values, use coordinate compression to attain a permutation of [1, 2, 3 ..... n] 
 *  Expected Complexity - O(nlog(n)) using segment trees
 */
 
public class BuildBSTFromUnsortedArray {
    
    public static Node[] tree;
    public static int[] bst_left, bst_right, a;
    public static ArrayList<Integer>[] graph;
    
    @SuppressWarnings("unchecked")
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
    
    public static void init(int idx, int l, int r) {
        tree[idx] = new Node(l, r);
        if (l < r) {
            init(2*idx, l, (l + r) / 2);
            init(2*idx + 1, (l + r) / 2 + 1, r);
        }
    }

    public static void modify(int idx, int l, int r, int j) {
        if (tree[idx].left == l && tree[idx].right == r)
            tree[idx].tag = j;
        else if(l <= Math.min(r, tree[2*idx].right))
            modify(2*idx, l, Math.min(r, tree[2*idx].right), j);
        else if(Math.max(l, tree[2*idx + 1].left) <= r)
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
            //out.println(next + " " + dist + " ");
        }

        public int compareTo(Node that) {
            return Integer.compare(this.tag, that.tag);
        }
    }
}
