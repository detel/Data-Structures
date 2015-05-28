class Bit:
    def __init__(self, n):
        size = 1
        while n > sz:
            sz *= 2
        self.size = size
        self.data = [0]*size

    def sum(self, i):
        s = 0
        while i > 0:
            s += self.data[i]
            i -= i & -i
        return s

    def add(self, i, x):
        while i < self.size:
            self.data[i] += x
            i += i & -i
