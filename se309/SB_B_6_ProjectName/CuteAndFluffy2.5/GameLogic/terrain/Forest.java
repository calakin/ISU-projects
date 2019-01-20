package terrain;

import units.Unit;

public class Forest extends Terrain
{
	
	public Forest(){
		super.name = "forest";
	}

//	@Override
//	public void unitEffect(Unit givenUnit) 
//	{	
//		if(givenUnit.getName().equals("Giant"))
//		{
//			givenUnit.moveRange -= 2;
//		}
//		else
//		{
//			
//			givenUnit.moveRange -= 1;
//		}
//	}
	
	@Override
	public String toString()
	{
		return "2";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
}