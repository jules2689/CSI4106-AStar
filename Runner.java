import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

import game.AStarStats;
import game.Block;
import game.GameBoard;
import game.Block.BlockType;

public class Runner {

	public static void main(String[] args) {
		System.out.println("Assignment 2, CSI4106");
		System.out.println("Julian Nadeau, 6008161");
		System.out.println("=============================================================");
		System.out.println("It is recommended to zoom into see the board output more easily.");
		
		int gameBoardSizeChoice = getGameBoardSize();
		ArrayList<int[]> playerPos = getPlayerPositions(gameBoardSizeChoice - 1);

		GameBoard gameBoard = new GameBoard(gameBoardSizeChoice, getShouldOutputPath(), playerPos);
		
		Block.printBlockSymbolMeanings();
		System.out.println("\n\n");
		
		System.out.println("Here is the gameboard:\n");
		gameBoard.printGameBoard(false);
		
		System.out.println("Here are the races for each player:\n");
		int size = gameBoard.gameBoard.length - 1;
		ArrayList<AStarStats> stats = new ArrayList<AStarStats>(playerPos.size());
		for (int []pos : playerPos) {
			stats.add(gameBoard.aStar(gameBoard.getBlock(pos[0], pos[1])));
		}
		System.out.println("Here are the race results:\n");
		declareWinnerAndStats(stats);
	}

	// Declare Winner and Stats
	
	public static void declareWinnerAndStats(List<AStarStats> stats) {
		Collections.sort(stats);
		for (AStarStats stat : stats) {
			int pos = stats.indexOf(stat);
			if (pos == 0) {
				System.out.println("The Winner was player at X: " + stat.player.getX() + " Y: " + stat.player.getY());
			} else {
				System.out.println("The player in position " + (pos + 1) + " at X: " + stat.player.getX() + " Y: " + stat.player.getY());
			}
			System.out.println("Visited " + stat.numVisited + " blocks");
			System.out.println("Length of Path was " + stat.lengthOfPath);
			System.out.println("\n");
		}
	}

	// Ask for the size of the game board
	
	private static int getGameBoardSize() {
		String choice = "N/A";
		String message = "Which gameboard do you want to use? The (S)mall board, or the (L)arge board with the extra wall? ";
		
		try {
			while(!choice.equalsIgnoreCase("S") && !choice.equalsIgnoreCase("L")) { choice = readLine(message); }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (choice.equalsIgnoreCase("s")) {
			return 7;
		} else {
			return 9;
		}
	}

	// Get Player Positions

	private static ArrayList<int[]> getPlayerPositions(int size) {
		String message = "How many players do you want? ";
		int numPlayers = getNumFromConsole(message, 10);
		ArrayList<int[]> pos = new ArrayList<int[]>(numPlayers);

		for (int i = 0; i < numPlayers; i++) {
			message = "What is the X position for player " + i + "? Max: " + size + " :: ";
			int x = getNumFromConsole(message, size);

			message = "What is the Y position for player " + i + "? Max: " + size + " :: ";
			int y = getNumFromConsole(message, size);

			int []position = new int[]{x,y};
			if (GameBoard.getBlockType(x,y,pos,size) == BlockType.EMPTY) {
				pos.add(position);
			} else {
				System.out.println("You can only place your player on an empty square.");
				i--; // Redo this one
			}
		}

		return pos;
	}

	// Ask if we want to show the traversal path
	
	private static boolean getShouldOutputPath() {
		String choice = "N/A";
		String message = "Do you want to output the path as we traverse? y/n ";
		
		try {
			while(!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) { choice = readLine(message); }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (choice.equalsIgnoreCase("y")) {
			return true;
		} else {
			return false;
		}
	}

	// Helpers
	
	private static String readLine(String format, Object... args) throws IOException {
	    if (System.console() != null) { return System.console().readLine(format, args); }
	    System.out.print(String.format(format, args));
	    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    return reader.readLine();
	}

	private static int getNumFromConsole(String message, int max)
	{
		String choice = "N/A";
		try {
			while(!isNumericWithinRange(choice, max)) { choice = readLine(message); }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(choice);
	}

	private static boolean isNumericWithinRange(String str, int max)  
	{  
		int d = Integer.MAX_VALUE;

	  try  
	  {  
	    d = Integer.parseInt(str); 
	    
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  } 

	  return d <= max;  
	}

	public static boolean listContainsPosition(ArrayList<int[]> list, int [] array) {
		boolean isContained = false;
		Iterator<int[]> iterator = list.iterator();
		while(iterator.hasNext() && !isContained) {
			int []compare = iterator.next();
			isContained = array[0] == compare[0] && array[1] == compare[1];
		}
		return isContained;
	}

}
