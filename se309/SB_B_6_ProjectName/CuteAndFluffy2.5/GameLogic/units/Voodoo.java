package units;

public class Voodoo extends Unit//support
{
	public Voodoo()//tank character
	{
		maxHP = currentHP = 50; 
		attackRange = 3; 
		turnCost = 3;
		unitCost = 3; 
		damage = 10;
	}
	
	public Voodoo copy()
	{
		return new Voodoo();
	}
	
	public String getName()
	{
		return "voodoo";
	}
}
