import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;

/**
 * Doublets.java
 * Provides an implementation of the WordLadderGame interface. The lexicon
 * is stored as a TreeSet of Strings.
 *
 * @author Carmen Stowe (cds0048@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 2016-04-07
 */
public class Doublets implements WordLadderGame {

   ////////////////////////////////////////////
   // DON'T CHANGE THE FOLLOWING TWO FIELDS. //
   ////////////////////////////////////////////

   // A word ladder with no words. Used as the return value for the ladder methods
   // below when no ladder exists.
   List<String> EMPTY_LADDER = new ArrayList<>();

   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   TreeSet<String> lexicon;


   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            ////////////////////////////////////////////////
            // Add code here to store str in the lexicon. //
            ////////////////////////////////////////////////
            lexicon.add(str.toLowerCase());
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }

   ///////////////////////////////////////////////////////////////////////////////
   // Fill in implementations of all the WordLadderGame interface methods here. //
   ///////////////////////////////////////////////////////////////////////////////
   
   /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param  str1 the first string
    * @param  str2 the second string
    * @return      the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */
   public int getHammingDistance(String str1, String str2) {
      if (str1.length() != str2.length()) {
         return -1;
      }
      char[] arr1 = str1.toCharArray();
      char[] arr2 = str2.toCharArray();
      
      int d = 0;
      
      for(int i = 0; i < arr1.length; i++) {
         if (arr1[i] != arr2[i]) {
            d++;
         }
      }
      return d;
   }


  /**
   * Returns a word ladder from start to end. If multiple word ladders exist,
   * no guarantee is made regarding which one is returned. If no word ladder exists,
   * this method returns an empty list.
   *
   * Depth-first search with backtracking must be used in all implementing classes.
   *
   * @param  start  the starting word
   * @param  end    the ending word
   * @return        a word ladder from start to end
   */
   public List<String> getLadder(String start, String end) {
      Deque<String> s = new ArrayDeque<>();
      TreeSet<String> visited = new TreeSet<String>();
      s.addFirst(start);
      visited.add(start);
      Map<String, String> prev = new TreeMap<String, String>();
      
      if(start.equals(end)) {
         return copy(prev, end);
      }
      while ((!visited.contains(end)) && (!s.isEmpty())) {
         String string = s.removeFirst();
         for(String neighbor : getNeighbors(string)) {
            if(!visited.contains(neighbor)) {
               visited.add(neighbor);
               s.addFirst(neighbor);
               prev.put(neighbor, string);
               if (neighbor.equals(end)) {
                  visited.add(string);
                  return copy(prev, end);
               }
            }
         }
      }
      return EMPTY_LADDER;
   }


  /**
   * Returns a minimum-length word ladder from start to end. If multiple
   * minimum-length word ladders exist, no guarantee is made regarding which
   * one is returned. If no word ladder exists, this method returns an empty
   * list.
   *
   * Breadth-first search must be used in all implementing classes.
   *
   * @param  start  the starting word
   * @param  end    the ending word
   * @return        a minimum length word ladder from start to end
   */
   public List<String> getMinLadder(String start, String end) {
      Deque<String> q = new ArrayDeque<>();
      TreeSet<String> visited = new TreeSet<String>();
      q.addLast(start);
      visited.add(start);
      Map<String, String> prev = new TreeMap<String, String>();
      
      if(start.equals(end)) {
         return copy(prev, end);
      }
   
      while ((!visited.contains(end)) && (!q.isEmpty())) {
         String string = q.removeFirst();
         for(String neighbor : getNeighbors(string)) {
            if(!visited.contains(neighbor)) {
               visited.add(neighbor);
               q.addLast(neighbor);
               prev.put(neighbor, string);
               if (neighbor.equals(end)) {
                  visited.add(string);
                  return copy(prev, end);
               }
            }
         }
      }
      return EMPTY_LADDER;
   }
   /**
    *
    */
   private List<String> copy(Map<String, String> m, String key) {
      List<String> doub = new ArrayList<String>();
      for(; key != null; key = m.get(key)) {
         doub.add(key);
      }
      Collections.reverse(doub);
      return doub;
   }

   /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param  word the given word
    * @return      the neighbors of the given word
    */
   public List<String> getNeighbors(String word) {
      Iterator itr = lexicon.iterator();
      String s = (String)itr.next();
      List<String> list = new ArrayList<String>();
      
      while(itr.hasNext()) {
         if (getHammingDistance(s, word) == 1) {
            list.add(s);
         }
         s = (String)itr.next();
      }
      return list;
   }


   /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
   public int getWordCount() {
      return lexicon.size();
   }


   /**
    * Checks to see if the given string is a word.
    *
    * @param  str the string to check
    * @return     true if str is a word, false otherwise
    */
   public boolean isWord(String str) {
      if (lexicon.contains(str)) {
         return true;
      }
      return false;
   }


   /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param  sequence the given sequence of strings
    * @return          true if the given sequence is a valid word ladder,
    *                       false otherwise
    */
   public boolean isWordLadder(List<String> sequence) {
      // Iterator itr = sequence.iterator();
      // String s = (String)itr.next();
   //       
      // while (itr.hasNext()) {
         // if (getHammingDistance(s,st) != 1) {
            // return false;
         // }
         // s = (String)itr.next();
      // }
      // for (int i = 0; i < sequence.size() - 1; i++) {
         // for (int j = i + 1; j < sequence.size(); j++) {
            // if (getHammingDistance(sequence.get(i), sequence.get(j)) != 1) {
               // return false;
            // }
         // }
      // }
      if (sequence.size() == 0) {
         return false;
      }
      int i = 0;
      int j = i + 1;
      while ((i < sequence.size() - 1) && (j < sequence.size())) {
         if (getHammingDistance(sequence.get(i), sequence.get(j)) != 1) {
            return false;
         }
         i++;
         j++;
      }
      
      int k = 0;
      while (k < sequence.size()) {
         if (!lexicon.contains(sequence.get(k))) {
            return false;
              
         }
         k++;
      }
      return true;
   }

}
