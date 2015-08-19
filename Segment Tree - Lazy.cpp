#include<iostream>
#include<algorithm>
using namespace std;

#include<string.h>
#include<math.h> 

#define N 20
#define MAX (1+(1<<6))
#define inf 0x7fffffff

int arr[N];
int tree[MAX];
int lazy[MAX];

/**
 * Build and init tree
 */
void build_tree(int node, int a, int b) {
    if(a > b) return;
    
    if(a == b) {
            tree[node] = arr[a]; 
        return;
    }
    
    build_tree(node*2, a, (a+b)/2); 
    build_tree(node*2+1, 1+(a+b)/2, b);
    
    tree[node] = max(tree[node*2], tree[node*2+1]); 
}

/**
 * Increment elements within range [i, j] with value value
 */
void update_tree(int node, int a, int b, int i, int j, int value) {
  
    if(lazy[node] != 0) { 
        tree[node] += lazy[node]; 

        if(a != b) {
            lazy[node*2] += lazy[node];
                lazy[node*2+1] += lazy[node]; 
        }

        lazy[node] = 0; 
    }
  
    if(a > b || a > j || b < i)
        return;
    
    if(a >= i && b <= j) { 
            tree[node] += value;

        if(a != b) { 
            lazy[node*2] += value;
            lazy[node*2+1] += value;
        }

            return;
    }

    update_tree(node*2, a, (a+b)/2, i, j, value); 
    update_tree(1+node*2, 1+(a+b)/2, b, i, j, value);

    tree[node] = max(tree[node*2], tree[node*2+1]);
}

/**
 * Query tree to get max element value within range [i, j]
 */
int query_tree(int node, int a, int b, int i, int j) {
    
    if(a > b || a > j || b < i) return -inf;

    if(lazy[node] != 0) { 
        tree[node] += lazy[node]; 

        if(a != b) {
            lazy[node*2] += lazy[node]; 
            lazy[node*2+1] += lazy[node]; 
        }

        lazy[node] = 0; 
    }

    if(a >= i && b <= j) 
        return tree[node];

    int q1 = query_tree(node*2, a, (a+b)/2, i, j); 
    int q2 = query_tree(1+node*2, 1+(a+b)/2, b, i, j); 

    int res = max(q1, q2); 
    
    return res;
}

int main() {
    for(int i = 0; i < N; i++) arr[i] = 1;

    build_tree(1, 0, N-1);

    memset(lazy, 0, sizeof lazy);

    update_tree(1, 0, N-1, 0, 6, 5); 
    update_tree(1, 0, N-1, 7, 10, 12);
    update_tree(1, 0, N-1, 10, N-1, 100); 

    cout << query_tree(1, 0, N-1, 0, N-1) << endl; 
}
