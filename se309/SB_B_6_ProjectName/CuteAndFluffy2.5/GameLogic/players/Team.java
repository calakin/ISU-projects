package players;

import java.util.ArrayList;

public class Team
{
	private int teamNum;
	private int maxPlayers;
	private ArrayList<Player> members = new ArrayList<Player>();
	
	public Team(int givenNum, int givenMaxPlayers)
	{
		this.teamNum = givenNum;
		maxPlayers = givenMaxPlayers;
	}
	
	public void removeMember(Player member){
		members.remove(member);
	}
	
	public void addMember(Player newMember)
	{
		if(members.size() < maxPlayers){
			members.add(newMember);
	    	newMember.setTeam(this);
		} else {
			
		}
	}
	
	public int getMaxPlayers()
	{
		return maxPlayers;
	}
	
	public ArrayList<Player> getMembers()
	{
		return members;
	}

	public int getTeamNum() {
		return teamNum;
	}
}