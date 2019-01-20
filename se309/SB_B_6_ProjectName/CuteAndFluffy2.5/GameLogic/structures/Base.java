package structures;

import players.Player;

public class Base extends Structure
{
	protected int currentHP, maxHP;
	private Player owner;
	
	public void setOwner(Player givenOwner)
	{
		this.maxHP = this.currentHP = 200;
		this.owner = givenOwner;
	}
	
	public Player getOwner()
	{
		return this.owner;
	}
	
	public Base copy()
	{
		return new Base();
	}
	
	public void hurt(int givenDamage){
		this.currentHP -= givenDamage;
	}
	
	public void heal(int givenHeal){
		this.currentHP += givenHeal;
	}
	
	public int getCurrentHP(){
		return this.currentHP;
	}
	
	public int getMaxHP(){
		return this.maxHP;
	}
	
	public boolean isDead(){
		return this.currentHP <= 0;
	}
}