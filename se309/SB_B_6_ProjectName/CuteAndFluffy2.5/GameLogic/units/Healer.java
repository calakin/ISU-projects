package units;

public class Healer extends Unit//support unit  ë“œë£¨ì�´ë“œ  shamen 
{
	public Healer()
	{
		maxHP = currentHP = 50; 
		attackRange = 3; 
		turnCost = 3;
		unitCost = 3; 
		damage = 5;
	}
	
	public Healer copy()
	{
		return new Healer();
	}
	
	public String getName()
	{
		return "healer";
	}
}
