import java.util.*;

class Tree234 implements Iterable<Integer> {
   protected Node234 root;
   
   // Initializes the tree by assigning the root node pointer with null.
   public Tree234() {
      root = null;
   }
   
   protected Node234 allocNode(int key) {
      return allocNode(key, null, null);
   }
   
   protected Node234 allocNode(int key, Node234 left, Node234 middle1) {
      return new Node234(key, left, middle1);
   }
        
   // Fuses a parent node and two children into one node. 
   // Precondition: leftNode and rightNode must have one key each.
   protected Node234 fuse(Node234 parent, Node234 leftNode, Node234 rightNode) {
      if (parent == root && parent.getKeyCount() == 1) {
         return fuseRoot();
      }

      int leftNodeIndex = parent.getChildIndex(leftNode);
      int middleKey = parent.getKey(leftNodeIndex);
      Node234 fusedNode = allocNode(leftNode.getKey(0));
      fusedNode.setKey(middleKey, 1);
      fusedNode.setKey(rightNode.getKey(0), 2);
      fusedNode.setKeyCount(3);
      fusedNode.setChildren(leftNode.getChild(0), leftNode.getChild(1),
         rightNode.getChild(0), rightNode.getChild(1));
      int keyIndex = parent.getKeyIndex(middleKey);
      parent.removeKey(keyIndex);
      parent.setChild(fusedNode, keyIndex);
      return fusedNode;
   }

   // Fuses the tree's root node with the root's two children. 
   // Precondition: Each of the three nodes must have one key each.
   protected Node234 fuseRoot() {
      Node234 oldLeft = root.getChild(0);
      Node234 oldMiddle1 = root.getChild(1);
      root.setKey(root.getKey(0), 1);
      root.setKey(oldLeft.getKey(0), 0);
      root.setKey(oldMiddle1.getKey(0), 2);
      root.setKeyCount(3);
      root.setChildren(oldLeft.getChild(0), oldLeft.getChild(1),
         oldMiddle1.getChild(0), oldMiddle1.getChild(1));
      return root;
   }
   
   // Searches for, and returns, the minimum key in a subtree
   protected int getMinKey(Node234 node) {
      Node234 current = node;
      while (current.getChild(0) != null) {
         current = current.getChild(0);
      }
      return current.getKey(0);
   }
   
   // Finds and replaces one key with another. The replacement key must
   // be known to be a key that can be used as a replacement without violating
   // any of the 2-3-4 tree rules.
   protected boolean keySwap(Node234 node, int existing, int replacement) {
      if (node == null) {
         return false;
      }

      int keyIndex = node.getKeyIndex(existing);
      if (keyIndex == -1) {
         Node234 next = node.nextNode(existing);
         return keySwap(next, existing, replacement);
      }

      node.setKey(replacement, keyIndex);
      return true;
   }
   
   // Rotates or fuses to add 1 or 2 additional keys to a node with 1 key.
   protected Node234 merge(Node234 node, Node234 nodeParent) {
      // Get pointers to node's siblings
      int nodeIndex = nodeParent.getChildIndex(node);
      Node234 leftSibling = nodeParent.getChild(nodeIndex - 1);
      Node234 rightSibling = nodeParent.getChild(nodeIndex + 1);
    
      // Check siblings for a key that can be transferred
      if (leftSibling != null && leftSibling.getKeyCount() >= 2) {
         rotateRight(leftSibling, nodeParent);
      }
      else if (rightSibling != null && rightSibling.getKeyCount() >= 2) {
         rotateLeft(rightSibling, nodeParent);
      }
      else { // fuse
         if (leftSibling == null) {
            node = fuse(nodeParent, node, rightSibling);
         }
         else {
            node = fuse(nodeParent, leftSibling, node);
         }
      }

      return node;
   }
   
   protected void rotateLeft(Node234 node, Node234 nodeParent) {
      // Get the node's left sibling
      int nodeIndex = nodeParent.getChildIndex(node);
      Node234 leftSibling = nodeParent.getChild(nodeIndex - 1);

      // Get the key from the parent that will be copied into the left sibling
      int keyForLeftSibling = nodeParent.getKey(nodeIndex - 1);

      // Append the key to the left sibling
      leftSibling.appendKeyAndChild(keyForLeftSibling, node.getChild(0));

      // Replace the parent's key that was appended to the left sibling
      nodeParent.setKey(node.getKey(0), nodeIndex - 1);

      // Remove key A and left child from node
      node.removeKey(0);
   }
    
   protected void rotateRight(Node234 node, Node234 nodeParent) {
      // Get the node's right sibling
      int nodeIndex = nodeParent.getChildIndex(node);
      Node234 rightSibling = nodeParent.getChild(nodeIndex + 1);
        
      // Get the key from the parent that will be copied into the right sibling
      int keyForRightSibling = nodeParent.getKey(nodeIndex);
        
      // Shift key and child pointers in right sibling
      rightSibling.setKey(rightSibling.getKey(1), 2);
      rightSibling.setKey(rightSibling.getKey(0), 1);
      rightSibling.setChild(rightSibling.getChild(2), 3);
      rightSibling.setChild(rightSibling.getChild(1), 2);
      rightSibling.setChild(rightSibling.getChild(0), 1);
        
      // Set key A and the left child of rightSibling
      rightSibling.setKey(keyForRightSibling, 0);
      rightSibling.setChild(node.removeRightmostChild(), 0);
      
      // rightSibling has gained a key
      rightSibling.setKeyCount(rightSibling.getKeyCount() + 1);
        
      // Replace the parent's key that was prepended to the right sibling
      nodeParent.setKey(node.removeRightmostKey(), nodeIndex);
   }
   
   public Node234 insert(int key) {
      return insert(key, null, null);
   }

   // Inserts a new key into this tree, provided the tree doesn't already
   // contain the same key.
   public Node234 insert(int key, Node234 node, Node234 nodeParent) {
      // Special case for empty tree
      if (root == null) {
         root = allocNode(key);
         return root;
      }

      // If the node argument is null, recursively call with root
      if (node == null) {
         return insert(key, root, null);
      }

      // Check for duplicate key
      if (node.hasKey(key)) {
         // Duplicate keys are not allowed
         return null;
      }

      // Preemptively split full nodes
      if (node.getKeyCount() == 3) {
         node = split(node, nodeParent);
      }

      // If node is not a leaf, recursively insert into child subtree
      if (!node.isLeaf()) {
         return insert(key, node.nextNode(key), node);
      }
        
      // key can be inserted into leaf node
      node.insertKey(key);
      return node;
   }
   
   // Returns the height of this tree.
   public int getHeight() {
      return getHeight(root);
   }
   
   protected int getHeight(Node234 node) {
      if (node.getChild(0) == null) {
         return 0;
      }
      return 1 + getHeight(node.getChild(0));
   }
   
   // Returns the number of keys in this tree.
   public int length() {
      int count = 0;
      Stack<Node234> nodes = new Stack<Node234>();
      nodes.push(root);
      
      while (nodes.size() > 0) {
         Node234 node = nodes.pop();
         if (node != null) {
            // Add the number of keys in the node to the count
            count = count + node.getKeyCount();
                
            // Push children
            for (int i = 0; i < 4; i++) {
               nodes.push(node.getChild(i));
            }
         }
      }
      return count;
   }

   // Finds and removes the specified key from this tree.
   public boolean remove(int key) {
      // Special case for tree with 1 key
      if (root.isLeaf() && root.getKeyCount() == 1) {
         if (root.getKey(0) == key) {
            root = null;
            return true;
         }
         return false;
      }

      Node234 currentParent = null;
      Node234 current = root;
      while (current != null) {
         // Merge any non-root node with 1 key
         if (current.getKeyCount() == 1 && current != root) {
            current = merge(current, currentParent);
         }

         // Check if current node contains key
         int keyIndex = current.getKeyIndex(key);
         if (keyIndex != -1) {
            if (current.isLeaf()) {
               current.removeKey(keyIndex);
               return true;
            }

            // The node contains the key and is not a leaf, so the key is
            // replaced with the successor
            Node234 tmpChild = current.getChild(keyIndex + 1);
            int tmpKey = getMinKey(tmpChild);
            remove(tmpKey);
            keySwap(root, key, tmpKey);
            return true;
         }

         // Current node does not contain key, so continue down tree
         currentParent = current;
         current = current.nextNode(key);
      }
                
      // key not found
      return false;
   }
   
   // Searches this tree for the specified key. If found, the node containing
   // the key is returned. Otherwise null is returned.
   public Node234 search(int key) {
      return searchRecursive(key, root);
   }
   
   // Recursive helper function for search.
   protected Node234 searchRecursive(int key, Node234 node) {
      if (node == null) {
         return null;
      }
            
      // Check if the node contains the key
      if (node.hasKey(key)) {
         return node;
      }
        
      // Recursively search the appropriate subtree
      return searchRecursive(key, node.nextNode(key));
   }
   
   // Splits a full node, moving the middle key up into the parent node.
   // Precondition: nodeParent has one or two keys.
   protected Node234 split(Node234 node, Node234 nodeParent) {
      Node234 splitLeft = allocNode(node.getKey(0), node.getChild(0),
         node.getChild(1));
      Node234 splitRight = allocNode(node.getKey(2), node.getChild(2),
         node.getChild(3));
      if (nodeParent != null) {
         nodeParent.insertKeyWithChildren(node.getKey(1), splitLeft, splitRight);
      }
      else {
         // Split root
         nodeParent = allocNode(node.getKey(1), splitLeft, splitRight);
         root = nodeParent;
      }
        
      return nodeParent;
   }
   
   // Added to support enhanced for loops
   public Iterator<Integer> iterator() {
      return new Tree234Iterator(root);
   }
}