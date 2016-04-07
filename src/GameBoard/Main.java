package GameBoard;

import java.util.Scanner;
import Controller.*;
import Model.*;

public class Main {

	static Player[] players;
	
	public static void main(String args[]) {
		players = new Player[2];
		GameBoard board = new GameBoard();
		Scanner scn  = new Scanner(System.in);
		System.out.println("-------Polarized Ladder Game--------");
		System.out.println("1: Manual Game");
		System.out.println("2: Player vs AI");
		System.out.println("3: AI vs Player");
		int gameMode = scn.nextInt();
		String player2 = gameMode==1?"Second":"Computer";
		boolean isAI = gameMode==1?false:true;
		Scanner scn1  = new Scanner(System.in);
		System.out.println("Enter the name of First Player");
		String firstName = scn1.nextLine();
		System.out.println("Enter the Token of First Player");
		String firstToken = scn1.nextLine();
		System.out.println("Enter the name of "+player2 +" player");
		String secondName = scn1.nextLine();
		System.out.println("Enter the Token of "+player2 +" player");
		String secondToken = scn1.nextLine();
		players[0] = new Player(firstName, firstToken,0,isAI);
		players[1] = new Player(secondName, secondToken,1,isAI);
		PlayGameController play = new PlayGameController();
		play.startGame(board, players, gameMode);
	}
}
