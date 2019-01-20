package terrain;

import units.Unit;

public class River extends Terrain
{
	
	public River(){
		super.name = "river";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString()
	{
		return "6";
	}
}