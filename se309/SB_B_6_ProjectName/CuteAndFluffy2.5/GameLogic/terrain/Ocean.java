package terrain;

import units.Unit;

public class Ocean extends Terrain
{
	
	public Ocean(){
		super.name = "ocean";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString()
	{
		return "5";
	}
}