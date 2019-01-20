package map;

import structures.Structure;
import terrain.Terrain;
import units.Unit;

public class Cell
{
	private Unit unit;
	private Structure structure;
	private Terrain terrain;
	private int x;
	private int y;
	
	public Cell(int givenX, int givenY, Terrain givenTerrain)
	{
		terrain = givenTerrain;
		structure = null;
		unit = null;
		 x = givenX;
		y = givenY;
	}
	
	public Cell(int givenX, int givenY, Terrain givenTerrain, Structure givenStructure)
	{
		terrain = givenTerrain;
		structure = givenStructure;
		unit = null;
		 x = givenX;
		y = givenY;
	}
	
	public Cell(int givenX, int givenY, Terrain givenTerrain, Structure givenStructure, Unit givenUnit)
	{
		terrain = givenTerrain;
		structure = givenStructure;
		unit = givenUnit;
		 x = givenX;
		y = givenY;
	}
	
	public Cell(int givenX, int givenY, Terrain givenTerrain, Unit givenUnit)
	{
		terrain = givenTerrain;
		unit = givenUnit;
		structure = null;
		 x = givenX;
		y = givenY;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public String getCoordinates()
	{
		String coordinates;
		
		coordinates = "" + x + ", " + y;
		
		return coordinates;
	}
	
	public boolean hasUnit()
	{
		return unit != null;
	}
	
	public boolean hasStructure()
	{
		return structure != null;
	}
	
	public Unit getUnit()
	{
		return unit;
	}
	
	public Terrain getTerrain()
	{
		return terrain;
	}
	
	public Structure getStructure()
	{
		return structure;
	}
	
	public void setUnit(Unit givenUnit)
	{
		unit = givenUnit;
	}
	
	public void setStructure(Structure givenStructure)
	{
		structure = givenStructure;
	}
	
	//Could implement things like flooding, deforestation, etc. Would need to be able to
	//set the terrain after constructing.
	public void setTerrain(Terrain givenTerrain)
	{
		terrain = givenTerrain;
	}

	public double distanceTo(Cell location) {
		
		int x_distance = (this.x - location.getX());
		int y_distance = (this.y - location.getY());
		
		double squared_distance = x_distance * x_distance + y_distance * y_distance;
		double distance = Math.sqrt(squared_distance);
		
		return distance;
	}
}