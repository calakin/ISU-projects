package players;


import java.util.ArrayList;

import game.Game;
import game.GameMode;
import game.Lobby;
import units.Clubber;
import units.Giant;
import units.Healer;
import units.Raptor;
import units.Stonethrower;
import units.Unit;
import units.Voodoo;

public class Account
{
    private String userID;
    private Player player;
    private Lobby currentLobby;
    private Game currentGame;
	private boolean ready;
	private ArrayList<Unit> availableUnits = new ArrayList<Unit>();
	private ArrayList<Unit> gameUnits = new ArrayList<Unit>();

    public Account(String givenUserID)
    {
    	/**
    	 * temporary
    	 */
    	this.pickUnit(new Clubber());
    	this.pickUnit(new Giant());
    	this.pickUnit(new Healer());
    	this.pickUnit(new Raptor());
    	this.pickUnit(new Stonethrower());
    	this.pickUnit(new Voodoo());	
    	/**
    	 * 
    	 */
    	userID = givenUserID;
    	player = new HumanPlayer(userID);
    	ready  = false;
    }
    
    public void pickUnit(Unit unit){
    	gameUnits.add(unit);
    }
    
    public void joinLobby(Lobby lobby)
    {
    	if(currentLobby == null && currentGame == null){
    		currentLobby = lobby;
    		ready = false;
    	}
    }
    
    public ArrayList<Unit> getAvailableUnits(){
    	return this.availableUnits;
    }
    
    public ArrayList<Unit> getGameUnits(){
    	return this.gameUnits;
    }
    
    public boolean isReady(){
    	return this.ready;
    }
    
    public void readyUp(){
    	this.ready = true;
    }
    
    public String getUserID()
    {
    	return this.userID;
    }
    
    public void exitLobby()
    {
    	currentLobby = null;
    	player.clear();
    	ready = false;
    }
    
    public void enterGame(Game game)
    {
    	currentLobby = null;
    	currentGame = game;
    }
    
    public void exitGame()
    {
    	currentGame = null;
    	player.clear();
    }
    
    public Player getPlayer()
    {
    	return player;
    }
}
