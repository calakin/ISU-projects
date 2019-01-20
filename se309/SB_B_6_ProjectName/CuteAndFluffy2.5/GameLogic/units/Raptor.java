package units;

public class Raptor extends Unit// tank
{
	public Raptor()//tank character
	{
		maxHP = currentHP = 100; 
		attackRange = 1;  
		turnCost = 3;
		unitCost = 2;
		damage = 15;
	}
	
	public Raptor copy()
	{
		return new Raptor();
	}
	
	public String getName()
	{
		return "raptor";
	}	
}