package terrain;

import units.Unit;

public class Tundra extends Terrain
{
	
	public Tundra(){
		super.name = "tundra";
	}

//	@Override
//	public void unitEffect(Unit givenUnit) 
//	{	
//		if((givenUnit.getName().equals("Raptor"))||givenUnit.getName().equals("Voodoo"))
//		{
//			givenUnit.moveRange -= 3;
//		}
//		
//	}
	
	@Override
	public String toString()
	{
		return "8";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
}