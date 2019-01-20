import java.util.ArrayList;

// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
* @author Cody Lakin
*/

public class HashCodeSimilarity
{
	// member fields and other member methods
	HashTable s1Table;
	HashTable s2Table;
	Tuple[] s1TuplesWithoutDuplicates;
	Tuple[] s2TuplesWithoutDuplicates;

	public HashCodeSimilarity(String s1, String s2, int sLength)
	{
		s1Table = HashUtils.stringToHashTable(s1, sLength);
		s2Table = HashUtils.stringToHashTable(s2, sLength);
		s1TuplesWithoutDuplicates = HashUtils.tuplesWithoutKeyDuplicates(s1, sLength);
		s2TuplesWithoutDuplicates = HashUtils.tuplesWithoutKeyDuplicates(s2, sLength);
	}

	public float lengthOfS1()
	{
		int sum = 0;
		
		//for each unique tuple, find out how many times it occurs in the hash table, square it, and sum
		int i;
		int length = s1TuplesWithoutDuplicates.length;
		for(i = 0; i < length; i++) {
			sum += Math.pow( s1Table.search( s1TuplesWithoutDuplicates[i].getKey() ).size() , 2 );
		}
		
		return (float) Math.sqrt(sum);
	}

	public float lengthOfS2()
	{
		int sum = 0;
		
		//for each unique tuple, find out how many times it occurs in the hash table, square it, and sum
		int i;
		int length = s2TuplesWithoutDuplicates.length;
		for(i = 0; i < length; i++) {
			sum += Math.pow( s2Table.search( s2TuplesWithoutDuplicates[i].getKey() ).size() , 2 );
		}
		
		return (float) Math.sqrt(sum);
	}

	public float similarity()
	{
		int sum = 0;
		Tuple[] intersection = HashUtils.getIntersection(s1TuplesWithoutDuplicates, s2TuplesWithoutDuplicates);
		
		for(Tuple t : intersection) {
			sum += s1Table.search(t.getKey()).size() * s2Table.search(t.getKey()).size();
		}
		
		return sum / (lengthOfS1() * lengthOfS2());
	}
}