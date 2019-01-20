package units;

import abilities.Ability;
import map.Cell;
import players.Player;
import structures.Base;

public abstract class Unit 
{	
	protected int attackRange, turnCost, unitCost, currentHP, maxHP,
				damage, costModifier, damageModifier;
	protected Cell curLocation;
	protected String name;
	protected Player owner; 
	protected Ability ability;
	
	//need to keep track of effects

	public abstract Unit copy();
	public abstract String getName();
	
	public boolean isDead()
	{
		return this.currentHP <= 0;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public int getAttackRange()
	{
		return this.attackRange;
	}
	
	public int getTurnCost()
	{
		return turnCost; 
	}

	public int getUnitCost()
	{
		return unitCost; 
	}
	
	public int getCurrentHP()
	{
		return this.currentHP;
	}
	
	public int getMaxHP()
	{
		return this.maxHP;
	}
	
	public Cell getPosition()
	{	
		return curLocation; 
	}
	
	public Ability getAbility()
	{
		return this.ability;
	}
	
	public void setPosition(Cell givenPosition)
	{
		this.curLocation = givenPosition;
	}
	
	public void setOwner(Player givenPlayer)
	{
		this.owner = givenPlayer;
	}
	
	public void hurt(int givenDamage)
	{
		this.currentHP -= givenDamage;
	}
	
	public void heal(int givenHeal)
	{
		this.currentHP += givenHeal;
	}
	
	public int move(Cell moveTo)
	{ 	
		int distanceX =  moveTo.getX() - this.curLocation.getX();
		int distanceY =  moveTo.getY() - this.curLocation.getY();

		if((distanceX!=0 && distanceY !=0))
		{
			System.out.println("not allowed: move in one direction");
			return 1;

		} else if((Math.abs(distanceX)+Math.abs(distanceY))!= 1)
		{
			System.out.println("not allowed: move only one space");
			return 1;

		} 
		this.curLocation.setUnit(null);
		moveTo.setUnit(this);
		this.curLocation = moveTo;
		return 0; 
	}
	
	public int attack(Unit enemyUnit)
	{
		//calculate distance
		int distanceX = Math.abs(enemyUnit.getPosition().getX() - this.curLocation.getX());
		int distanceY = Math.abs(enemyUnit.getPosition().getX() - this.curLocation.getY());
		double distance  = Math.sqrt(Math.pow(distanceY, 2) + Math.pow(distanceX, 2));
		
		//check if it is within range
		boolean ableToAttack = attackRange >= distance; 
		
		//if it is, attack
		if(ableToAttack)
		{
			enemyUnit.hurt(this.damage); 
			this.getOwner().addDamage(this.damage);
			
			return 0; 
		}
		//if it is not, do nothing and send error
		else
		{ 
			System.out.println("Enemy is not within attack range.");
			return 1; 
		}
	}
	
	public int attack(Base enemy)
	{
		//calculate distance
		int distanceX = Math.abs(enemy.getPosition().getX() - this.curLocation.getX());
		int distanceY = Math.abs(enemy.getPosition().getX() - this.curLocation.getY());
		double distance  = Math.sqrt(Math.pow(distanceY, 2) + Math.pow(distanceX, 2));
		
		//check if it is within range
		boolean ableToAttack = attackRange >= distance; 
		
		//if it is, attack
		if(ableToAttack)
		{
			enemy.hurt(this.damage); 
			this.getOwner().addDamage(this.damage);
			
			return 0; 
		}
		//if it is not, do nothing and send error
		else
		{ 
			System.out.println("Enemy is not within attack range.");
			return 1; 
		}
	}
	
	public int useAbility(){
		return 0;
	}
	
	public boolean sameUnitType(Unit otherUnit){
		return this.getClass().getTypeName().equals(otherUnit.getClass().getTypeName());
	}
}
