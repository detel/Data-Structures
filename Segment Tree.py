""" http://coutcode.com/blog/segment-trees-prt1/ by Al.Cash and Muhamed Keta"""

def build():
	for i in xrange(n-1,0,-1):
		tree[i] = max(tree[i<<1],tree[i<<1|1])

def modify(position,value):
	position += n
	tree[position] = value
	while position > 1:
		tree[position>>1] = max(tree[position],tree[position^1])
		position >>= 1

def query(l,r):
	ans,l,r = 0,l+n,r+n
	while l < r:
		if l&1:
			ans = max(ans,tree[l])
			l += 1
		if r&1:
			ans = max(ans,tree[r-1])
			r -= 1
		l, r = l>>1, r>>1
	return ans

N,M = map(int,raw_input().split())
tree = [0]*(2*N)
tree[N:] = map(int,raw_input().split())
build()
for i in range(M):
	choice,a,b = map(int,raw_input().split())
	if choice:  modify(a-1,b)
	else:       print query(a-1,b)
