def build_tree(node,begin,end):
	
	if begin > end:
		return

	if begin == end:
		tree[node] = arr[begin]
		return

	mid = (begin+end)>>1
	build_tree(node<<1, begin, mid)
	build_tree(node<<1|1, mid+1, end)

	tree[node] = max(tree[node<<1],tree[node<<1|1])

# increments elements within range[i,j] with 'value' = value
def update_tree(node, begin, end, i, j, value):

	if lazy[node]:
		tree[node] += lazy[node]

		if begin != end:
			lazy[node<<1] += lazy[node]
			lazy[node<<1|1] += lazy[node]

		lazy[node] = 0

	if begin > end or begin > j or end < i:
		return

	if begin >= i and end <= j:
		tree[node] += value

		if begin != end:
			lazy[node<<1] += value
			lazy[node<<1|1] += value

		return

	mid = (begin+end)>>1
	update_tree(node<<1, begin, mid, i, j, value)
	update_tree(node<<1|1, mid+1, end, i, j, value)

	tree[node] = max(tree[node<<1], tree[node<<1|1])

# get max within range[i,j]
def query_tree(node, begin, end, i, j):

	if begin > end or begin > j or end < i:
		return -float('inf')

	if lazy[node]:
		tree[node] += lazy[node]

		if begin != end:
			lazy[node<<1] += lazy[node]
			lazy[node<<1|1] += lazy[node]

		lazy[node] = 0

	if begin >= i and end <= j:
		return tree[node]

	mid = (begin+end)>>1
	q1 = query_tree(node<<1, begin, mid, i, j)
	q2 = query_tree(node<<1|1, mid+1, end, i, j)
	
	return max(q1, q2)

N = input()
arr = range(N)

tree = [0]*(1000)
lazy = [0]*(1000)
build_tree(1, 0, N-1)

update_tree(1, 0, N-1, 0, 6, 5)
update_tree(1, 0, N-1, 7, 10, 12)
update_tree(1, 0, N-1, 10, N-1, 100)

print query_tree(1, 0, N-1, 0, N-1)
