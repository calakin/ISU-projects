package gameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import game.Game;
import game.Lobby;
import map.Cell;
import map.CellMap;
import players.Account;
import players.Player;
import players.Team;
import structures.Base;
import units.Unit;


/**
 * This class creates an object that handles requests from a CuteAndFluffyServlet
 * in a separate thread. There is a strictly defined list of commands that can be
 * received. The game/lobby state is updated and the agreed upon response is sent.
 * @author Cody Lakin
 *
 */
class RequestHandler implements Runnable {

	CuteAndFluffyServer server;
	Socket requestorConnection;
	ObjectInputStream objIn;
	ObjectOutputStream objOut;

	public RequestHandler(Socket givenSocket, CuteAndFluffyServer givenServer)
	{
		try {
			this.requestorConnection = givenSocket;
			this.server = givenServer;
			this.objIn = new ObjectInputStream(requestorConnection.getInputStream());
			this.objOut = new ObjectOutputStream(requestorConnection.getOutputStream());
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	/**
	 * The handler reads a command from the input stream and handles it
	 * appropriately.
	 */
	@Override
	public void run()
	{
		try {
			String command = (String) objIn.readObject();

			int isUnsuccessful = handleCommand(command);

			objOut.writeInt(isUnsuccessful);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int handleCommand(String command)
	{		
		int i, j, numTeams;
		String response = "";
		ArrayList<Team> teams;
		ArrayList<String> stringArray = new ArrayList<String>();
		Lobby lobby = null;
		Game game = null;
		String name;
		String action;
		String username = null;

		System.out.println("Received Command: " + command);
		/*
		Expected form of requests is a string with space separated keywords/data.
		The order is as follows:
		game/lobby_name		action		player_name		data...
		 */

		Scanner scanCommand = new Scanner(command);			

		name 		= scanCommand.next();
		
//		if(name.equals("LOBBYLIST")) {
//			action = "LOBBYLIST";
//		} else {
		action 		= scanCommand.next();
//		}
		
		if(!action.equals("RECORD") && !action.equals("INFO") && !action.equals("PLAYERTEAMS") &&
				!action.equals("PLAYERSTATUS") && !action.equals("LOBBYINFO")){
			username 	= scanCommand.next();
		}
		
		//search the lobby list and game list for the given name
		if(!action.equals("LOBBYLIST")){
			lobby = server.getLobby(name);

			if(lobby == null){
				game = server.getGame(name);
				if(game == null && !action.equals("JOIN")){
					System.out.println("Server: No lobby or game found with the name: " + name);
					scanCommand.close();
					return -1;
				} else {
					System.out.println("Server: Found game " + name);
				}
			} else if(action.equals("JOIN")) {

			} else {
				System.out.println("Server: Found lobby " + name);
			}
		}

		switch(action){
		case "MOVE":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				CellMap map = game.getCellMap();
				Cell[][] cells = map.getCells();

				int currentX = scanCommand.nextInt();
				int currentY = scanCommand.nextInt();
				int newX = scanCommand.nextInt();
				int newY = scanCommand.nextInt();
				scanCommand.close();
				
				//set the unit to move & cell to move it to
				Cell currentCell 	= cells[currentY][currentX];
				Unit unit 			= currentCell.getUnit();
				Cell newCell 		= cells[newY][newX];

				//move the unit
				if(game.getMovesLeft() - unit.getTurnCost() >= 0 	&& 
						unit.getOwner() == game.getTurn()			&&
						game.getTurn().getName().equals(username)		){

					if(game.move(unit, newCell) != 0) return 1;
					game.setRecord(command);
				} else {
					return -1;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			break;
			
		case "SPEC1":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				CellMap map = game.getCellMap();
				Cell[][] cells = map.getCells();

				int currentX = scanCommand.nextInt();
				int currentY = scanCommand.nextInt();
				int newX = scanCommand.nextInt();
				int newY = scanCommand.nextInt();
				scanCommand.close();
				
				//set the unit to move & cell to move it to
				Cell attackerCell 	= cells[currentY][currentX];
				Cell targetCell		= cells[newY][newX];
				Unit attacker 		= attackerCell.getUnit();
				Unit targetUnit;
				Base targetBase;
				
				if(targetCell.hasUnit()){
					targetUnit 		= targetCell.getUnit();
					game.attackUnit(attacker, targetUnit);
				} else if(targetCell.hasStructure()){
					targetBase		= (Base)targetCell.getStructure();
					game.attackBase(attacker, targetBase);
				}
			
				game.setRecord(command);
				
			} catch (Exception e){
				e.printStackTrace();
			}
			
			break;
			
		case "PLACE":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				String unitName = scanCommand.next();
				int unitX = scanCommand.nextInt();
				int unitY = scanCommand.nextInt();
				scanCommand.close();
				
				Cell location = game.getCellMap().getCells()[unitY][unitX];
				
				if(game.placeUnit(username, unitName, location) != 0) return 1;
				game.setRecord(command);	
			} catch (Exception e){
				e.printStackTrace();
			}
			
			break;
			
		case "ENDTURN":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				Player initialTurn = game.getTurn();
				game.endTurn();
				while(initialTurn.getName().equals(game.getTurn().getName()));
				game.setRecord(command);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "ENDGAME":	
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				game.endGame();
				game.setRecord(command);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
		case "TURN":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "SPEC2":			
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				game.setRecord(command);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "SPEC3":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				game.setRecord(command);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "RECORD":
			if(game == null) return -1;
			while(game.getTurn() == null);
			
			try {
				String record = game.getRecord();
				if(record == null){
					record = "null";
					objOut.writeObject(record);
					return 1;
				}
	
				objOut.writeObject(record);
				objOut.flush();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "CELLINFO":
			if(game == null) return -1;
			while(game.getTurn() == null);

			try {
				int x = scanCommand.nextInt();
				int y = scanCommand.nextInt();
				String specific = scanCommand.next();
				scanCommand.close();
				
				CellMap map = game.getCellMap();
				Cell[][] cells = map.getCells();
				Cell cell = cells[y][x];
				
				if (sendCellInfo(game, cell, specific) != 0) return -1;
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "MAPINFO":
			if(game == null) return -1;
				
			
			int y = game.getCellMap().getCells().length;
			int x = game.getCellMap().getCells()[0].length;

			response = "" + x + " " + y;
			
			try {
				objOut.writeObject(response);
				objOut.flush();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "JOIN":
			
			if(!server.userExists(username)) return -1;
			Account joiningUser = server.getAccount(username);
			
			//join if exists, create if not
			if(server.nameExists(name)){
				lobby.addAccount(joiningUser);
			
			} else {
				numTeams = scanCommand.nextInt();
				int teamSize = scanCommand.nextInt();
				
				if(server.addLobby(teamSize, numTeams, username, name) != 0) return -1;			
				
				server.getLobby(name).addAccount(joiningUser);
			}
			
			break;
			
		case "LEAVE":
			
			if(!server.userExists(username)) return -1;
			Account leavingUser = server.getAccount(username);
			
			if(!server.nameExists(name)) return -1;
			
			lobby.removeAccount(leavingUser);
			
			break;
			
		case "READY":
			if(!server.userExists(username)) return -1;
			Account readyUser = server.getAccount(username);
			
			if(!server.nameExists(name)) return -1;
			
			int index = server.getLobby(name).getAccountList().indexOf(readyUser);
			
			lobby.getAccountList().get(index).readyUp();
			
			Game fdsa;
			
			if(username.equals(lobby.getHost().getPlayer().getName())){
				fdsa = lobby.startGame();
				server.addGame(fdsa);
				server.removeLobby(lobby.getName());
				fdsa.run();
			}
			
			break;
			
		case "SETMAP":
			
			if(!server.userExists(username)) return -1;
			Account host = server.getAccount(username);
			
			if(!lobby.getHost().getPlayer().getName().equals(username)) return 1;
			
			if(!server.nameExists(name)) return -1;
			
			String mapName = scanCommand.next();
			server.getLobby(name).setMapName(mapName);
			
			break;
			
		case "PLAYERTEAMS":
			scanCommand.close();
			
			if(!server.nameExists(name)) return -1;
			
			teams = lobby.getTeamList();
			numTeams = teams.size();
			
			for(i = 0; i < numTeams; i++){
				Team team = teams.get(i);
				
				for(j = 0; j < team.getMembers().size(); j++){
					stringArray.add(team.getMembers().get(j).getName());
					stringArray.add("" + team.getTeamNum());
				}
			}
			
			try{
				this.objOut.writeObject(stringArray.toArray());
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "PLAYERSTATUS":
			scanCommand.close();
			
			if(!server.nameExists(name)) return -1;
			
			int numPlayers = lobby.getAccountList().size();
			
			for(i = 0; i < numPlayers; i++){
				stringArray.add(lobby.getAccountList().get(i).getPlayer().getName());
				
				if(lobby.getAccountList().get(i).isReady()){
					stringArray.add("1");
				} else {
					stringArray.add("0");
				}
			}
			
			try{
				this.objOut.writeObject(stringArray.toArray());
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "LOBBYLIST":
			scanCommand.close();
			
			int numLobbies = server.getLobbies().size();
			for(i = 0; i < numLobbies; i++){
				Lobby asdf = server.getLobbies().get(i);
				stringArray.add(asdf.getName());
				stringArray.add(asdf.getHost().getPlayer().getName());
				stringArray.add("" + asdf.getSpotsOpen());
			}
			
			try{
				this.objOut.writeObject(stringArray.toArray());
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		case "LOBBYINFO":
			scanCommand.close();
			
			if(!server.nameExists(name)) return -1;
			//host_name   num_teams   team_size   map_name
			stringArray.add(lobby.getHost().getPlayer().getName());
			stringArray.add("" + lobby.getTeamList().size());
			stringArray.add("" + lobby.getTeamList().get(0).getMembers().size());
			stringArray.add(lobby.getMapName());
			
			try{
				this.objOut.writeObject(stringArray.toArray());
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
			break;
			
		default:
			System.out.println(name + ": Unrecognizable command.");
			return -1;
		}

		System.out.println(name + ": Command executed successfully.");
		return 0;
	}

	private int sendCellInfo(Game game, Cell cell, String specific)
	{
		String response = "";
		
		switch(specific){
		
		case "occupy":
			if(cell.hasUnit() || cell.hasStructure()) {
				response = "1";
			} else {
				response = "0";
			}
			break;

		case "team":
			//get the team number of the given cell's unit's owner
			int teamNumber = game.getTeams().indexOf(cell.getUnit().getOwner().getTeam());
			response += teamNumber;
			break;

		case "user":
			response += cell.getUnit().getOwner().getName();
			break;

		case "name":
			if(cell.hasStructure()){
				response += "base";
			} else {
				response += cell.getUnit().getName();
			}
			break;

		case "spec1":
			response += "attack";
			break;

		case "spec2":
			response += cell.getUnit().getAbility().getName();
			break;

		case "spec3":
			response += "null";
			break;

		case "maxhealth":
			if(cell.hasStructure()){
				response += ((Base)cell.getStructure()).getMaxHP();
			} else {
				response += cell.getUnit().getCurrentHP();
			}
			break;

		case "curhealth":
			if(cell.hasStructure()){
				response += ((Base)cell.getStructure()).getMaxHP();
			} else {
				response += cell.getUnit().getMaxHP();
			}
			break;

		//cost to move to this cell (assuming one cell away)
		case "groundmovement":
			response += cell.getUnit().getTurnCost();
			break;

		case "maxaction":
			response += game.getMaxTurnMoves();
			break;

		case "curaction":
			response += game.getMovesLeft();
			break;

		case "ground":
			response += cell.getTerrain().getName();
			break;
			
		case "activeplayer":
			response += game.getTurn().getName();
			break;
			
		case "gamespec":
			if(game.isOver()){
				response += "0";
			} else {
				response += "1";
			}
			break;

		default:
			response += "null";
		}
		
		try {
			objOut.writeObject(response);	
			objOut.flush();
		} catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
}

