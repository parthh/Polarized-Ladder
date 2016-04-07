package Controller;

import java.util.Iterator;

import GameBoard.GameBoard;
import Model.LeafNodes;

public class MiniMaxAlgo {

	GameBoard maxBoard;
	GameBoard minBoard;

	public GameBoard maxMove(LeafNodes n){
		
		int max =  -2147483648;
		int maxtemp =-2147483648;
       
		Iterator<LeafNodes> it = n.getLeafNodes().iterator();
		maxBoard =it.next().getData(); 
		max = maxBoard.getHeuristic();
		//System.out.println("h  from minimax " + maxBoard.getHeuristic());		

		while( it.hasNext() )
		{
            GameBoard	boardTemp = it.next().getData();
			maxtemp =  boardTemp.getHeuristic();
			if(maxtemp != 0){
			if(maxtemp >= max){
				 maxBoard = boardTemp;
				 max = maxtemp;
				 }
		    }
			n.getData().setHeuristic(max);
			maxBoard.setHeuristic(max);
		}
		return maxBoard;
	}

	public GameBoard minMove(LeafNodes n){

		int min =  2147483647;
		int mintemp = 2147483647;
		Iterator<LeafNodes> it = n.getLeafNodes().iterator();
		minBoard =it.next().getData(); 
		min = minBoard.getHeuristic();

		while( it.hasNext() )
		{
			GameBoard	boardTemp = it.next().getData();
			mintemp =  boardTemp.getHeuristic();
			if(mintemp <= min){
				minBoard = boardTemp;
			    min = mintemp;	
			}
			n.getData().setHeuristic(min);
			minBoard.setHeuristic(min);
		}
		return minBoard;

	}

}
