import java.util.*;

class NodeIndexPair {
   public Node234 node;
   public int index;
   
   public NodeIndexPair(Node234 node, int index) {
      this.node = node;
      this.index = index;
   }
}

public class Tree234Iterator implements Iterator<Integer> {
   // Your code here
   private Stack<NodeIndexPair> remaining;

   public Tree234Iterator(Node234 root) {
       // Your code here
      remaining = new Stack<NodeIndexPair>();
      
      Node234 node = root;
      while (node != null) {
         // Push a pair the node and index 0. The index of 0 means that when
         // the pair is popped off the stack, the node's left child subtree has
         // been visited and the key at index 0 is next in the sequence.
         remaining.push(new NodeIndexPair(node, 0));
         
         // Go to left child
         node = node.getChild(0);
      }
   }
   
   public boolean hasNext() {
       // Your code here (remove placeholder line below)
      return remaining.size() > 0;
   }
   
   public Integer next() {
       // Your code here (remove placeholder line below)
      NodeIndexPair topPair = remaining.pop();
      Node234 poppedNode = topPair.node;
      int keyIndex = topPair.index;
      
      // Get the key to return
      int toReturn = poppedNode.getKey(keyIndex);
      
      // Advance to the next element in the iteration
      
      // If poppedNode's key at keyIndex is not the node's last key then the
      // node must be pushed back onto the stack with an incremented key index
      if (keyIndex < poppedNode.getKeyCount() - 1) {
         remaining.push(new NodeIndexPair(poppedNode, keyIndex + 1));
      }
      
      // Traverse down subtree to the right of the key
      Node234 node = poppedNode.getChild(keyIndex + 1);
      while (node != null) {
         remaining.push(new NodeIndexPair(node, 0));
         node = node.getChild(0);
      }
      
      return toReturn;
   }
}