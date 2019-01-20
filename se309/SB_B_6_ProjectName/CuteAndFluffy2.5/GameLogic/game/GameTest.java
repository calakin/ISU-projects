
package game;

import java.util.ArrayList;

import map.Cell;
import map.CellMap;
import players.Account;
import players.Player;
import players.Team;
import units.Unit;

class GameTest {
	
	public static void main(String[] args)
	{
		CellMap map1 = new CellMap("test4");
		
		ArrayList<Account> onlineList = new ArrayList<Account>();
		onlineList.add(new Account("calakin"));
		onlineList.add(new Account("cksdnd004"));
		onlineList.add(new Account("dongkyun"));
		onlineList.add(new Account("jphan"));
		onlineList.add(new Account("clone1"));
		onlineList.add(new Account("clone2"));
		
		ArrayList<Team> teamList = new ArrayList<Team>();
		teamList.add(new Team(0, 2));
		teamList.add(new Team(1, 2));
		teamList.add(new Team(2, 2));
		
		teamList.get(0).addMember(onlineList.get(0).getPlayer());
		teamList.get(0).addMember(onlineList.get(1).getPlayer());
		teamList.get(1).addMember(onlineList.get(2).getPlayer());
		teamList.get(1).addMember(onlineList.get(3).getPlayer());
		teamList.get(2).addMember(onlineList.get(4).getPlayer());
		teamList.get(2).addMember(onlineList.get(5).getPlayer());
		
		Game game1 = new Game("game1", map1, teamList, onlineList);
		
		//System.out.println("Map after initialization:");
		//System.out.println(game1.getCellMap().toString());
		
		//start games thread
		Thread gameThread = new Thread(game1);
		gameThread.start();
		
		while(game1.getTurn() == null)
		{
			
		}

		System.out.println("Randomly generated turn order:");
		System.out.println(game1.getTurnOrderPrintable() + "\n");
		
		//test base placement
		//System.out.println("Map after random base placement (should include all players): ");
		//System.out.println(game1.getCellMap().toString());
		
		//test unit placement
		Player nonturn = game1.getTurnOrder().get(1);
		Player turn = game1.getTurnOrder().get(0);
		Cell nonturnBaseLoc = nonturn.getBase().getPosition();
		Cell turnBaseLoc = turn.getBase().getPosition();
		
		//incorrect
		Cell loc1 = game1.getCellMap().getCells()[(nonturnBaseLoc.getY()+1 > map1.getCells().length - 1 ? nonturnBaseLoc.getY()-1:nonturnBaseLoc.getY()+1)][(nonturnBaseLoc.getX()+1 > map1.getCells().length - 1 ? nonturnBaseLoc.getX()-1:nonturnBaseLoc.getX()+1)];
		//correct
		Cell loc2 = game1.getCellMap().getCells()[(turnBaseLoc.getY()+1 > map1.getCells().length - 1 ? turnBaseLoc.getY()-1:turnBaseLoc.getY()+1)][(turnBaseLoc.getX()+1 > map1.getCells().length - 1 ? turnBaseLoc.getX()-1:turnBaseLoc.getX()+1)];
		//incorrect: too far
		Cell loc3 = game1.getCellMap().getCells()[(nonturnBaseLoc.getY()+1 > map1.getCells().length - 1 ? nonturnBaseLoc.getY()-1:nonturnBaseLoc.getY()+1)][(nonturnBaseLoc.getX()+1 > map1.getCells().length - 1 ? nonturnBaseLoc.getX()-1:nonturnBaseLoc.getX()+1)];
		//incorrect: does not have another clubber
		Cell loc4 = game1.getCellMap().getCells()[(turnBaseLoc.getY()+1 > map1.getCells().length - 1 ? turnBaseLoc.getY()-1:turnBaseLoc.getY()+1)][(turnBaseLoc.getX()+2 > map1.getCells().length - 1 ? turnBaseLoc.getX()-2:turnBaseLoc.getX()+2)];
		
		System.out.println("Player's turn: " + turn.getName());
		System.out.println("Attempting to place giant for " + nonturn.getName() + " at x=" + loc1.getX() + ",y=" + loc1.getY());
		System.out.println(game1.placeUnit(nonturn.getName(), "giant", loc1));
		System.out.println("Attempting to place clubber for " + turn.getName() + " at x=" + loc2.getX() + ",y=" + loc2.getY() + "(correct).");
		System.out.println(game1.placeUnit(turn.getName(), "clubber", loc2));
		System.out.println("Attempting to place giant for " + turn.getName() + " at x=" + loc3.getX() + ",y=" + loc3.getY() + "(too far).");
		System.out.println(game1.placeUnit(turn.getName(), "giant", loc3));
		System.out.println("Attempting to place clubber for " + turn.getName() + " at x=" + loc4.getX() + ",y=" + loc4.getY() + "(has no more clubbers).");
		System.out.println(game1.placeUnit(turn.getName(), "clubber", loc4));
		
		//System.out.println("Map after attempts:");
		//System.out.println(game1.getCellMap().toString());
	
		//test end turn
		Player initialTurn = game1.getTurn();
		game1.endTurn();
		while(initialTurn.getName().equals(game1.getTurn().getName()));
		turn = game1.getTurn();	
		Cell loc5 = game1.getCellMap().getCells()[(nonturnBaseLoc.getY()+1 > map1.getCells().length - 1 ? nonturnBaseLoc.getY()-1:nonturnBaseLoc.getY()+1)][(nonturnBaseLoc.getX()+1 > map1.getCells().length - 1 ? nonturnBaseLoc.getX()-1:nonturnBaseLoc.getX()+1)];
		
		System.out.println("Ending turn.");
		System.out.println("Player turn: " + turn.getName());
		
		for(int i = 0; i < 5; i++){
			initialTurn = game1.getTurn();
			game1.endTurn();
			while(initialTurn.getName().equals(game1.getTurn().getName()));
			turn = game1.getTurn();	
			System.out.println("Ending turn.");
			System.out.println("Player turn: " + turn.getName());
		}
		
		//test movement
		Unit unit1 = game1.getTurn().getUnits().get(0);
		Unit unit2 = game1.getTurnOrder().get(1).getUnits().get(0);
		
		System.out.println(game1.getCellMap().toString());
		
		int x_dist = unit1.getPosition().getX() - unit2.getPosition().getX();
		int y_dist = unit1.getPosition().getY() - unit2.getPosition().getY();
		
		
		//test move limit
		
		
		//test attack
	}
}
	
	