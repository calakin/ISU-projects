package terrain;

import units.Unit;

public abstract class Terrain 
{
	protected String name;
	
	public abstract void unitEffect(Unit givenUnit);
	
	public String toString()
	{
		return name;
	}	
	
	public String getName()
	{
		return this.name;
	}
}