package units;

import players.Player;

public class Giant extends Unit
{
	public Player owner;

	public Giant copy()
	{
		return new Giant();
	}
	public Giant()
	{	
		maxHP = currentHP = 100; 
		attackRange = 1; 
		turnCost = 3;
		unitCost = 4; 
		damage = 40; 
		
	}

	public String getName()
	{
		return "giant";
	}
}
