/*
 *  Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
 *  https://sites.google.com/site/indy256/algo/link-cut-tree-connectivity
 */

public class LinkCutTree {
    
    public static void main(String[] args) {

    }
    
    public static void link(Node x, Node y) {
        if (connected(x, y))
            throw new RuntimeException("error: x and y are already connected");
        makeRoot(x);
        x.parent = y;
     }
    
    public static void cut(Node x, Node y) {
        makeRoot(x);
        expose(y);
        if (y.right != x || x.left != null || x.right != null)
            throw new RuntimeException("error: no edge (x,y)");
        y.right.parent = null;
        y.right = null;
    }
    
    static void connect(Node ch, Node p, Boolean isLeftChild) {
        if (ch != null)
            ch.parent = p;
        if (isLeftChild != null) {
            if (isLeftChild)
                p.left = ch;
            else
                p.right = ch;
       }
    }
    
    static void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;
        boolean isRootP = p.isRoot();
        boolean leftChildX = (x == p.left);
        // create 3 edges: (x.r(l),p), (p,x), (x,g)
        connect(leftChildX ? x.right : x.left, p, leftChildX);
        connect(p, x, !leftChildX);
        connect(x, g, !isRootP ? p == g.left : null);
    }
    
    static void splay(Node x) {
        while (!x.isRoot()) {
            Node p = x.parent;
            Node g = p.parent;
            if (!p.isRoot())
                g.push();
            p.push();
            x.push();
            if (!p.isRoot())
                rotate((x == p.left) == (p == g.left) ? p/*zig-zig*/ : x/*zig-zag*/);
            rotate(x);
        }
      x.push();
    }
    
    // makes node x the root of the virtual tree, and also x becomes the leftmost node in its splay tree
    static Node expose(Node x) {
        Node last = null;
        for (Node y = x; y != null; y = y.parent) {
            splay(y);
            y.left = last;
            last = y;
        }
        splay(x);
        return last;
    }
    
    public static void makeRoot(Node x) {
        expose(x);
        x.revert = !x.revert;
    }
    
    public static boolean connected(Node x, Node y) {
        if (x == y)
            return true;
        expose(x);
        // now x.parent is null
        expose(y);
        return x.parent != null;
    }
    
    public static class Node {
        Node left;
        Node right;
        Node parent;
        boolean revert;
        
        // tests whether x is a root of a splay tree
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
        }
        
        void push() {
            if (revert) {
                revert = false;
                Node t = left;
                left = right;
                right = t;
                if (left != null)   left.revert = !left.revert;
                if (right != null)  right.revert = !right.revert;
            }
        }
    }  
}
