import java.util.ArrayList;

/**
 * 
 * @author Cody Lakin
 *
 */
public class HashUtils {
	
	public static int[] rolloverHash(String s, int sLength) {
		int[] set = new int[s.length() - sLength + 1];
		int i;
		int alpha = 100;
	
		//compute N1
		int n1 = 0;
		for(i = 0; i < sLength; i++) {
			n1 += s.charAt(sLength - i - 1) * Math.pow(alpha, sLength - (sLength - i - 1) - 1) ;
		}
		set[0] = n1;
		
		//compute the rest based on the previous
		for(i = 1; i < set.length; i++) {
			set[i] = (set[i-1] - (s.charAt(i-1) * (int)Math.pow(alpha, sLength - 1)) ) * alpha + 
						s.charAt(i + sLength - 1);
		}
		
		return set;
	}
	
	public static HashTable stringToHashTable(String s, int sLength) {
		String[] sSubstrings = stringsToShingles(s, sLength);
		
		HashTable table = new HashTable(sSubstrings.length);
			
		int[] sHashedValues = HashUtils.rolloverHash(s, sLength);
		
		Tuple[] tuples = shinglesToTuples(sSubstrings, sHashedValues);
		
		int i;
		for(i = 0; i < tuples.length; i++) {
			table.add(tuples[i]);
		}
		
		return table;
	}
	
	public static Tuple[] shinglesToTuples(String[] shingles, int[] hashValues) {
		Tuple[] tuples = new Tuple[shingles.length];
		
		int i;
		for(i = 0; i < shingles.length; i++) {
			tuples[i] = new Tuple(hashValues[i], shingles[i]);
		}
		
		return tuples;
	}
	
	public static String[] stringsToShingles(String s, int sLength) {
		String[] sSubstrings = new String[s.length() - sLength + 1];
		
		int i, j;
		for( i = 0; i < sSubstrings.length; i++) {
			String substring = "";
			
			for(j = 0; j < sLength; j++) {
				substring += s.charAt(i + j);
			}
			sSubstrings[i] = substring;
		}
		
		return sSubstrings;
	}
	
	public static Tuple[] tuplesWithoutDuplicates(String s, int sLength) {
		String[] sSubstrings = stringsToShingles(s, sLength);

		ArrayList<Tuple> resultList = new ArrayList<Tuple>();
		
		int mod = findPrime(sSubstrings.length);
		int[] sHashedValues = HashUtils.rolloverHash(s, sLength);
		
		Tuple[] tuplesWithDuplicates = shinglesToTuples(sSubstrings, sHashedValues);

		int i;
		for(i = 0; i < tuplesWithDuplicates.length; i++) {
			if(findTuple(resultList, tuplesWithDuplicates[i]) == -1) {
				resultList.add(tuplesWithDuplicates[i]);
			}
		}
		
		Tuple[] result = new Tuple[resultList.size()];
		result = resultList.toArray(result);
		
		return result;
	}
	
	public static Tuple[] tuplesWithoutKeyDuplicates(String s, int sLength) {
		String[] sSubstrings = stringsToShingles(s, sLength);
		
		int mod = findPrime(sSubstrings.length);
		int[] sHashedValues = HashUtils.rolloverHash(s, sLength);
		
		Tuple[] tuplesWithDuplicates = shinglesToTuples(sSubstrings, sHashedValues);
		
		ArrayList<Tuple> resultList = new ArrayList<Tuple>();
		
		int i;
		for(i = 0; i < tuplesWithDuplicates.length; i++) {
			if(findKey(resultList, tuplesWithDuplicates[i]) == -1) {
				resultList.add(tuplesWithDuplicates[i]);
			}
		}
		
		Tuple[] result = new Tuple[resultList.size()];
		result = resultList.toArray(result);
		
		return result;
	}
	
	private static int findTuple(ArrayList<Tuple> tuples, Tuple t) {
		int tupleCount = tuples.size();
		int i;
		for( i = 0; i < tupleCount; i++) {
			if(tuples.get(i).equals(t)) {
				return i;
			}
		}
			
		return -1;
	}
	
	private static int findTuple(Tuple[] tuples, Tuple t) {
		int tupleCount = tuples.length;
		int i;
		for( i = 0; i < tupleCount; i++) {
			if(tuples[i].equals(t)) {
				return i;
			}
		}
			
		return -1;
	}
	
	private static int findKey(ArrayList<Tuple> tuples, Tuple t) {
		int tupleCount = tuples.size();
		int i;
		for( i = 0; i < tupleCount; i++) {
			if(tuples.get(i).getKey() == t.getKey()) {
				return i;
			}
		}
			
		return -1;
	}

	public static Tuple[] getIntersection(Tuple[] t1, Tuple[] t2) {
		ArrayList<Tuple> union = new ArrayList<Tuple>();

		for(Tuple t : t1) {
			union.add(t);
		}
		for(Tuple t : t2) {
			union.add(t);
		}

		
		ArrayList<Tuple> tmp = new ArrayList<Tuple>();
		
		int i;
		for(i = 0; i < union.size(); i++) {
			if(findKey(tmp, union.get(i)) == -1) {
				tmp.add(union.get(i));
			}
		}
		
		ArrayList<Tuple> resultList = new ArrayList<Tuple>();

		for(i = 0; i < tmp.size(); i++) {
			if(findTuple(t1, tmp.get(i)) != -1 && findTuple(t2, tmp.get(i)) != -1) {
				resultList.add(tmp.get(i));
			}
		}
		
		Tuple[] result = new Tuple[resultList.size()];
		result = resultList.toArray(result);

		return result;
	}
	
	private static int findPrime(int n) {
		boolean found = false;
		int num = n;
		while(!found) {
			if (isPrime(num))
				return num;
			num++;
		}
		return -1;	
	}
	
	//This function was provided as part of HashFunction.java
	private static boolean isPrime(int n) {
		for(int i= 2; i<=Math.sqrt(n); i++)
			if (n%i==0)
				return false;
		return true;
	}
}
