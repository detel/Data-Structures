import java.io.*;
import java.util.*;

public class NobitaInTrouble {

    public static int n, m;
    public static ArrayList<Integer>[] g, idx;
    public static ArrayList<Long> num[];
    public static int chainNo, ptr;
    public static int[] chainHead, chainSize, chainIdx, posInBase, subtree;
    public static long[] baseArray, segtree;
    public static int[] depth, parent;
    public static int[][] P;
    public static int rev[];
    public static int LOGMAXN;
    public static int[] A;
    
    public static void main(String args[]) {
        new Thread(null, new Runnable() {
            public void run() {
                try{
                    solve();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, "1", 1 << 26).start();
    }
    
    @SuppressWarnings("unchecked")
    public static void solve() {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        n = in.nextInt();
        //m = in.nextInt();

        g = new ArrayList[n];
        for (int i = 0; i < n; i++)
            g[i] = new ArrayList<Integer>();

        idx = new ArrayList[n];
        for (int i = 0; i < n; i++)
            idx[i] = new ArrayList<Integer>();

        num = new ArrayList[n];
        for (int i = 0; i < n; i++)
            num[i] = new ArrayList<Long>();

        for (int i = 1; i < n; i++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            //long c = in.nextLong();
            g[u].add(v);
            idx[u].add(i);
            num[u].add(0L);
            g[v].add(u);
            idx[v].add(i);
            num[v].add(0L);
        }
        
        A = new int[n];
        for (int i = 0; i < n; i++)
            A[i] = in.nextInt();

        depth = new int[n];
        parent = new int[n];
        subtree = new int[n];
        rev = new int[n];
        dfs(0, -1);

        chainNo = ptr = 0;
        chainHead = new int[n];
        Arrays.fill(chainHead, -1);
        chainSize = new int[n];
        chainIdx = new int[n];
        posInBase = new int[n];
        baseArray = new long[n];
        HLD(0, -1, 1);

        LOGMAXN = (int)(Math.log(n)/Math.log(2) + 3);
        P = new int[LOGMAXN][n];
        preprocessForLCA();

        segtree = new long[4 * n];
        build(0, n - 1, 0);

        int Q = in.nextInt();
        while(Q-- > 0) {
            int a = in.nextInt() - 1,
                  b = in.nextInt() - 1;
            out.println(query(a, b));
        }
        out.close();
    }


    public static long query(int a, int b) {
        int lca = getLCA(a, b);
        return merge(queryUp(a, lca), queryUp(b, lca)) - A[lca];
    }

    public static long queryUp(int a, int lca) {
        if (a == lca)
            return A[lca];
        if (chainIdx[a] == chainIdx[lca])
            return query(0, n - 1, 0, posInBase[lca], posInBase[a]);
        long curr = query(0, n - 1, 0, posInBase[chainHead[chainIdx[a]]], posInBase[a]);
        return merge(curr, queryUp(parent[chainHead[chainIdx[a]]], lca));
    }

    public static long query(int s, int e, int c, int l, int r) {
        if (s == l && e == r)
            return segtree[c];
        int m = (s + e) >> 1;
        if (r <= m)
            return query(s, m, 2 * c + 1, l, r);
        if (l > m)
            return query(m + 1, e, 2 * c + 2, l, r);
        return merge(query(s, m, 2 * c + 1, l, m), query(m + 1, e, 2 * c + 2, m + 1, r));
    }

    public static void update(int x, long y) {
        update(0, n - 1, 0, posInBase[x], y);
    }

    public static void update(int s, int e, int c, int x, long y) {
        if (s == e)
            segtree[c] = y;
        else {
            int m = (s + e) >> 1;
            if (x <= m)
                update(s, m, 2 * c + 1, x, y);
            else
                update(m + 1, e, 2 * c + 2, x, y);
            segtree[c] = merge(segtree[2 * c + 1], segtree[2 * c + 2]);
        }
    }

    public static void dfs(int curr, int prev) {
        int s = g[curr].size();
        subtree[curr] = 1;
        for (int i = 0; i < s; i++) {
            int nxt = g[curr].get(i);
            int edge = idx[curr].get(i);
            if (nxt != prev) {
                depth[nxt] = depth[curr] + 1;
                parent[nxt] = curr;
                rev[edge] = nxt;
                dfs(nxt, curr);
                subtree[curr] += subtree[nxt];
            }
        }
    }

    public static void HLD(int curr, int prev, long cost) {
        if (chainHead[chainNo] == -1)
            chainHead[chainNo] = curr;
        chainIdx[curr] = chainNo;
        posInBase[curr] = ptr;
        baseArray[ptr++] = cost;
        chainSize[curr]++;

        int maxIndex = -1;
        long maxCost = -1;

        for (int i = 0; i < g[curr].size(); i++) {
            int next = g[curr].get(i);
            if (next != prev) {
                if (maxIndex == -1 || subtree[next] > subtree[maxIndex]) {
                    maxIndex = next;
                    maxCost = num[curr].get(i);
                }
            }
        }

        if (maxIndex != -1)
            HLD(maxIndex, curr, maxCost);

        for (int i = 0; i < g[curr].size(); i++) {
            if (g[curr].get(i) != prev && maxIndex != g[curr].get(i)) {
                chainNo++;
                HLD(g[curr].get(i), curr, num[curr].get(i));
            }
        }

    }

    public static void build(int s, int e, int c) {
        if (s == e) {
            segtree[c] = A[s];//baseArray[s];
            return;
        }
        int m = (s + e) >> 1;
        build(s, m, 2 * c + 1);
        build(m + 1, e, 2 * c + 2);
        segtree[c] = merge(segtree[2 * c + 1], segtree[2 * c + 2]);
    }

    public static long merge(long x, long y) {
         // merge operation for segment tree
        return x +  y;
    }

    public static void preprocessForLCA() {
        for (int j = 0; 1 << j < n; j++)
            for (int i = 0; i < n; i++)
                P[j][i] = j == 0 ? parent[i] : -1;

        for (int j = 1; (1 << j) < n; ++j)
            for (int i = 0; i < n; ++i)
                if (P[j - 1][i] != -1)
                    P[j][i] = P[j - 1][P[j - 1][i]];
    }

    public static int getLCA(int p, int q) {
        int tmp, log, i;
        if (depth[p] < depth[q]) {
            tmp = p;
            p = q;
            q = tmp;
        }

        for (log = 1; 1 << log <= depth[p]; log++);
        log--;

        for (i = log; i >= 0; i--)
            if (depth[p] - (1 << i) >= depth[q])
                p = P[i][p];

        if (p == q)
            return p;

        for (i = log; i >= 0; i--)
            if (P[i][p] != -1 && P[i][p] != P[i][q]) {
                p = P[i][p];
                q = P[i][q];
            }

        return parent[p];
    }

    static class InputReader {

        private InputStream stream;
        private byte[] buf = new byte[8192];
        private int curChar;
        private int snumChars;
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
