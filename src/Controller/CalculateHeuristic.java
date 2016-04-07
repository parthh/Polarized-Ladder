package Controller;

import java.awt.Point;

import GameBoard.GameBoard;
import Model.Player;

public class CalculateHeuristic {
	private int[] discAvailableWeight = { 0, 1, 2, 20, 100, 1000 };
	
	public int calculate(Player current, Player opponent, GameBoard b) {
		int finalHeuristic = 0;
		String[][] board = b.getState();
		String playerToken = current.getPlayerToken();
		String opponentToken = opponent.getPlayerToken();
		// analyze every populated board point for potential ladders
		for (int i = 0; i < b.ROW; i++) {
			for (int j = 0; j < b.COLUMN; j++) {
				if ((board[i][j]).equalsIgnoreCase(playerToken)) {
					// add player disc potential ladders
					finalHeuristic += countLadderAndDisc(board, new Point(i, j), playerToken,
							opponentToken,b);
				} else if ((board[i][j]).equalsIgnoreCase(opponentToken)) {
					// subtract opponent's disc potential ladders
					finalHeuristic -= countLadderAndDisc(board, new Point(i, j), opponentToken,
							playerToken,b);
				}
			}
		}

		return finalHeuristic;
	}

	private int countLadderAndDisc(String[][] board, Point movePoint, String player, String opponent,GameBoard bd) {
		int valueOfHeuristic = 0;
		// calculate potential ladders...
		for (int row = 0; row < GameBoard.winningCondition.length; row++) {
			int counter = 0;
			int numOfDisc = 0;
			boolean midPointBlock;
			for (int col = 0; col < GameBoard.winningCondition[0].length; col++) {
				int xPos = movePoint.x + GameBoard.winningCondition[row][col].x;
				int yPos = movePoint.y + GameBoard.winningCondition[row][col].y;
				try {
					if (board[xPos][yPos].equals(player) || board[xPos][yPos].equals("-")) {
						counter++;
				
						if (board[xPos][yPos].equals(player))
							numOfDisc++;
					} else
						break;
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				if (counter == 5) {
					Point[] ladder = new Point[5];
					String ladderDirection = (row > 4) ? "LEFT" : "RIGHT";
					for (int j = 0; j < GameBoard.winningCondition[0].length; j++) {
						ladder[j] = new Point(movePoint.x + GameBoard.winningCondition[row][j].x,
								movePoint.y + GameBoard.winningCondition[row][j].y);
					}
					// If ladder is blocked then don't add its value
					if (!bd.checkBlocked(board, ladder, ladderDirection, opponent)) {
						valueOfHeuristic++;
						valueOfHeuristic = valueOfHeuristic + discAvailableWeight[numOfDisc];
					}
				}
			}
		}
		return valueOfHeuristic;
	}
}
