package game;

import java.util.ArrayList;

import map.CellMap;
import players.Account;
import players.Team;
import units.Clubber;
import units.Giant;
import units.Healer;
import units.Raptor;
import units.Stonethrower;
import units.Voodoo;

public class Lobby
{
	private static final String DEFAULTMAP = "test3";
	
    private ArrayList<Account> accountList = new ArrayList<Account>();
    private ArrayList<Team> teamList = new ArrayList<Team>();
    private int maxPlayers;
    private Account host;
    private String lobbyName;
    private String mapName;
    private Game game;
    
    public Lobby(int givenTeamSize, int givenNumTeams, Account givenHost, 
    			String givenName)
    {
    	mapName = DEFAULTMAP;
		lobbyName = givenName;
		maxPlayers = givenTeamSize * givenNumTeams;
		host = givenHost;
		game = null;
		
		accountList.add(host);
		host.joinLobby(this);
	
		//Adds needed number of teams to the lobby's list
		for(int i = 0; i < givenNumTeams; i++){
		    teamList.add(new Team(i, givenTeamSize));
		}
	
		teamList.get(0).addMember(host.getPlayer());
    }

    /** 
     * This function adds the given player to the first team with an open space, if there is one.
     * If there is no open space, the player is not added.
     */
    public void addAccount(Account givenAccount)
    {
    	//add the account to the lobby, if lobby is not full
    	if( accountList.size() < maxPlayers ) {
			accountList.add(givenAccount);
			givenAccount.joinLobby(this);
		} else {
			return;
		}
		//check the teams in order until there is an open space
		//if found, add player then return
		int numTeams = teamList.size();
		for(int i = 0; i < numTeams; i++){
		    if( teamList.get(i).getMembers().size() < teamList.get(i).getMaxPlayers() ){
		    	teamList.get(i).addMember(givenAccount.getPlayer());
			return;
		    }	
		}
    }

    public void removeAccount(Account givenAccount)
    {	
    	accountList.remove(givenAccount);

    	givenAccount.getPlayer().getTeam().removeMember(givenAccount.getPlayer());
    	
    	givenAccount.exitLobby();
    }

//    public void addAI(int difficulty)
//    {
//
//    }

    public Game startGame()
    {
    	CellMap map = new CellMap(mapName);

    	game = new Game(this.lobbyName, map, teamList, accountList);

    	return game;
    }

    public Account getHost(){
    	return this.host;
    }
    
    public int getSpotsOpen(){
    	return this.maxPlayers - this.accountList.size();
    }
    
//    public void setUnitValueCap()
//    {
//    }

//    public boolean readyCheck()
//    {
//    	int i;
//    	for(i = 0; i < accountList.size(); i++){
//    		if(!(accountList.get(i).getStatus().compareTo("host") == 0 ||
//    				accountList.get(i).getStatus().compareTo("ready") == 0)) {
//    			return false;
//    		}
//    	}
//    	
//    	return true;
//    }    

    public String getName()
    {
    	return this.lobbyName;
    }

    public ArrayList<Account> getAccountList()
    {
    	return this.accountList;
    }

    public ArrayList<Team> getTeamList()
    {
    	return this.teamList;
    }

    public String getMapName()
    {
    	return this.mapName;
    }
    
    public void setMapName(String givenMapName)
    {
    	this.mapName = givenMapName;
    }

//    public void setPlayerTeam(Player givenPlayer, Team givenTeam)
//    {
//    }
}
