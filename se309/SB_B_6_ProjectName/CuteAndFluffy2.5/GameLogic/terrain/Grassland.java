package terrain;

import units.Unit;

public class Grassland extends Terrain
{
	public Grassland()
	{
		super.name = "grassland";
	}

//	@Override
//	public void unitEffect(Unit givenUnit) 
//	{	
//		if(!(givenUnit.getName().equals("Rapter")))
//		{
//			givenUnit.moveRange -= 1;
//		}
//	}
	
	@Override
	public String toString()
	{
		return "3";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
}