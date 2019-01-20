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

public class BruteForceSimilarity
{
	// member fields and other member methods
	int s1HashedValues[];
	int s2HashedValues[];
	String s1substrings[];
	String s2substrings[];
	
	public BruteForceSimilarity(String s1, String s2, int sLength)
	{	
		s1HashedValues = HashUtils.rolloverHash(s1, sLength);
		s2HashedValues = HashUtils.rolloverHash(s2, sLength);
		s1substrings = new String[s1HashedValues.length];
		s2substrings = new String[s2HashedValues.length];		

		int i, j;
		
		for( i = 0; i < s1substrings.length; i++) {
			String substring = "";
			
			for(j = 0; j < sLength; j++) {
				substring += s1.charAt(i + j);
			}
			
			s1substrings[i] = substring;
		}
		
		for( i = 0; i < s2substrings.length; i++) {
			String substring = "";
			
			for(j = 0; j < sLength; j++) {
				substring += s2.charAt(i + j);
			}
			
			s2substrings[i] = substring;
		}		
	}

	public float lengthOfS1()
	{
		int i, j;
		ArrayList<Integer> s1HashWithoutDuplicates = new ArrayList<Integer>();	
		float sum = 0;
		
		for(i = 0; i < s1HashedValues.length; i++) {
			if(!s1HashWithoutDuplicates.contains(s1HashedValues[i])) {
				s1HashWithoutDuplicates.add(s1HashedValues[i]);
			}
		}
		
		for(i = 0; i < s1HashWithoutDuplicates.size(); i++) {
			int count = 0;
			int value = s1HashWithoutDuplicates.get(i);
			
			for(j = 0; j < s1HashedValues.length; j++) {
				if(value == s1HashedValues[j]) {
					if(s1substrings[j].equals(s1substrings[i])) {
						count++;
					}
				}
			}
			
			sum += Math.pow(count, 2);
		}
		
		return (float) Math.sqrt(sum);
	}

	public float lengthOfS2()
	{
		int i, j;
		ArrayList<Integer> s2HashWithoutDuplicates = new ArrayList<Integer>();	
		float sum = 0;
		
		for(i = 0; i < s2HashedValues.length; i++) {
			if(!s2HashWithoutDuplicates.contains(s2HashedValues[i])) {
				s2HashWithoutDuplicates.add(s2HashedValues[i]);
			}
		}
		
		for(i = 0; i < s2HashWithoutDuplicates.size(); i++) {
			int count = 0;
			int value = s2HashWithoutDuplicates.get(i);
			
			for(j = 0; j < s2HashedValues.length; j++) {
				if(value == s2HashedValues[j]) {
					if(s2substrings[j].equals(s2substrings[i])) {
						count++;
					}
				}
			}
			
			sum += Math.pow(count, 2);
		}
		
		return (float) Math.sqrt(sum);
	}

	public float similarity()
	{
		float result;
		int sum = 0;
		int i, j;	
		ArrayList<Integer> intersectionS1S2 = new ArrayList<Integer>();
		ArrayList<String> strings = new ArrayList<String>();
		
		//get the intersection set of S1, S2 (values in only one will result in 0 anyway)
		for(i = 0; i < s1HashedValues.length; i++) {
			if(intersectionS1S2.contains(s1HashedValues[i])) continue;
			
			for(j = 0; j < s2HashedValues.length; j++) {
				if(s1HashedValues[i] == s2HashedValues[j]) {
					if(s1substrings[i].equals(s2substrings[j])) {
						intersectionS1S2.add(s1HashedValues[i]);
						strings.add(s1substrings[i]);
						break;
					}
				}
			}
		}
		
		//count values from intersection occurrence in each set
		for(i = 0; i < intersectionS1S2.size(); i++) {
			int countInS1 = 0;
			int countInS2 = 0;
			
			for(j = 0; j < s1HashedValues.length; j++) {
				if(s1HashedValues[j] == intersectionS1S2.get(i)) {
					if(s1substrings[j].equals(strings.get(i))) {
						countInS1++;
					}
				}
			}
				
			for(j = 0; j < s2HashedValues.length; j++) {
				if(s2HashedValues[j] == intersectionS1S2.get(i)) {
					if(s2substrings[j].equals(strings.get(i))) {
						countInS2++;
					}
				}
			}
			sum += countInS1 * countInS2;
		}

		//perform similarity calculation
		result = sum / (lengthOfS1() * lengthOfS2());
		
		return result;
	}
}