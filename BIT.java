
public class BIT {
	
	int []tree;
	int size;
	
	BIT(int n) {
		size = n;
		tree = new int[n+1];
	}
	
	int getBitmask() {
		int count, i;
		for(i = size, count = 0; i > 0; i >>= 1, count++);
		return 1<<count;
	}
	
	int query(int i) {
		int result = 0;
		for(; i > 0; i -= (i&-i))
			result += tree[i];
		return result;
	}
	
	int query(int i, int j) {
		return query(j) - (i == 1 ? 0 : query(i - 1));
	}
	
	int readSingle(int i) {
		int result = tree[i];
		if (i > 0) {
			int lca = i - (i&-i);
			i--;
			while( lca != i) {
				result -= tree[i];
				i -= (i&-i);
			}
		}
		return result;
	}
	
	void update(int i, int value) {
		for(; i <= size; i += (i&-i))
			tree[i] += value;
	}
	
	//only non-negative frequencies
	//if in tree exists more than one index with a same cumFre, it returns the greatest one
	int find(int cumFre) {
		
		int index = 0,
			  bitMask = getBitmask();
		
		while((bitMask != 0) && (index < size)) {
			int tmp = index + bitMask;
			if (cumFre >= tree[tmp]) {
				index = tmp;
				cumFre -= tree[index];
			}
			bitMask >>= 1;
		}
		
		if (cumFre != 0)
			index = -1;
		
		return index;
	}
	
}
