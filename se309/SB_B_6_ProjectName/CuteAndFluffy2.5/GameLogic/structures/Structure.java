package structures;

import map.Cell;

public abstract class Structure
{
	private Cell position;
	
	public void setPosition(Cell givenPosition)
	{
		this.position = givenPosition;
	}
	
	public abstract Structure copy();
	
	public Cell getPosition()
	{
		return position;
	}
	
	public String getCoordinates()
	{
		return position.getCoordinates();
	}
}