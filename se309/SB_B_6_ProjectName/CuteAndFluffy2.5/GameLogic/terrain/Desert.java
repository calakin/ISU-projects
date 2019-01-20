package terrain;

import units.Unit;

public class Desert extends Terrain
{
	
	public Desert(){
		super.name = "desert";
	}

//	@Override
//	public void unitEffect(Unit givenUnit) 
//	{	
//		if(!(givenUnit.getName().equals("Rapter")))
//		{
//			givenUnit.moveRange-= 1;
//		}
//	}
	
	@Override
	public String toString()
	{
		return "1";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
}