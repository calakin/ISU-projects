package players;

import java.util.ArrayList;

import map.Cell;
import structures.Base;
import units.Unit;

public abstract class Player 
{
	private String name;
	private Team team;
	private Base base;
	private ArrayList<Unit> units = new ArrayList<Unit>();
	private int damageDone;
	
	public Player(String givenName)
	{
		damageDone = 0;
		name = givenName;
		base = null;
		team = null;
	}

	public ArrayList<Unit> getUnits()
	{
		return this.units;
	}
	
	public int addUnit(Unit givenUnit){
		this.units.add(givenUnit);
		
		return 0;
	}
	
	public void setTeam(Team givenTeam)
	{
		team = givenTeam;
	}
	
	public Team getTeam()
	{
		return team;
	}
	
	public void clear()
	{
		this.team = null;
		this.base = null;
		this.damageDone = 0;
	}
	
	public void placeUnit(String unitName, Cell givenLocation)
	{
		//check if unit is available
		
		//assign unit to cell
	}
	
	public void setBase(Base givenBase)
	{
		this.base = givenBase;
		this.base.setOwner(this);
		givenBase.getPosition().setStructure(this.base);
	}
	
	public Base getBase()
	{
		return base;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addDamage(int damage)
	{
		this.damageDone += damage;
	}
	
	public int getDamage()
	{
		return this.damageDone;
	}
	
	public boolean isDead(){
		return (this.getBase() == null && this.getUnits().size() == 0);
	}
}