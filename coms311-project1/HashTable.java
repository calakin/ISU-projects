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

public class HashTable
{
	// member fields and other member methods
	private HashFunction function;
	private ArrayList<Tuple>[] table;
	private int numElements;

	
	public HashTable(int size)
	{
		this.function = new HashFunction(size);
		this.table = new ArrayList[findPrime(size)];
		this.numElements = 0;
	}

	public int maxLoad()
	{
		int i;
		int maxLoad = 0;
		
		for(i = 0; i < this.size(); i++) {
			if(table[i] == null) continue;
			if(table[i].size() > maxLoad) maxLoad = table[i].size();
		}
		
		return maxLoad;
	}

	public float averageLoad()
	{
		int i;
		float sumOfLoads = 0;
		
		for(i = 0; i < this.size(); i++) {
			if(table[i] == null) continue;

			sumOfLoads += table[i].size();
		}

		return (sumOfLoads) / this.size();
	}

	public int size()
	{
		return this.table.length;
	}

	public int numElements()
	{
		return this.numElements;
	}

	public float loadFactor()
	{
		return ((float)this.numElements() / this.size());
	}

	public void add(Tuple t)
	{
		//get the table index for t
		int index = function.hash(t.getKey());
		
		//add ArrayList if it has not been done
		if(this.table[index] == null) {
			
			this.table[index] = new ArrayList<Tuple>();
			
		}

		//if tuple is not a duplicate, increment count of distinct elements
		if(this.search(t) == 0) {

			this.numElements++;

		} 
		
		//add tuple
		this.table[index].add(t);
	
		//check load factor, rehash if necessary
		if(this.loadFactor() > 0.7) rehash();
	}

	public ArrayList<Tuple> search(int k)
	{	
		int index = this.function.hash(k);
		
		if(index < table.length && table[index] != null ) {
			return table[index];
		} else if (index < table.length){
			table[index] = new ArrayList<Tuple>();
			return table[index];
		} else {
			return new ArrayList<Tuple>();
		}
	}

	public int search(Tuple t)
	{
		int count = 0;	
		
		int index = this.function.hash(t.getKey());
		
		if(index < table.length && this.table[index] != null) {
			int collisions = this.table[index].size();
			int i;

			for(i = 0; i < collisions; i++) {
				if(t.equals(this.table[index].get(i))) {
					count++;
				}
			}
		}
		
		return count;
	}

	public void remove(Tuple t)
	{
		if(search(t) == 0) return;
		if(search(t) == 1) numElements--;
		
		int bucketIndex = function.hash(t.getKey());
		int listIndex = findTuple(table[bucketIndex], t);
		
		table[bucketIndex].remove(listIndex);
	}
	
	private void rehash() {
		int i;
		int newSize = findPrime(table.length * 2);
		this.function = new HashFunction(newSize);
		
		ArrayList<Tuple>[] newTable = new ArrayList[newSize];
		
		for(i = 0; i < this.size() ; i++) {
			//add each tuple from the previous table
			if(table[i] == null) continue;
			
			int index = this.table[i].get(0).getKey();
			int newIndex = this.function.hash(index);
			
			newTable[newIndex] = new ArrayList<Tuple>();
			
			for(int j = 0; j < table[i].size(); j++) {
				newTable[newIndex].add(new Tuple(table[i].get(j).getKey(), table[i].get(j).getValue()));
			}
		}
		
		this.table = newTable;
	}
	
	private int findTuple(ArrayList<Tuple> tuples, Tuple t) {

		int collisions = tuples.size();
		int i;
		for( i = 0; i < collisions; i++) {
			if(tuples.get(i).equals(t)) {
				return i;
			}
		}
			
		return -1;
	}
	
	//This function was provided as part of HashFunction.java
	private int findPrime(int n) {
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
	private boolean isPrime(int n) {
		for(int i= 2; i<=Math.sqrt(n); i++)
			if (n%i==0)
				return false;
		return true;
	}
}