# iterable_2_3_4_tree
e Tree234 class is extended to support iteration with an enhanced for loop. A Tree234 iterator references the tree's minimum key upon construction. The iterator can then move to the second to minimum key, then the third to minimum, and so on. Eventually the iterator moves to the last key and can move no further. Enhanced for loops work on any class that implements the Iterable interface. Tree234 stores a collection of integer keys, so the Tree234 class implements Iterable<Integer>. Implementing Iterable<Integer> requires Tree234 to implement the iterator() method, which returns an Iterator<Integer>.

Note the subtle difference in names: Iterable and Iterator.
