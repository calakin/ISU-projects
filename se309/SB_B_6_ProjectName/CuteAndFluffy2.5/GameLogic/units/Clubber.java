package units;

public class Clubber extends Unit//close range attack char
{
	public Clubber()//attacker character
	{
		maxHP = currentHP = 70; 
		attackRange = 2; 
		turnCost = 2; 
		unitCost = 1; 
		damage = 20;
	}
	
	public Clubber copy()
	{
		return new Clubber();
	}
	
	public String getName()
	{
		return "clubber";
	}
}
