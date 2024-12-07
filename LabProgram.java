import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LabProgram {
   public static void main(String[] args) {
      // Create a new Tree234 instance
      Tree234 tree = new Tree234();

      // Generate and insert random integers
      ThreadLocalRandom rand = ThreadLocalRandom.current();
      ArrayList<Integer> expected = new ArrayList<Integer>();
      HashSet<Integer> added = new HashSet<Integer>();
      for (int i = 0; expected.size() < 20; i++) {
         int randomInteger = rand.nextInt(100, 1000);
         if (!added.contains(randomInteger)) {
            added.add(randomInteger);
            expected.add(randomInteger);
            tree.insert(randomInteger);
         }
      }
      Collections.sort(expected);
      
      // Build the actual list of integers by iterating through the tree. Keep
      // track of the number of iterations and if more iterations occur than
      // expected, then stop.
      ArrayList<Integer> actual = new ArrayList<Integer>();
      int iterationCount = 0;
      for (Integer actualInt : tree) {
         actual.add(actualInt);
         iterationCount++;
         
         // If this iteration exceeded the expected number of iterations then
         // print a failure message
         if (iterationCount > expected.size()) {
            System.out.print("FAIL: More than the expected " + expected.size());
            System.out.println(" iterations, occurred. The iterator's " +
               "hasNext() method implementation may be incorrect.");
            return;
         }
      }
      
      // Print the pass or fail messsage
      System.out.print(expected.equals(actual) ? "PASS" : "FAIL");
      System.out.println(": Iteration through tree's keys:");
      System.out.println("  Actual:   " + actual);
      System.out.println("  Expected: " + expected);
   }
}