package map;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

import structures.Base;
import structures.Ruins;
import structures.Settlement;
import structures.Structure;
import terrain.Desert;
import terrain.Forest;
import terrain.Grassland;
import terrain.Mountains;
import terrain.Ocean;
import terrain.River;
import terrain.Taiga;
import terrain.Terrain;
import terrain.Tundra;
import units.Clubber;
import units.Giant;
import units.Healer;
import units.Raptor;
import units.Stonethrower;
import units.Unit;
import units.Voodoo;

public class CellMap
{
	private static final int MAXHEIGHT = 100;
	private static final int MAXWIDTH = 100;
	private static final int MINHEIGHT = 15;
	private static final int MINWIDTH = 15;
	
	private static final Terrain[] terrainTable = {
		new Desert(), new Forest(), new Grassland(),
		new Mountains(), new Ocean(), new River(),
		new Taiga(), new Tundra()
	};
	private static final Unit[] unitTable = {
		new Clubber(), new Raptor(),
		new Stonethrower(), new Voodoo(),
		new Giant(), new Healer()
	};
	private static final Structure[] structureTable = {
		new Ruins(), new Settlement()	
	};
	
	private Cell map[][];
	private String mapName;	
	
	public CellMap(String givenName)
	{
		mapName = givenName;
		
		if(mapName.equals("Random")) {
			generateMap();
		} else {
			loadFromFile(givenName);
		}	
	}

	//there can be an option for more specifications here
	public CellMap(int height, int width)
	{
		generateMap(height, width);
	}
	
	public void loadFromDatabase(String mapName)
	{
		
	}
	
	//File format
	//1st line height
	//2nd line width
	//next lines: terrain type, structure type, unit type
	//all values specified by integers. Cell specs refer to 
	//the corresponding table
	private void loadFromFile(String fileName)
	{
		try{
			Random r = new Random(System.nanoTime());
			
			//Get the filepath of the map to be loaded
			String filepath = "./maps/" + fileName + ".txt";
			File mapFile = new File(filepath);

			//scan in the map data
			if(mapFile.exists()){
				Scanner scanLines = new Scanner(mapFile);
				
				int mapHeight = scanLines.nextInt();
				scanLines.nextLine();
				int mapWidth = scanLines.nextInt();
				scanLines.nextLine();
				
				//initialize map dimensions
				map = new Cell[mapHeight][mapWidth];
				
				int i,j;
				for(i = 0; i < mapHeight; i++){
					for(j = 0; j < mapWidth; j++){
						
						if(!scanLines.hasNextLine()) {
							throw new IllegalArgumentException("Given file's "
									+ "specified height and width do not match its data.");
						}
						//each cell has its own line
						String cellInfo = scanLines.nextLine();
						Scanner scanInts = new Scanner(cellInfo);
						
						//terrain must be specified
						int terrainType = scanInts.nextInt();
						
						Cell newCell;
						
						//if 0 or out of terrainTable bounds, random
						if(terrainType > 0 && terrainType < terrainTable.length + 1){
							newCell = new Cell(j, i, terrainTable[terrainType - 1]);	
						} else {
							terrainType = (Math.abs(r.nextInt()) % (terrainTable.length)) + 1;
							newCell = new Cell(j, i, terrainTable[terrainType - 1]);
						}
						
						int unitType = -1;
						int structureType = -1;
						
						//unit and structure are optional
						if(scanInts.hasNextInt()){
							structureType = scanInts.nextInt();
						} 
						if(scanInts.hasNextInt()){
							unitType = scanInts.nextInt();
						} 
						
						//no randoms, nothing if out of bounds
						if(structureType >= 0 && structureType < structureTable.length) {
							newCell.setStructure(structureTable[structureType].copy());
							newCell.getStructure().setPosition(newCell);
							
						}
						
						//random if 0, nothing if out of bounds
						if(unitType == 0){
							unitType = (Math.abs(r.nextInt()) % (unitTable.length)) + 1;
							newCell.setUnit(unitTable[unitType - 1].copy());
							newCell.getUnit().setPosition(newCell);
						} else if(unitType > 0 && unitType < unitTable.length + 1) {
							newCell.setUnit(unitTable[unitType - 1].copy());
							newCell.getUnit().setPosition(newCell);
						}
						
						map[i][j] = newCell;
						scanInts.close();
					}
				}
				scanLines.close();
			} else {
				generateMap();
			}
		
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//make saved maps table for each player
	public void saveMap()
	{
		
	}
	
	public void generateMap(int width, int height)
	{
		long seed = System.nanoTime();
		Random r = new Random(seed);
		mapName = "NewMap" + seed;
		
		if(width > MAXWIDTH) width = MAXWIDTH;
		if(width < MINWIDTH) width = MINWIDTH;
		if(height > MAXHEIGHT) height = MAXHEIGHT;
		if(height < MINHEIGHT) height = MINHEIGHT;
		
		map = new Cell[height][width];
	}
	
	public void generateMap()
	{
		long seed = System.nanoTime();
		Random r = new Random(seed);
		mapName = "NewMap" + seed;
		
		int height = Math.abs(r.nextInt()) % ((MAXHEIGHT + 1) - MINHEIGHT) + MINHEIGHT;
		int width = Math.abs(r.nextInt()) % ((MAXWIDTH + 1) - MINWIDTH) + MINWIDTH;
		
		map = new Cell [height][width];
	}
	
	public Cell[][] getCells()
	{
		return map;
	}
	
	public String getMapName()
	{
		return mapName;
	}
	
	public String toString()
	{
		int i,j, yDim, xDim;
		yDim = map.length;
		xDim = map[yDim - 1].length;
		
		String mapString = "";
		
		mapString += "Terrain:\n";
		for(i = 0; i < yDim; i++){
			for(j = 0; j < xDim; j++){
				String s = map[i][j].getTerrain().getClass().getName();
				s = s.substring(s.indexOf('.') + 1);
				while(s.length() < 9){
					s += ' ';
				}
				mapString += s;
				mapString += " ";
			}
			mapString += "\n";
		}
		
		mapString += "\n";
		mapString += "Structures:\n";
		for(i = 0; i < yDim; i++){
			for(j = 0; j < xDim; j++){
				
				if(map[i][j].hasStructure()){
					String s = map[i][j].getStructure().getClass().getName();
					s = s.substring(s.indexOf('.') + 1);
					if(s.equals("Base")) {
						Base base = (Base) map[i][j].getStructure();
						s += ' ';
						s += base.getOwner().getName().substring(0, 5);
					}
					while(s.length() < 10){
						s += ' ';
					}
					mapString += s;
				} else {
					String s = "0";
					while(s.length() < 10){
						s += ' ';
					}
					mapString += s;
				}

				mapString += " ";
			}
			mapString += "\n";
		}
		
		mapString += "\n";
		mapString += "Units:\n";
		for(i = 0; i < yDim; i++){
			for(j = 0; j < xDim; j++){
				if(map[i][j].hasUnit()){
					String s = map[i][j].getUnit().getClass().getName();
					s = s.substring(s.indexOf('.') + 1);
					while(s.length() < 12){
						s += ' ';
					}
					mapString += s;
				} else {
					String s = "0";
					while(s.length() < 12){
						s += ' ';
					}
					mapString += s;
				}

				mapString += " ";
			}
			mapString += "\n";
		}
		
		mapString += "\n";
		
		return mapString;
	}
}