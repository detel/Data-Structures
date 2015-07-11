from math import ceil,log

INF = float("inf")

def __RMQ(tree,tree_begin,tree_end,query_begin,query_end,index):

    if query_begin <= tree_begin and query_end >= tree_end:
        return tree[index]
 
    if tree_end < query_begin or tree_begin > query_end:
        return INF
 
    mid = tree_begin + (tree_end-tree_begin)/2

    return min(__RMQ(tree,tree_begin,mid,query_begin,query_end,2*index+1),__RMQ(tree,mid+1,tree_end,query_begin,query_end,2*index+2))

def RMQ(tree,n,query_begin,query_end):

    if (query_begin < 0 or query_end > n-1 or query_begin > query_end):
        print "Invalid Input"
        return -1
 
    return __RMQ(tree, 0, n-1, query_begin, query_end, 0)

def construct(arr,tree_begin,tree_end,tree,tree_index):

    if tree_begin == tree_end:
        tree[tree_index] = arr[tree_begin]
        return arr[tree_begin]
    
    mid = tree_begin + (tree_end-tree_begin)/2

    tree[tree_index] =  min(construct(arr,tree_begin,mid,tree,tree_index*2+1),construct(arr, mid+1, tree_end, tree, tree_index*2+2))
    return tree[tree_index]
 
def createTree(arr):

	n = len(arr)
	x = (int(ceil(log(n,2))))
	max_size = 2*int(2**x)-1
	
	tree = [INF]*max_size
	construct(arr, 0, n-1, tree, 0)
	
	return tree

arr = [1,3,2,7,9,11]
tree = createTree(arr)
print RMQ(tree,6,1,5)
