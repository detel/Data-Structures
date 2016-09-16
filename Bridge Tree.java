import java.util.*;

public class BridgeTree {
    
    public static int n, time;
    public static boolean[] visited;
    public static int[] low, belongsTo;
    public static HashSet<Integer>[] bridges;
    public static ArrayList<Edge> BRIDGES;
    public static ArrayList<Integer>[] graph, biconnectedComponents, bridgeTree;
    
    @SuppressWarnings("unchecked")
    public static void solve() {
        
        /*
        * int N = in.nextInt(),
        *     M = in.nextInt(),
        *     Q = in.nextInt();
        * n = N;
        */
        
        visited = new boolean[n];
        graph = new ArrayList[n];
        biconnectedComponents = new ArrayList[n];
        bridges = new HashSet[n];
        for(int i = 0; i < n; i++) {
            graph[i] = new ArrayList<Integer>();
            bridges[i] = new HashSet<Integer>();
            biconnectedComponents[i] = new ArrayList<Integer>();
        }
        
        /*
        * for (int i = 0; i < M; i++) {
        *     int u = in.nextInt() - 1,
        *         v = in.nextInt() - 1;
        *     graph[u].add(v);
        *     graph[v].add(u);
        * }
        */
        
        time = 0;
        low = new int[n];
        Arrays.fill(visited, false);
        BRIDGES = new ArrayList<Edge>();
        for(int i = 0; i < n; i++)
            if(!visited[i])
                findBridges(i, -1);
        
        for(int u = 0; u < n; u++) {
            for(Integer v : graph[u]) {
                if(bridges[u].contains(v))
                    continue;
                biconnectedComponents[u].add(v);
            }
        }
        
        int cmpno = 0;
        Arrays.fill(visited, false);
        belongsTo = new int[n];
        for(int i = 0; i < n; i++)
            if(!visited[i])
                setComponents(i, cmpno++);
        
        bridgeTree = new ArrayList[cmpno];
        for(int i = 0; i < cmpno; i++)
            bridgeTree[i] = new ArrayList<Integer>();
        for (int i = 0; i < BRIDGES.size(); i++) {
            int u = BRIDGES.get(i).u,
                v = BRIDGES.get(i).v;
            bridgeTree[belongsTo[u]].add(belongsTo[v]);
            bridgeTree[belongsTo[v]].add(belongsTo[u]);
        }
        
        
    }
    
    public static void findBridges(int curr, int parent) {
        visited[curr] = true;
        low[curr] = ++time;
        int intime = low[curr];
        for(Integer next : graph[curr]) {
            if(next == parent)
                continue;
            if(!visited[next])
                findBridges(next, curr);
            low[curr] = Math.min(low[curr], low[next]);
            // if intime(curr_node) < low[next], then next_node can only be reached from curr_node ==> it is bridge
            if(intime < low[next]) {
                bridges[curr].add(next);
                bridges[next].add(curr);
            }
        }
    }
    
    public static void setComponents(int curr, int currcmp) {
        visited[curr] = true;
        belongsTo[curr] = currcmp;
        for(Integer next : biconnectedComponents[curr])
            if(!visited[next])
                setComponents(next, currcmp);
    }
    
    static class Edge implements Comparable<Edge> {
        int u, v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }

        public void print() {
            System.out.println(u + " " + v + " ");
        }

        public int compareTo(Edge that) {
            return Integer.compare(this.v, that.v);
        }
    }

    
}
