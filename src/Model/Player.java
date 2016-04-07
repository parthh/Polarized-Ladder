package Model;

import java.io.Serializable;

import GameBoard.GameBoard;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String playerName;	
	protected String playerToken;
	protected int number;
	protected boolean isAIplayer;
	protected GameBoard AIplayerBoard;
	public Player(String playerName, String playerToken,int number,boolean isAI) {
		
		this.playerName 	= playerName;
		this.playerToken 	= playerToken;
		this.number 		= number;
		this.isAIplayer		= isAI;
		if(isAI){
			AIplayerBoard = new GameBoard();
		}
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public String getPlayerToken() {
		return playerToken;
	}
	public int getPlayerNumber() {
		return number;
	}
	public boolean isAIPlayer(){
		return isAIplayer;
	}
	public GameBoard getAIplayerBoard(){
		return AIplayerBoard;
	}
	
	
}
