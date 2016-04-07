package GameBoard;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;



public class GameBoard {
	public  final int ROW = 8;
	public final int COLUMN = 14;
	public HashMap<Point, String> openPointList;
	String[][] boardData = null;
	public int numberOfEmptySpace = 0;
	public Point lastPosition = null;
	int heuristic;
	
	/*
	 *  Winning strategy pattern
	 */
	public static Point winningCondition[][] = {
			{ new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(2, -1), new Point(2, -2) },
			{ new Point(-1, 0), new Point(0, 0), new Point(0, -1), new Point(1, -1), new Point(1, -2) },
			{ new Point(-1, 1), new Point(0, 1), new Point(0, 0), new Point(1, 0), new Point(1, -1) },
			{ new Point(-2, 1), new Point(-1, 1), new Point(-1, 0), new Point(0, 0), new Point(0, -1) },
			{ new Point(-2, 2), new Point(-1, 2), new Point(-1, 1), new Point(0, 1), new Point(0, 0) },
			
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
			{ new Point(-1, 0), new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(-1, -1), new Point(0, -1), new Point(0, 0), new Point(1, 0), new Point(1, 1) },
			{ new Point(-2, -1), new Point(-1, -1), new Point(-1, 0), new Point(0, 0), new Point(0, 1) },
			{ new Point(-2, -2), new Point(-1, -2), new Point(-1, -1), new Point(0, -1), new Point(0, 0) }, };

	/*
	 *  rightLadderBlock and leftLadderBlok are two point array will use to check blocking
	 *  condition of the detected ladder based on that direction
	 */
	public static Point rightLadderBlock[][] = {
			{ new Point(2, 0), new Point(0, -2) }};

	public static Point leftLadderBlock[][] = {
			{ new Point(2, 0), new Point(0, 2) }};

	public GameBoard() {
		boardData = new String[ROW][COLUMN];
		openPointList = new HashMap<Point, String>();
		lastPosition = new Point();
		createGameBoard();
		createPointList();
	}

	/*
	 * createGameBoard method will first create game board using two dimension string array
	 */
	private void createGameBoard() {

		int counterC = 0;
		while (counterC < COLUMN) {
			int counterR = 7;
			while (counterR >= 0) {
				if (counterR == ROW - 1) {
					if (counterC != 0) {
						boardData[counterR][counterC] = String.valueOf((char) (counterC + 64));
					} else {
						boardData[counterR][counterC] = " ";
					}
				} else {
					if (counterR >= Math.abs((ROW - counterC - 1))) {
						boardData[counterR][counterC] = "-";
					} else {
						if (counterR == ROW - counterC - 2) {
							boardData[counterR][counterC] = (counterC + 1) + "";
						} else {
							boardData[counterR][counterC] = " ";
						}
					}
				}
				counterR--;
			}
			counterC++;
		}
	}

	/*
	 * createPointList will create hashtable of point and token on that point
	 */
	public void createPointList() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUMN; j++) {
				if (boardData[i][j].equals("-")) {
					Point p = new Point(i, j);
					openPointList.put(p, "open");
					numberOfEmptySpace++;
				}
			}
		}
		
	}

	/*
	 * printBoard method will print total empty spaces in board and 
	 * whole configuration of board at current time
	 */
	public void printBoard() {
		System.out.println("Total Empty space : "+ numberOfEmptySpace);
		System.out.println();
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUMN; j++) {
				System.out.print(boardData[i][j]);
				System.out.print("  ");
			}
			System.out.println();
		}
	}
	
	/*
	 * isValidPos will check current point is one of the valid point on board or not 
	 */
	private boolean isValidPos(int i, int j) {
		try {
			if (boardData[i][j].equalsIgnoreCase("-")) {
				return true;
			} else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException aiob) {
			return false;
		}
	}

	/*
	 *  isBoardFull methos will check is there any emptyspace on board or not
	 */
	public boolean isBoardFull() {
		if (numberOfEmptySpace == 0) {
			return true;
		} else {
			return false;
		}
	}
	/*
	 * setPosition will check whether current point is valid for the player to put
	 * their own token on that position or not.
	 */
	public boolean setPosition(int i, int j, String mark) {
		i = ROW - i - 1;
		if (isValidPos(i, j)) {
			lastPosition = new Point(i, j);
			boardData[i][j] = mark;
			openPointList.remove(new Point(i, j));
			numberOfEmptySpace--;
			return true;
		} else {

			return false;
		}
	}
	/*
	 *  chechLadder method will identify by the last point player put whether Ladder is
	 *  formed or not by using the above winning condition
	 */
	public boolean checkLadder(String playerToken, String opposingPlayerToken, Point currPoint) {
		currPoint.x = ROW - currPoint.x - 1;
		for (int i = 0; i < winningCondition.length; i++) {
			int confirmedPoints = 0;
			for (int j = 0; j < winningCondition[0].length; j++) {
				int xPos = currPoint.x + winningCondition[i][j].x;
				int yPos = currPoint.y + winningCondition[i][j].y;
				try{
				if (boardData[xPos][yPos].equalsIgnoreCase(playerToken)) {
					confirmedPoints++;
				}
				else {
					confirmedPoints = 0;
					break;
				}
				}catch( ArrayIndexOutOfBoundsException e){
					break;
				}
			}

			if (confirmedPoints == 5) {
				Point[] ladder = new Point[5];
				String ladderDirection = (i > 4) ? "LEFT" : "RIGHT";
				for (int j = 0; j < winningCondition[0].length; j++) {
					ladder[j] = new Point(currPoint.x + winningCondition[i][j].x,
							currPoint.y + winningCondition[i][j].y);
				}
				if (!checkBlocked(boardData,ladder, ladderDirection, opposingPlayerToken)) {
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * checkBlocked method will check the ladder points in blocked by opponent or not
	 */
	public boolean checkBlocked(String[][] boardState,Point[] ladder, String direction, String opposingPlayerToken) {

		if (direction.equalsIgnoreCase("LEFT")) {
				Point p1 = new Point(Math.abs(ladder[0].x + leftLadderBlock[0][0].x),
						Math.abs(ladder[0].y + leftLadderBlock[0][0].y));
				Point p2 = new Point(Math.abs(ladder[0].x + leftLadderBlock[0][1].x),
						Math.abs(ladder[0].y + leftLadderBlock[0][1].y));
				
				if ((boardState[p1.x][p1.y].equalsIgnoreCase(opposingPlayerToken))
						&& (boardState[p2.x][p2.y].equalsIgnoreCase(opposingPlayerToken))) {
//					System.out.println("Left Ladder point (" + (ladder[2].x - 1) + ","
//							+ (String.valueOf((char) (ladder[2].y + 64))) + ") Blocked");
					return true;
				}
			
		} else if (direction.equalsIgnoreCase("RIGHT")) {
				Point p1 = new Point(Math.abs(ladder[0].x + rightLadderBlock[0][0].x),
						Math.abs(ladder[0].y + rightLadderBlock[0][0].y));
				Point p2 = new Point(Math.abs(ladder[0].x + rightLadderBlock[0][1].x),
						Math.abs(ladder[0].y + rightLadderBlock[0][1].y));
				
				if ((boardState[p1.x][p1.y].equalsIgnoreCase(opposingPlayerToken))
						&& (boardState[p2.x][p2.y].equalsIgnoreCase(opposingPlayerToken))) {
//					System.out.println("Right Ladder point (" + (ladder[2].x - 1) + ","
//							+ (String.valueOf((char) (ladder[2].y + 64))) + ") Blocked");
					return true;
				}
		}

		return false;
	}
	
	public Iterator<Point> getOpenPointIterator()
	{
		return openPointList.keySet().iterator();
	}
	
	public String[][] getState()
	{	
		return boardData.clone();
	}
	
	public void setState(String[][] board)
	{
		this.boardData = board;
	}
	
	public String[][] cloneArray() {
	    int length = boardData.length;
	    String[][] target = new String[length][boardData[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(boardData[i], 0, target[i], 0, boardData[i].length);
	    }
	    return target;
	}
	
	public boolean setTempAIPosition(int i, int j, String mark) {
		if (isValidPos(i, j)) {
			lastPosition = new Point(i, j);
			boardData[i][j] = mark;
			numberOfEmptySpace--;
			return true;
		} else {

			return false;
		}
	}
	
	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
}
