package game;

import java.util.ArrayList;
import java.util.Random;

import database.JDBC;
import map.Cell;
import map.CellMap;
import players.Account;
import players.Player;
import players.Team;
import structures.Base;
import units.Clubber;
import units.Giant;
import units.Healer;
import units.Raptor;
import units.Stonethrower;
import units.Unit;
import units.Voodoo;

 /**
  * This is a Game object representing a game of CuteAndFluffy. It is created
  * automatically when transitioning from a Lobby to a game.
  * @author Cody Lakin
  *
  */
public class Game implements Runnable
{
	//private static final long maxTurnTime = 120000; //in milliseconds, 2 mins
	private static final int maxTurnMoves = 10;
	private static final int maxAllyDistance = 2;
	private static final int maxUnitPlacementDistance = 5;
	private int minEnemyDistance;

	/**
	 * This CellMap represents the game's current state as it relates to
	 * terrain, structures, and units.
	 */
	private CellMap map;
	
	/**
	 * This list keeps track of all accounts associated with players in the game.
	 */
	private ArrayList<Account> accountList = new ArrayList<Account>();
	
	/**
	 * This list keeps track of all players in the game.
	 */
	private ArrayList<Player> playerList = new ArrayList<Player>();
	
	/**
	 * This list keeps track of all teams in the game.
	 */
	private ArrayList<Team> teamList = new ArrayList<Team>();
	
	/**
	 * This array contains once instance of every Player object in the game.
	 * The order of the indices represent the turn order of the players.
	 */
	private ArrayList<Player> turnOrder = new ArrayList<Player>();
	
	/**
	 * This is a container for the games settings chosen by the host
	 */
	//private GameMode mode;
	
	/**
	 * This is the name of the game object.
	 */
	private String gameName;
	
	/**
	 * This is the player whose turn it is.
	 */
	private Player activePlayer;
	
	/**
	 * This is how many moves the current player has left.
	 */
	private int movesUsed;
	
	/**
	 * This is a string that stores the last command received from a client
	 */
	private String record;
	
	public boolean endTurn = false;
	private boolean gameOver = false;
	private int winnerIndex;
	private boolean unitsPlaced = false;
	
	/**
	 * This constructor creates a Game object representing a game of CuteAndFluffy
	 * @param givenMap
	 * 	The given CellMap contains the present map size, terrain, wild units, and structures.
	 * @param givenTeams
	 * 	The given list of Teams determines who is whose ally/enemy
	 * @param givenPlayers
	 * 	The given list of Accounts keeps track of all human players in the game
	 * @param givenMode
	 * 	The given GameMode contains user specified settings used to set up and run the game.
	 */
	public Game(String givenName, CellMap givenMap, ArrayList<Team> givenTeams, 
				ArrayList<Account> givenPlayers) {
		
		this.gameName = givenName;
		this.teamList = givenTeams;
		this.accountList = givenPlayers;
		this.map = givenMap;
		this.initMinEnemyDistance();
		this.initPlayerList();
	}

	/**
	 * This returns the game status (in progress = false, over = true). 
	 * The game is over when all players are dead except 1 team.
	 * @return
	 */
	public boolean isOver(){
		return this.gameOver;
	}
	
	/**
	 * Returns the Player object corresponding to the active player.
	 */
	public Player getTurn()
	{
		return activePlayer;
	}
	
	public int getMaxTurnMoves() {
		return Game.maxTurnMoves;
	}
	
	/**
	 * Return the number of moves left for the active player's turn.
	 * @return
	 */
	public int getMovesLeft()
	{
		return maxTurnMoves - movesUsed;
	}

	/**
	 * Print the usernames of the players, in order of their turns.
	 * @return
	 */
	public String getTurnOrderPrintable()
	{
		String turns = "";

		int i, length;
		length = turnOrder.size();
		for(i = 0; i < length; i++){
			turns += turnOrder.get(i).getName();
			
			if(i < length -1) turns += ", ";
		}
		turns += "\n";

		return turns;
	}
	
	/**
	 * This method returns the game instance's record variable.
	 */	
	public String getRecord()
	{
		return this.record;
	}
	
	/**
	 * This methods returns the name of the game instance.
	 * @return
	 */
	public String getName()
	{
		return this.gameName;
	}
	
	/**
	 * Returns an array list of all Player objects in the game.
	 * @return
	 */
	public ArrayList<Player> getPlayerList()
	{
		return this.playerList;
	}
	
	/**
	 * Returns an array list of all Account objects in the game. Indices
	 * correspond to the associated Player object in the player list.
	 * @return
	 */
	public ArrayList<Account> getPlayers() {
		return this.accountList;
	}
	
	/**
	 * Returns an array list of all Team objects in the game.
	 * @return
	 */
	public ArrayList<Team> getTeams() {
		return this.teamList;
	}
	
	/**
	 * Returns a CellMap, which represents the map of a game. 
	 * @return
	 */
	public CellMap getCellMap() {
		return this.map;
	}
	
	// public String getSettings() {

	// }
	
	public ArrayList<Player> getTurnOrder() {
		return this.turnOrder;
	}
	
	/**
	 * This method sets the game instance's record variable.
	 * @param command
	 * 	Command is the last command sent by a user that changes the game state. 
	 * 	It is retrieved by the GUI for updating purposes.
	 */
	public void setRecord(String command)
	{
		this.record = command;
	}
	
	//
	public int attackUnit(Unit attacker, Unit target){
		this.decrementMovesLeft(attacker.getTurnCost());
		attacker.attack(target);
		
		if(target.isDead()) {
			this.removeUnit(target);
		}
		
		return 0;
	}
	
	//
	public int move(Unit unit, Cell target){
		if(unit.move(target) != 0) return 1;
		this.decrementMovesLeft(unit.getTurnCost());
		
		return 0;
	}
	
	//
	public int attackBase(Unit attacker, Base target){
		this.decrementMovesLeft(attacker.getTurnCost());
		attacker.attack(target);
		
		if(target.isDead()){
			this.removeBase(target);
		}
		
		return 0;
	}
	
	public int placeUnit(String username, String unitName, Cell location){
		Account user = null;
		Unit unit = null;
		Base userBase = null;
		
		for(int i = 0; i < this.accountList.size(); i++){
			if(this.accountList.get(i).getPlayer().getName().equals(username)){
				user = this.accountList.get(i);
			}
		}
		
		if(!user.getPlayer().getName().equals(this.activePlayer.getName())) return -1;
		
		userBase = user.getPlayer().getBase();
		
		if (userBase.getPosition().distanceTo(location) > Game.maxUnitPlacementDistance) return -1;
		
		switch(unitName){
		case "clubber":
			unit = new Clubber();
			break;
		case "giant":
			unit = new Giant();
			break;
		case "healer":
			unit = new Healer();
			break;
		case "raptor":
			unit = new Raptor();
			break;
		case "stonethrower":
			unit = new Stonethrower();
			break;
		case "voodoo":
			unit = new Voodoo();
			break;
		default:
			return -1;
		}
		
		int index = -1;
		for(int i = 0; i < user.getGameUnits().size(); i++) {
			if(user.getGameUnits().get(i).getName().equals(unitName)){
				index = i;
			}
		}

		if(index == -1) return -1;
		
		unit.setPosition(location);
		unit.setOwner(user.getPlayer());
		location.setUnit(unit);
		
		user.getPlayer().addUnit(unit);
		user.getGameUnits().remove(index);
		return 0;
	}
	
//	public void saveGame() {
//	}

	/**
	 * This method ends the active player's turn.
	 */
	public void endTurn() {
		this.endTurn = true;
	}

	/**
	 * This method manually ends the game with no winner.
	 */
	public void endGame() {
		this.winnerIndex = -1;
		gameOver = true;
	}
	
	/*
	 * The following initializes minEnemyDistance for this game.
	 * minEnemyDistance is the minimum distance a player's base
	 * must be placed from their enemies.
	 * It is calculated based on the map size, team size, number
	 * of teams. 
	 * If the map is large enough, it will be 12 at the highest
	 */
	private void initMinEnemyDistance(){
		int maxTeamSize = teamList.get(0).getMaxPlayers();
		
		int mapYsize = map.getCells().length;
		int mapXsize = map.getCells()[mapYsize - 1].length;
		
		int smallSide = Math.min(mapXsize,  mapYsize);
		int largeSide = Math.max(mapXsize,  mapYsize);
		
		int cellsPerTeam = (int)Math.floor(largeSide / teamList.size());
		int offset = (int) Math.floor(maxTeamSize / smallSide);
		this.minEnemyDistance = Math.min(cellsPerTeam - offset, 12);
	}
	
	private void decrementMovesLeft(int amount){
		this.movesUsed += amount;
	}
	
	private void removeUnit(Unit givenUnit)
	{
		givenUnit.getOwner().getUnits().remove(givenUnit);
		map.getCells()[givenUnit.getPosition().getY()][givenUnit.getPosition().getX()].setUnit(null);
		if(this.checkWinLoss() != -1) this.endTurn = true;
	}
	
	private void removeBase(Base givenBase){
		givenBase.getOwner().setBase(null);
		givenBase.getPosition().setStructure(null);
		if(this.checkWinLoss() != -1) this.endTurn = true;
	}
	
	private void startTurn() {
		endTurn = false;
		if(activePlayer.isDead() && unitsPlaced == false){
			this.turnOrder.remove(activePlayer);
			endTurn = true;
		}

		while(!(this.endTurn));
	}
	
	//returns index of winning team or -1 if the game is still in progress
	private int checkWinLoss()
	{
		int numDeadTeams = 0;
		int teamIndex = -1;
		
		//check for a team with all bases/units gone for every player
		for(int i = 0; i < teamList.size(); i++){
			boolean isDead = false;
			
			for(int j = 0; j < teamList.get(i).getMembers().size(); j++){
				
				if(!teamList.get(i).getMembers().get(j).isDead()){
					teamIndex = i;
					break;
				}
				
				//if this is the last player on the team
				if(j == teamList.get(i).getMembers().size() - 1){
					isDead = true;
				}
			}
			
			if(isDead){
				numDeadTeams++;
			}
		}
		
		if(numDeadTeams == teamList.size() - 1){
			this.gameOver = true;
			this.winnerIndex = teamIndex;
			return teamIndex;
		} else {
			return -1;
		}
	}
	
	private void initPlayerList()
	{
		for(int i = 0; i < accountList.size(); i++){
			Player player = accountList.get(i).getPlayer();
			playerList.add(player);
		}
	}
	
	private void startTurns() {
		activePlayer = turnOrder.get(0);
		movesUsed = 0;
		//int previousIndex = -1;
		
		while(!gameOver){
			//while(previousIndex == i);

			startTurn();

			//previousIndex = i;
			int i = turnOrder.indexOf(activePlayer);
			if(i == turnOrder.size() - 1){
				this.activePlayer = turnOrder.get(0);
				this.unitsPlaced = true;
			} else{
				this.activePlayer = turnOrder.get(i + 1);
			}

			this.movesUsed = 0;
		}
	}

	private void processGame() {
		
		if(this.winnerIndex == -1) return;
		
		try {
			JDBC database = new JDBC();
			database.openDB();

			int i, j;

			for(i = 0; i < teamList.size(); i++){
				for(j = 0; j < teamList.get(i).getMembers().size(); j++){

					Player player = teamList.get(i).getMembers().get(j);
					String playerName = player.getName();

					if(i == winnerIndex){
						database.addWin(1, playerName);
					} else {
						database.addWin(0,  playerName);
					}

					database.addTotalDMG(player.getDamage(), playerName);
				}	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private void loadGame() {
//	}

	//used by this class to initialize turnOrder
	private void setTurnSequence() {
		Random r = new Random(System.nanoTime());
		
		ArrayList<Team> teamOrder = new ArrayList<Team>();
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(int i = 0; i < accountList.size(); i++){
			players.add(accountList.get(i).getPlayer());
		}

		//get a random turn order for the teams
		while(teamOrder.size() < teamList.size()){
			int nextTeam = (Math.abs(r.nextInt()) % teamList.size());
			Team team = teamList.get(nextTeam);

			if(!teamOrder.contains(team)){
				teamOrder.add(team);
			}
		}

		int teamIndex = 0;
		int direction = 1;
			
		//Selects a player randomly from each team until all players are included.
		//The first team in teamOrder gets the first pick from it the first 
		//time around. It gets the last player picked from it the second time around. 
		//And so on.
		while(!players.isEmpty()){

			Team currentTeam = teamOrder.get(teamIndex);
			int playerIndex = Math.abs(r.nextInt()) % (currentTeam.getMembers().size());
			Player player = currentTeam.getMembers().get(playerIndex);

			if(players.contains(player)) {			
				turnOrder.add(player);	
				players.remove(player);
				teamIndex += direction;
				
				if(teamIndex == teamOrder.size() - 1) {
					if(direction == 0){
						direction = -1;
					} else {
						direction = 0;
					}
				} else if (teamIndex == 0) {
					if(direction == 0){
						direction = 1;
					} else {
						direction = 0;
					}
				}
			}
		}
	}

	private void placePlayers() 
	{
		Base bases[][] = placeBases();
		//System.out.println("Checkpoint: Bases have been generated");
		
		int i, j, numTeams = teamList.size();
		for(i = 0; i < numTeams; i++){
			ArrayList<Player> teamMembers = teamList.get(i).getMembers();
			int teamSize = teamMembers.size();
			for(j = 0; j < teamSize; j++){
				teamMembers.get(j).setBase(bases[i][j]);
			}
		}
		//System.out.println("Checkpoint: Players' bases have been placed");
	}

	//creates multiple arrays of bases. Each array of bases represents the bases
	//for a single team (they are away from bases in other arrays)
	private Base[][] placeBases()
	{
		Random r = new Random(System.nanoTime());
		int numTeams = teamList.size();
		int maxTeamSize = teamList.get(numTeams - 1).getMaxPlayers();
		Base bases[][] = new Base[numTeams][maxTeamSize];
		int baseX = 0, baseY = 0;
		boolean nearEnemies, isOpen, nearAllies;
		Cell baseLocation;
		
		//find a potential base for that max number of players in each team
		int i, j, p, q;
		for(i = 0; i < numTeams; i++){
			for(j = 0; j < maxTeamSize; j++){		
				do{
					nearEnemies = false;
					isOpen = true;
					nearAllies = true;
					
					//get random x and y coordinates
					baseX = Math.abs(r.nextInt() % map.getCells()[0].length);
					baseY = Math.abs(r.nextInt() % map.getCells().length);

					baseLocation = map.getCells()[baseY][baseX];
					
					//check if location is open
					if((!baseLocation.hasStructure()) && (!baseLocation.hasUnit())){
						isOpen = true;
								
						//check if location is within range of allies
						for(p = 0; p < j; p++){
							Cell allyBase = bases[i][p].getPosition();
							int allyY = allyBase.getY();
							int allyX = allyBase.getX();
							int distY = allyY - baseY;
							int distX = allyX - baseX;
							int dist = (int) Math.round(Math.sqrt(distX * distX + distY * distY));
							
							if(dist == 0) isOpen = false;
							
							if(dist > Game.maxAllyDistance){
								nearAllies = false;
								break;
							}
						}
						
						if(nearAllies && isOpen) {
						
							//check if location is near enemies
							for(p = 0; p < i; p++){
								for(q = 0; q < teamList.get(i).getMembers().size(); q++){
									Cell enemyLocation = bases[p][q].getPosition();
									int enemyY = enemyLocation.getY();
									int enemyX = enemyLocation.getX();
									int distY = enemyY - baseY;
									int distX = enemyX - baseX;
									int dist  = (int) Math.floor(Math.sqrt(distX * distX + distY * distY));

									if(dist < minEnemyDistance){
										nearEnemies = true;
									} 
								}
							}
						}	
					} else {
						isOpen = false;
					}

				} while(nearEnemies || !isOpen || !nearAllies);
			
				//if open and away from enemies, add base to the array
				Base newBase = new Base();
				newBase.setPosition(baseLocation);
				bases[i][j] = newBase;
			}
		}
		
		return bases;
	}
	
	/**
	 * This function will serve as the main loop of the game object.
	 * Here, the game logic will be run independently of the players.
	 * Player input will be received elsewhere, but it will be waited
	 * for here.
	 */
	@Override 
	public void run()
	{	
		setTurnSequence();

		placePlayers();	
			
		startTurns();
		
		processGame();	
	}
}
