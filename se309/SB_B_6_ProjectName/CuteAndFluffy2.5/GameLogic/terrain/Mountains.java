package terrain;

import units.Unit;

public class Mountains extends Terrain
{
	
	public Mountains(){
		super.name = "mountains";
	}

	@Override
	public void unitEffect(Unit givenUnit) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString()
	{
		return "4";
	}
}