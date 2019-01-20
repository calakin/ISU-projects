package units;

public class Stonethrower extends Unit//long range attacker
{
	public Stonethrower()
	{
		maxHP = currentHP = 70; 
		attackRange = 3; 
		turnCost = 2; 
		unitCost = 1;
		damage = 15;
	}
	
	public Stonethrower copy()
	{
		return new Stonethrower();
	}
	
	public String getName()
	{
		return "stonethrower";
	}
}