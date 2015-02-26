package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;

import game.Block.BlockType;

public class GameBoard {
	
	public Block[][] gameBoard;
	public Block goal;
	private boolean outputPath;
	private ArrayList<int[]> playerPos;
	
	public GameBoard(int size, boolean outputPath, ArrayList<int[]> playerPos) {
		this.gameBoard = new Block[size][size];
		this.outputPath = outputPath;
		this.playerPos = playerPos;
		
		for (int x = 0; x < this.gameBoard.length; x++) {
			for (int y = 0; y < this.gameBoard[x].length; y++) {
				BlockType type = GameBoard.getBlockType(x, y, this.playerPos, size);
				this.gameBoard[y][x] = new Block(this, type, x, y);
				if (type == BlockType.GOAL) {
					goal = this.gameBoard[y][x];
				}
				
			}
		}
	}
	
	public Block getBlock(int x, int y) {
		return this.gameBoard[y][x];  // Reverse x,y because Java is column first traversal
	}
	
	// Gameboard Helpers
	
	public void printGameBoard(boolean traversal) {		
		for (int x = 0; x < this.gameBoard.length; x++) {
			for (int y = 0; y < this.gameBoard[x].length; y++) {
				Block block = getBlock(x,y);
				block.printBlock(traversal);
			}
			System.out.print('\n');
		}
		System.out.println("\n\n");
	}
	
	public void resetGameBoard() {
		for (int x = 0; x < this.gameBoard.length; x++) {
			for (int y = 0; y < this.gameBoard[x].length; y++) {
				Block block =  getBlock(x,y);
				block.visited = false;
				block.parentBlock = null;
			}
		}
	}
	
	// A* Algorithm
	
	public AStarStats aStar(Block startPlayer) {
		resetGameBoard();
		
		System.out.println("Starting search for player at X: " + startPlayer.getX() + " Y: " + startPlayer.getY());
		
		int numVisited = 0;
		Block goalBlock = null;
		
		ArrayList<Block> visited = new ArrayList<Block>();
		
		PriorityQueue<Block> frontier = new PriorityQueue<Block>(20, getBlockComparator());
		startPlayer.visited = true;
		frontier.add(startPlayer);
		
		HashMap<Block, Integer> fScores = new HashMap<Block, Integer>();
		fScores.put(startPlayer, startPlayer.pathCost + startPlayer.heuristicCost());

		while (!frontier.isEmpty()) {
			Block currentBlock = frontier.poll();
			
			if (outputPath) {
				currentBlock.visiting = true;
				printGameBoard(true);
			}
			
			if (currentBlock.blockType == BlockType.GOAL) {
				System.out.println("Reached goal after visiting " + numVisited + " blocks.");
				goalBlock = currentBlock;
				break;
			}
			
			visited.add(currentBlock);
			
			for (Block neighbour : currentBlock.getNeighbours()) {
				if (visited.contains(neighbour)) { continue; }
				int tentativeScore = currentBlock.pathCost + 1;
						
				if (!neighbour.visited || tentativeScore < neighbour.pathCost) {
					neighbour.parentBlock = currentBlock; // Link neighbour to this block
					neighbour.pathCost = tentativeScore;
					fScores.put(neighbour, tentativeScore + neighbour.heuristicCost());
					
					neighbour.visited = true; // We have now visited the block
					numVisited++;
					frontier.add(neighbour);
				}
			}
			
			currentBlock.visiting = false;
		}
		

		return reconstuctPath(goalBlock, startPlayer, numVisited);
	}
	
	// Comparator for Priority Queue
	
	private Comparator<Block> getBlockComparator() {
		Comparator<Block> blockComparator = new Comparator<Block>() {
	        @Override
	        public int compare(Block e1, Block e2) {
	        	return e1.heuristicCost() - e2.heuristicCost();
	        }
	    };
	    return blockComparator;
	}
	
	// Path Reconstruction
	
	private AStarStats reconstuctPath(Block goalBlock, Block startPlayer, int numVisited) {
		// Reverse the Linked List of parentBlocks
		Stack<Block> blockStack = new Stack<Block>();
		blockStack.add(goalBlock);
		while(goalBlock.parentBlock != null) {
			goalBlock = goalBlock.parentBlock;
			blockStack.push(goalBlock);
		}
		
		// Iterate through the stack to print out the path
		int lengthOfPath = blockStack.size();
		System.out.println("The length of the actual path was " + lengthOfPath + ".");
		System.out.println("Here is the series of moves the player made:");
		while (!blockStack.isEmpty()) {
			Block block = blockStack.pop();
			String msg = block.blockType == BlockType.PLAYER || block.blockType == BlockType.GOAL ? block.blockType + "-> " : "Moved to: ";
			System.out.println(msg + "X: " + block.getX() + " Y: " + block.getY());
		}

		System.out.println("\n\n");
		return new AStarStats(startPlayer, numVisited, lengthOfPath);
	}
	
	// Gameboard Building Helpers
	
	public static BlockType getBlockType(int x, int y, ArrayList<int[]>playerPos, int size) {
		ArrayList<int[]> walls = GameBoard.getWalls(size);

		if (GameBoard.listContainsPosition(playerPos, new int[]{x,y})) {
			return BlockType.PLAYER;
		} else if (GameBoard.listContainsPosition(walls, new int[]{x,y})) {
			return BlockType.WALL;
		} else if (x == 3 && y == 3) {
			return BlockType.GOAL;
		} else if ( x > 1 && x < 7 && y > 6) {
			return BlockType.VOID;
		} else {
			return BlockType.EMPTY;
		}
	}
	
	public static ArrayList<int[]> getWalls(int size) {
		ArrayList<int[]> walls = new ArrayList<int[]>();
		walls.add(new int[]{0,3});
		walls.add(new int[]{1,1});
		walls.add(new int[]{1,4});
		walls.add(new int[]{2,1});
		walls.add(new int[]{2,4});
		walls.add(new int[]{3,1});
		walls.add(new int[]{3,2});
		walls.add(new int[]{3,4});
		walls.add(new int[]{4,2});
		walls.add(new int[]{5,2});
		walls.add(new int[]{5,3});
		walls.add(new int[]{5,4});
		if (size == 9) {
			walls.add(new int[]{6,5});
		}
		return walls;
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
