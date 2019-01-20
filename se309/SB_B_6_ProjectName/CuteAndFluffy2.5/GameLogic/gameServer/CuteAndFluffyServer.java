package gameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import game.Game;
import game.GameMode;
import game.Lobby;
import map.Cell;
import map.CellMap;
import players.Account;
import players.Team;
import units.Unit;

public class CuteAndFluffyServer {

	//keep track of games
	//keep track of lobbies
	//keep track of online players
	//provide any of these objects upon request
	//add any of these objects upon request
	ArrayList<Account> onlineList = new ArrayList<Account>();
	ArrayList<Game> gameList = new ArrayList<Game>();
	ArrayList<Lobby> lobbyList = new ArrayList<Lobby>();
	
	ServerSocket serverSocket;
	static int port = 4444;
	
	public CuteAndFluffyServer()
	{
		serverSocket = null;
		
		try {
		    serverSocket = new ServerSocket(port);
		} catch (IOException e) {
		    System.out.println("Server console: Could not listen on port: 4444");
		    System.exit(-1);
		}
	}
	
	private void waitForRequests()
	{
		while(true){

			try {
			System.out.println("Server: waiting for commands");
			Socket newSocket;

			//accept request and store socket
			newSocket = serverSocket.accept();
			System.out.println("Server: command received; starting new handler");
			Thread t = new Thread( new RequestHandler(newSocket, this) );
			t.start();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds a account to the online list. This signifies that a player is online.
	 * A player must have logged in to be added.
	 * @return
	 */
	public int addAccount(String username)
	{
		return 0;
	}
	
	/**
	 * Removes an account from the online list. This signifies that a player has
	 * logged off and is no longer using the game.
	 * @return
	 */
	public int removeAccount(String username)
	{
		if(!this.nameExists(username)) return -1;
		
		this.onlineList.remove(username);
		
		return 0;
	}
	
	/**
	 * This returns the account with the given name, if online.
	 * @return
	 *	Returns an account object if found. If not, returns null.
	 */
	public Account getAccount(String username)
	{
		int length = this.onlineList.size();
		int i;
		for(i = 0; i < length; i++){
			if(this.onlineList.get(i).getPlayer().getName().equals(username)){
				return this.onlineList.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Adds the given game to the list of active games.
	 * @param newGame
	 * 	The game object to be added.
	 */
	public int addGame(Game newGame)
	{
		try {
			Thread gameThread = new Thread(newGame);
			gameThread.start();
			
			gameList.add(newGame);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		
		return 0;
	}
	
	/**
	 * This method removes the specified lobby from the list of active lobbies,
	 * and removes the Game object.
	 * @param givenName
	 * 	This is the name of the Game object that will be searched for and removed.
	 * @return
	 *	Returns -1 if game does not exist. Returns 0 if successful. Returns 1
	 *	if game cannot be removed.
	 */
	public int removeGame(String givenName)
	{
		Game game = this.getGame(givenName);
		
		if(game == null){
			return -1;
		}
		
		try {
			game.endGame();
			gameList.remove(game);
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	/**	This method searches the active Game list for a game
	 * 	with the specified name. 
	 * @param gameName
	 * 	The name of the Game object to be returned;
	 * @return
	 * 	A Game object.
	 */
	public Game getGame(String gameName)
	{
		Game game = null;
		
		int i = 0;
		while(i < gameList.size() && !gameName.equals(gameList.get(i).getName())){
			i++;
		}
		
		if(i != gameList.size()){
			game = gameList.get(i);
		}
		
		return game;
	}
	
	/**	This method searches the active Lobby list for a lobby
	 * 	with the specified name. 
	 * @param lobbyName
	 * 	The name of the Lobby object to be returned;
	 * @return
	 * 	A Lobby object.
	 */
	public Lobby getLobby(String lobbyName)
	{
		Lobby lobby = null;
		
		int i = 0;
		while(i < lobbyList.size() && !lobbyName.equals(lobbyList.get(i).getName())){
			i++;
		}
		
		if(i != lobbyList.size()){
			lobby = lobbyList.get(i);
		}
		
		return lobby;
	}
	
	/**
	 * Adds a new Lobby object to the server
	 * @param givenTeamSize
	 * 	The maximum number of players per team.
	 * @param givenNumTeams
	 * 	The maximum number of teams.
	 * @param givenUsername
	 * 	The username of the "host", the player who creates
	 * 	the lobby.
	 * @param givenLobbyName
	 * 	The name by which the new lobby will be known to the server.
	 * 	The lobby name is transferred to the game when it starts.
	 */
	public int addLobby(int givenTeamSize, int givenNumTeams, String givenUsername, 
						String givenLobbyName)
	{
		Account host;
		
		if(this.nameExists(givenLobbyName)) return -1;
		if(!(this.userExists(givenUsername))){
			return -1;
		} else {
			host = this.getAccount(givenUsername);
		}
			
		Lobby newLobby = new Lobby(givenTeamSize, givenNumTeams, host, givenLobbyName);
		
		this.lobbyList.add(newLobby);
		
		return 0;
	}
	
	/**
	 * This method checks if the given username matches that of an
	 * online player.
	 * @param givenUsername
	 * 	The name to be searched for.
	 * @return
	 *	True of the name exists, false if it does not.
	 */
	public boolean userExists(String givenUsername){
		int i, length = this.onlineList.size();
		boolean userExists = false;;
		
		for(i = 0; i < length; i++){
			if(this.onlineList.get(i).getPlayer().getName().equals(givenUsername)){
				userExists = true;
			}
		}
		
		return userExists;
	}
	
	/**
	 * This method checks if the given name matches that of an
	 * active game or lobby.
	 * @param givenUsername
	 * 	The name to be searched for.
	 * @return
	 *	True of the name exists, false if it does not.
	 */
	public boolean nameExists(String givenName){
		int i, length = this.lobbyList.size();;
		boolean nameTaken = false;;
		
		for(i = 0; i < length; i++){
			if(this.lobbyList.get(i).getName().equals(givenName)){
				nameTaken = true;
			}
		}
		
		length = this.gameList.size();
		for(i = 0; i < length; i++){
			if(this.gameList.get(i).getName().equals(givenName)){
				nameTaken = true;
			}
		}
		
		return nameTaken;
	}
	
	/**
	 * This method removes the specified lobby from the list of active lobbies,
	 * and removes the Lobby object.
	 * @param givenName
	 * 	This is the name of the Lobby object that will be searched for and removed.
	 * @return
	 *	Returns -1 if lobby does not exist. Returns 0 if successful. Returns 1
	 *	if lobby cannot be removed.
	 */
	public int removeLobby(String givenName)
	{
		if(this.nameExists(givenName)) return -1;
		
		Lobby lobby = this.getLobby(givenName);
		
		this.lobbyList.remove(lobby);
		
		return 0;
	}
	
	/**
	 * This returns an array list of Lobby objects that are active.
	 */
	public ArrayList<Lobby> getLobbies()
	{	
		return this.lobbyList;
	}	
	
	/**
	 * This returns an array of all the names of active Lobby objects.
	 */
	public String[] getLobbyNames()
	{
		int i;
		int length = this.lobbyList.size();
		String[] nameList = new String[length];
		
		for(i = 0; i < length; i++){
			nameList[i] = lobbyList.get(i).getName();
		}
		
		return nameList;
	}
	
	public static void main(String[] args){
		CuteAndFluffyServer server = new CuteAndFluffyServer();
		
		server.waitForRequests();
	}
}