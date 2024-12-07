// Node234 class - represents a node in a 2-3-4 tree
class Node234 {
   protected int A;
   protected int B;
   protected int C;
   protected int keyCount;
   protected Node234 left;
   protected Node234 middle1;
   protected Node234 middle2;
   protected Node234 right;

   public Node234(int keyA) {
      this(keyA, null, null);
   }
   
   public Node234(int keyA, Node234 leftChild, Node234 middle1Child) {
      A = keyA;
      B = 0;
      C = 0;
      keyCount = 1;
      left = leftChild;
      middle1 = middle1Child;
      middle2 = null;
      right = null;
   }

   // Appends 1 key and 1 child to this node.
   // Preconditions:
   // 1. This node has 1 or 2 keys
   // 2. key > all keys in this node
   // 3. Child subtree contains only keys > key
   public void appendKeyAndChild(int key, Node234 child) {
      if (keyCount == 1) {
         B = key;
         middle2 = child;
      }
      else {
         C = key;
         right = child;
      }
      keyCount++;
   }
    
   // Returns the left, middle1, middle2, or right child if the childIndex
   // argument is 0, 1, 2, or 3, respectively.
   // Returns null if the childIndex argument is < 0 or > 3.
   public Node234 getChild(int childIndex) {
      if (childIndex == 0) {
         return left;
      }
      else if (childIndex == 1) {
         return middle1;
      }
      else if (childIndex == 2) {
         return middle2;
      }
      else if (childIndex == 3) {
         return right;
      }
      return null;
   }

   // Returns 0, 1, 2, or 3 if the child argument is this node's left,
   // middle1, middle2, or right child, respectively.
   // Returns -1 if the child argument is not a child of this node.
   public int getChildIndex(Node234 child) {
      if (child == left) {
         return 0;
      }
      else if (child == middle1) {
         return 1;
      }
      else if (child == middle2) {
         return 2;
      }
      else if (child == right) {
         return 3;
      }
      return -1;
   }

   // Returns this node's A, B, or C key, if the keyIndex argument is
   // 0, 1, or 2, respectively.
   // Returns 0 if the keyIndex argument is < 0 or > 2.
   public int getKey(int keyIndex) {
      if (keyIndex == 0) {
         return A;
      }
      else if (keyIndex == 1) {
         return B;
      }
      else if (keyIndex == 2) {
         return C;
      }
      return 0;
   }
   
   // Returns this node's key count.
   public int getKeyCount() {
      return keyCount;
   }

   // Returns 0, 1, or 2, if the key argument is this node's A, B, or
   // C key, respectively.
   // Returns -1 if the key is not in this node.
   public int getKeyIndex(int key) {
      if (key == A) {
         return 0;
      }
      else if (keyCount > 1 && key == B) {
         return 1;
      }
      else if (keyCount > 2 && key == C) {
         return 2;
      }
      return -1;
   }
   
   // Returns true if this node has the specified key, false otherwise.
   public boolean hasKey(int key) {
      return (key == A || (keyCount > 1 && key == B) || (keyCount > 2 && key == C));
   }

   // Inserts a new key into the proper location in this node.
   // Precondition: This node is a leaf and has 2 or fewer keys
   public void insertKey(int key) {
      if (key < A) {
         C = B;
         B = A;
         A = key;
      }
      else if (keyCount == 1 || key < B) {
         C = B;
         B = key;
      }
      else {
         C = key;
      }
      keyCount++;
   }
   
   // Inserts a new key into the proper location in this node, and
   // sets the children on either side of the inserted key.
   // Precondition: This node has 2 or fewer keys
   public void insertKeyWithChildren(int key, Node234 leftChild,
      Node234 rightChild) {
      if (key < A) {
         C = B;
         B = A;
         A = key;
         right = middle2;
         middle2 = middle1;
         middle1 = rightChild;
         left = leftChild;
      }
      else if (keyCount == 1 || key < B) {
         C = B;
         B = key;
         right = middle2;
         middle2 = rightChild;
         middle1 = leftChild;
      }
      else {
         C = key;
         right = rightChild;
         middle2 = leftChild;
      }
      keyCount++;
   }

   // Returns true if this node is a leaf, false otherwise.
   public boolean isLeaf() {
      return left == null;
   }

   // Returns the child of this node that would be visited next in the
   // traversal to search for the specified key.
   public Node234 nextNode(int key) {
      if (key < A) {
         return left;
      }
      else if (keyCount == 1 || key < B) {
         return middle1;
      }
      else if (keyCount == 2 || key < C) {
         return middle2;
      }
      return right;
   }

   // Removes key A, B, or C from this node, if keyIndex is 0, 1, or 2,
   // respectively. Other keys and children are shifted as necessary.
   public void removeKey(int keyIndex) {
      if (keyIndex == 0) {
         A = B;
         B = C;
         left = middle1;
         middle1 = middle2;
         middle2 = right;
         right = null;
         keyCount--;
      }
      else if (keyIndex == 1) {
         B = C;
         middle2 = right;
         right = null;
         keyCount--;
      }
      else if (keyIndex == 2) {
         right = null;
         keyCount--;
      }
   }

   // Removes and returns the rightmost child. Several cases exist:
   // 1. If this node's right child is not null, then right is assigned with
   //    null and the previous right child is returned.
   // 2. Otherwise, if this node's middle2 child is not null, then middle2 is
   //    assigned with null and the previous middle2 child is returned.
   // 3. Otherwise no action is taken, and null is returned.
   // No keys are changed in any case, nor the keyCount.
   public Node234 removeRightmostChild() {
      Node234 removed = null;
      if (right != null) {
         removed = right;
         right = null;
      }
      else if (middle2 != null) {
         removed = middle2;
         middle2 = null;
      }
      return removed;
   }

   // Removes and returns the rightmost key. Three possible cases exist:
   // 1. If this node has 3 keys, keyCount is decremented C is returned.
   // 2. If this node has 2 keys, keyCount is decremented B is returned.
   // 3. Otherwise no action is taken and 0 is returned.
   // No children are changed in any case.
   public int removeRightmostKey() {
      int removed = 0;
      if (keyCount == 3) {
         removed = C;
         keyCount--;
      }
      else if (keyCount == 2) {
         removed = B;
         keyCount--;
      }
      return removed;
   }

   // Sets the left, middle1, middle2, or right child if the childIndex
   // argument is 0, 1, 2, or 3, respectively.
   // Does nothing if the childIndex argument is < 0 or > 3.
   public void setChild(Node234 child, int childIndex) {
      if (childIndex == 0) {
         left = child;
      }
      else if (childIndex == 1) {
         middle1 = child;
      }
      else if (childIndex == 2) {
         middle2 = child;
      }
      else if (childIndex == 3) {
         right = child;
      }
   }
   
   // Sets all four of this node's child references.
   public void setChildren(Node234 newLeft, Node234 newMiddle1,
      Node234 newMiddle2, Node234 newRight) {
      left = newLeft;
      middle1 = newMiddle1;
      middle2 = newMiddle2;
      right = newRight;
   }

   // Sets this node's A, B, or C key if the keyIndex argument is 0, 1, or
   // 2, respectively.
   // Does nothing if the keyIndex argument is < 0 or > 2.
   public void setKey(int key, int keyIndex) {
      if (keyIndex == 0) {
         A = key;
      }
      else if (keyIndex == 1) {
         B = key;
      }
      else if (keyIndex == 2) {
         C = key;
      }
   }
   
   // Sets this node's keyCount variable to newKeyCount, provided newKeyCount
   // is in the range [1,3]. On sucess, true is returned. On failure, no change
   // occurs and false is returned.
   public boolean setKeyCount(int newKeyCount) {
      if (newKeyCount <= 0 || newKeyCount > 3) {
         return false;
      }
      
      keyCount = newKeyCount;
      return true;
   }
   
   public String toString() {
      String result = "[";
      
      // At least 1 key always exists
      result += A;
      
      if (keyCount >= 2) {
         result += " ";
         result += B;
      }
      if (keyCount >= 3) {
         result += " ";
         result += C;
      }
      
      // Add the closing part and return the string
      result += "]";
      return result;
   }
}