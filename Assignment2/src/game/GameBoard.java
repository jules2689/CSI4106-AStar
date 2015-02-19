package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

import game.Block.BlockType;

public class GameBoard {
	
	public Block[][] gameBoard;
	public Block goal;
	
	public GameBoard(int size) {
		this.gameBoard = new Block[size][size];
		
		for (int x = 0; x < this.gameBoard.length; x++) {
			for (int y = 0; y < this.gameBoard[x].length; y++) {
				BlockType type = getBlockType(x,y);
				this.gameBoard[y][x] = new Block(this, type, y, x);
				if (type == BlockType.GOAL) {
					goal = this.gameBoard[y][x];
				}
				
			}
		}
	}
	
	public Block getBlock(int x, int y) {
		return this.gameBoard[x][y];
	}
	
	public void printGameBoard(boolean traversal) {		
		for (int x = 0; x < this.gameBoard.length; x++) {
			for (int y = 0; y < this.gameBoard[x].length; y++) {
				Block block = getBlock(y,x); // Reverse x,y because Java is column first traversal
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
	
	public AStarStats aStar(Block startPlayer) {
		resetGameBoard();
		
		System.out.println("Starting search for player at X: " + startPlayer.getX() + " Y: " + startPlayer.getY());
		
		int numVisited = 0;
		Block goalBlock = null;
		
		Queue<Block> frontier = new ArrayDeque<Block>();
		startPlayer.visited = true;
		frontier.add(startPlayer);
		
		WhileLoop: while (!frontier.isEmpty()) {
			Block currentBlock = frontier.poll();
			for (Block neighbour : currentBlock.getNeighbours()) {
				if (!neighbour.visited) {
					neighbour.parentBlock = currentBlock; // Link neighbour to this block
					neighbour.visited = true; // We have now visited the block
					numVisited++;
					
					if (neighbour.blockType == BlockType.GOAL) { // If we reach the goal, break out of everything
						System.out.println("Reached Goal!");
						goalBlock = neighbour;
						break WhileLoop;
					}
					
					frontier.add(neighbour);
				}
			}
		}
		
		// Reverse the Linked List of parentBlocks
		Stack<Block> blockStack = new Stack<Block>();
		blockStack.add(goalBlock);
		while(goalBlock.parentBlock != null) {
			goalBlock = goalBlock.parentBlock;
			blockStack.push(goalBlock);
		}
		
		// Iterate through the stack to print out the path
		int lengthOfPath = blockStack.size();
		while (!blockStack.isEmpty()) {
			Block block = blockStack.pop();
			String msg = block.blockType == BlockType.PLAYER || block.blockType == BlockType.GOAL ? block.blockType + "-> " : "";
			System.out.println(msg + "X: " + block.getX() + " Y: " + block.getY());
		}

		System.out.println("\n\n");
		return new AStarStats(startPlayer, numVisited, lengthOfPath);
		
	}
	
	private BlockType getBlockType(int x, int y) {
		ArrayList<int[]> walls = getWalls();
		boolean isPlayer = (x == this.gameBoard.length -1 || x == 0) && 
						   (y == this.gameBoard.length - 1 || y == 0);
		if (isPlayer) {
			return BlockType.PLAYER;
		} else if (listContainsPosition(walls, new int[]{x,y})) {
			return BlockType.WALL;
		} else if (x == 3 && y == 3) {
			return BlockType.GOAL;
		} else if ( x > 1 && x < 7 && y > 6) {
			return BlockType.VOID;
		} else {
			return BlockType.EMPTY;
		}
	}
	
	// Helper Methods
	private ArrayList<int[]> getWalls() {
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
		if (this.gameBoard.length == 9) {
			walls.add(new int[]{6,5});
		}
		return walls;
	}
	
	private boolean listContainsPosition(ArrayList<int[]> list, int [] array) {
		boolean isContained = false;
		Iterator<int[]> iterator = list.iterator();
		while(iterator.hasNext() && !isContained) {
			int []compare = iterator.next();
			isContained = array[0] == compare[0] && array[1] == compare[1];
		}
		return isContained;
	}

}