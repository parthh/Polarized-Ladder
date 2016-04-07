package Controller;

import java.awt.Point;
import java.util.Iterator;
import java.util.Scanner;

import GameBoard.GameBoard;
import Model.LeafNodes;
import Model.Player;

public class PlayGameController {
	private boolean gameOver;
	private int playerTurn;
	private Player[] players;
	private int maxDepth;
	private CalculateHeuristic heuristics;
	private GameBoard aiPLayerBoard = null;
	private MiniMaxAlgo minmax;

	public PlayGameController() {
		heuristics = new CalculateHeuristic();
		minmax = new MiniMaxAlgo();
	}

	public void startGame(GameBoard board, Player[] players, int gameType) {
		this.players = players;
		this.gameOver = false;
		this.playerTurn = gameType == 3 ? 1 : 0;
		if (gameType == 1)
			manualGame(board, players);
		if (gameType == 2)
			playerVsAI(board, players);
		if (gameType == 3)
			aIvsPlayer(board, players);
	}

	/*
	 * manualGame is first step for manual player game to moveforward
	 */
	public void manualGame(GameBoard board, Player[] players) {

		while (!gameOver) {
			board.printBoard();
			if (board.isBoardFull()) {
				System.out.printf("The Game Finish It's a draw!\n");
				gameOver = true;
			} else {
				manualMove(players, board, playerTurn);
			}
		}
		System.out.printf("Game over!\n");
	}

	private void playerVsAI(GameBoard board, Player[] players) {

		while (!gameOver) {
			board.printBoard(); // display board state
			// TODO: add win condition check
			if (board.isBoardFull()) { // check win condition or draw
				System.out.printf("The game ended in a draw!\n");
				gameOver = true;
			} else {

				if (playerTurn == 0) { // player turn
//					long tStart = System.currentTimeMillis();
//					aiMove(players, board, playerTurn);
//					long tEnd = System.currentTimeMillis(); // obtain end time
//					System.out.printf("AI Move Duration = %.2f %s\n", (float) (tEnd - tStart) / 1000, "seconds");
					manualMove(players, board, playerTurn);
				} else {
					long tStart = System.currentTimeMillis();
					aiMove(players, board, playerTurn);
					long tEnd = System.currentTimeMillis(); // obtain end time
					System.out.printf("AI Move Duration = %.2f %s\n", (float) (tEnd - tStart) / 1000, "seconds");
				}
			}
		}

		System.out.printf("Game over!\n");
	}

	private void aIvsPlayer(GameBoard board, Player[] players) {

		while (!gameOver) {
			board.printBoard(); // display board state
			// TODO: add win condition check
			if (board.isBoardFull()) { // check win condition or draw
				System.out.printf("The game ended in a draw!\n");
				gameOver = true;
			} else {
				if (playerTurn == 1) {
					long tStart = System.currentTimeMillis();
					aiMove(players, board, playerTurn);
					long tEnd = System.currentTimeMillis(); // obtain end time
					System.out.printf("AI Move Duration = %.2f %s\n", (float) (tEnd - tStart) / 1000, "seconds");
				} else {
					manualMove(players, board, playerTurn);
				}
			}
		}

		System.out.printf("Game over!\n");
	}

	/*
	 * nextPLayerMove method will choose the next move on board with all
	 * required condition
	 */
	private void manualMove(Player[] players, GameBoard board, int Turn) {
		int opponent = (Turn == 0) ? 1 : 0;
		Point newPosition = new Point();
		while (!board.setPosition(newPosition.x, newPosition.y, players[Turn].getPlayerToken())) {
			newPosition = playerInput(players[Turn]);
		}
		if (board.checkLadder(players[Turn].getPlayerToken(), players[opponent].getPlayerToken(), newPosition)) {
			board.printBoard();
			System.out.printf(players[Turn].getPlayerName() + " Wins the Game! \n");
			gameOver = true;
		}
		playerTurn = opponent;
	}

	private void aiMove(Player[] players, GameBoard board, int Turn) {
		int opponent = (Turn == 0) ? 1 : 0;
		aiPLayerBoard = players[Turn].getAIplayerBoard();
		Point AIPlayerMove = doAIPlayerTurn(players[Turn], players[opponent], board);
		AIPlayerMove.x = board.ROW - AIPlayerMove.x - 1;
		while(!board.setPosition(AIPlayerMove.x, AIPlayerMove.y, players[Turn].getPlayerToken())) {
			doAIPlayerTurn(players[Turn], players[opponent], board);
		}
		if (board.checkLadder(players[Turn].getPlayerToken(), players[opponent].getPlayerToken(), AIPlayerMove)) {
			board.printBoard();
			System.out.printf(players[Turn].getPlayerName() + " Wins the Game! \n");
			gameOver = true;
		}
		playerTurn = opponent;
		// return AIPlayerMove;
	}

	public Point doAIPlayerTurn(Player aiPLayer, Player opponent, GameBoard oldBoard) {

		Point aiPlayerNewPointOnBoard;
		try {
			aiPlayerNewPointOnBoard = calculateDepth(oldBoard, aiPLayer, opponent);
			return aiPlayerNewPointOnBoard;
		} catch (ArrayIndexOutOfBoundsException aiob) {
			System.out.println("Invalid move.");
			return playerInput(aiPLayer);
		} catch (NumberFormatException nf) {
			System.out.println("Invalid move.");
			return playerInput(aiPLayer);
		}

	}

	private Point calculateDepth(GameBoard board, Player aiPLayer, Player Opponent) {
		// local variables
		int initialDepth = 1;
		int emptySpaces = board.numberOfEmptySpace;
		if (emptySpaces > 35) {
			maxDepth = 2;
			System.out.println("Depth : " + maxDepth);
		} else if (emptySpaces > 10) {
			maxDepth = 3;
			System.out.println("Depth : " + maxDepth);
		} else if (emptySpaces > 4) {
			maxDepth = 4;
			System.out.println("Depth : " + maxDepth);
		} else {
			// decrease by depth each time??
			maxDepth = 1;
			System.out.println("Depth : " + maxDepth);
		}
		// create new tree with board
		LeafNodes root = new LeafNodes(board);
		// generate all potential next moves
		genratePossibleSpace(root.getRoot(), initialDepth, aiPLayer.getPlayerToken(), board);

		return aiPLayerBoard.lastPosition;

	}

	public void genratePossibleSpace(LeafNodes parentNode, int depthOfTree, String currPlayer,
			GameBoard oldState) {
		// generate all potential next moves
		Iterator<Point> openPoints = oldState.getOpenPointIterator();
		int opponent = (playerTurn == 0) ? 1 : 0;
		// and shared with sub-trees.
		if (depthOfTree == maxDepth) {
			while (openPoints.hasNext()) {
				GameBoard newBoard = new GameBoard();
				newBoard.setState(oldState.cloneArray());
				Point nextPoint = openPoints.next();
				if (newBoard.setTempAIPosition(nextPoint.x, nextPoint.y,currPlayer)) {
					newBoard.setHeuristic(heuristics.calculate(players[playerTurn], players[opponent], newBoard));
					LeafNodes nextChild = new LeafNodes();
					nextChild.setParent(parentNode);
					nextChild.setData(newBoard);
					parentNode.addLeafs(nextChild);
				}
			}
			startMinMax(parentNode, depthOfTree);
		}

		else {
			while (openPoints.hasNext()) {
				// prepare new board
				GameBoard newBoard = new GameBoard();
				newBoard.setState(oldState.cloneArray());
				// generate next move
				Point nextPoint = openPoints.next();
				if (newBoard.setTempAIPosition(nextPoint.x, nextPoint.y,currPlayer)) {
					// newBoard.printBoard();
					// add next move child node to tree
					LeafNodes nextChild = new LeafNodes();
					nextChild.setParent(parentNode);
					nextChild.setData(newBoard);
					parentNode.addLeafs(nextChild);
				}
			}
			for (LeafNodes childNode : parentNode.getLeafNodes()) {
				// populate child sub-tree
				String nextToken = (currPlayer == players[playerTurn].getPlayerToken())
						? players[opponent].getPlayerToken() : players[playerTurn].getPlayerToken();
				genratePossibleSpace(childNode, depthOfTree + 1, nextToken, childNode.getData());
			}
		}
	}

	public void startMinMax(LeafNodes parent, int depth) {
		GameBoard maxBoard=null;
		GameBoard minBoard=null;
		if (depth == 1) {
			maxBoard = minmax.maxMove(parent);
			parent.setData(maxBoard);
			aiPLayerBoard = maxBoard;
			return;
		}
		if (depth % 2 == 1) {
			maxBoard = minmax.maxMove(parent);
			parent.setHeuristic(maxBoard.getHeuristic());
			startMinMax(parent.getParent(), depth - 1);
		} else if (depth % 2 == 0) {
			minBoard = minmax.minMove(parent);
			parent.setHeuristic(minBoard.getHeuristic());
			startMinMax(parent.getParent(), depth - 1);
		}
	}

	/*
	 * playerInput is method to take input from the player to choose there next
	 * move on board
	 */
	public Point playerInput(Player player) {

		System.out.printf("Please enter your next move " + player.getPlayerName() + " (ex. "
				+ (player.getPlayerNumber() + 1) + ",A):");
		Scanner input = new Scanner(System.in);
		try {
			String playerMove = input.nextLine();
			String rowTemp[] = playerMove.split(",");
			int row = Integer.parseInt(rowTemp[0]);
			int col = Character.getNumericValue(rowTemp[1].toUpperCase().charAt(0)) - 9;
			Point newPoint = new Point(row, col);
			return newPoint;
		} catch (Exception aiob) {
			System.out.println("This is invalid move");
			return playerInput(player);
		}
	}
}
