class _Node:
        """Lightweight, nonpublic class for storing a singly linked node."""
        __slots__ = '_element','_next'         # streamline memory usage

        def __init__(self,element,next):
                self._element = element
                self._next = next

def reverse(head):                             # iterative
        current,prev = head,None
        while current:
                next = current._next
                current._next = prev
                prev = current
                current = next
        head = prev

def reverse(head,p):                          # recursion
        if p._next == None:
                head = p
                return
        reverse(head,p._next)
        q = p._next
        q._next = p
        p._next = None
