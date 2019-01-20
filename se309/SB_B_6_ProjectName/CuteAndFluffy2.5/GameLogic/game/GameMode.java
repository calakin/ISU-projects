package game;

public class GameMode {
	
	protected int numPlayers;
	
	public void setNumberPlayers(int givenNumPlayers)
	{
		this.numPlayers = givenNumPlayers;
	}
	
	public int getNumberPlayers()
	{
		return this.numPlayers;
	}
}
