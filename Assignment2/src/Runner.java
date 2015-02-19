import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.AStarStats;
import game.GameBoard;

public class Runner {

	public static void main(String[] args) {
		GameBoard gameBoard = new GameBoard(9);
		gameBoard.printGameBoard(false);
		
		int size = gameBoard.gameBoard.length - 1;
	
		ArrayList<AStarStats> stats = new ArrayList<AStarStats>(4);
		stats.add(gameBoard.aStar(gameBoard.getBlock(0, 0)));
		stats.add(gameBoard.aStar(gameBoard.getBlock(0, size)));
		stats.add(gameBoard.aStar(gameBoard.getBlock(size, 0)));
		stats.add(gameBoard.aStar(gameBoard.getBlock(size, size)));
		declareWinner(stats);
	}
	
	public static void declareWinner(List<AStarStats> stats) {
		Collections.sort(stats);
		for (AStarStats stat : stats) {
			int pos = stats.indexOf(stat);
			if (pos == 0) {
				System.out.println("The Winner was player at X: " + stat.player.getX() + " Y: " + stat.player.getY());
			} else {
				System.out.println("The player in position " + (pos + 1) + " at X: " + stat.player.getX() + " Y: " + stat.player.getY());
			}
			System.out.println("Visited " + stat.numVisited + " blocks");
			System.out.println("Length of Path was " + stat.lengthOfPath + "\n\n");
			
		}
	}

}