// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
* @author Cody Lakin
*/

public class Tuple
{
	// member fields and other member methods
	private int key;
	private String value;

	public Tuple(int keyP, String valueP)
	{
		this.key = keyP;
		this.value = valueP;
	}
	
	public int getKey()
	{
		return this.key;
	}

	public String getValue()
	{
		return this.value;
	}

	public boolean equals(Tuple t)
	{
		if(this.key == t.getKey()) {	
			if(this.value.equals(t.getValue())) {
				return true;
			} else {
				return false;	
			}
		} else {
			return false;
		}
	}
}