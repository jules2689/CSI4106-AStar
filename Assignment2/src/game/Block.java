package game;

import java.util.ArrayList;

public class Block {
	GameBoard board;
	public BlockType blockType;
	private int x;
	private int y;
	
	boolean visited = false;
	Block parentBlock = null;
	
	public enum BlockType {
	    WALL, GOAL, PLAYER, VOID, EMPTY
	}
	
	public Block(GameBoard board, BlockType blockType, int x, int y) {
		this.board = board;
		this.blockType = blockType;
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public ArrayList<Block> getNeighbours() {
		ArrayList<Block> neighbours = new ArrayList<Block>(4);
		
		// Left
		if (this.x > 0) {
			Block block = this.board.getBlock(this.x - 1, this.y);
			if ((block.blockType == BlockType.WALL || block.blockType == BlockType.VOID) == false) {
				neighbours.add(block);
			}
		}
		
		// Right
		if (this.x < this.board.gameBoard.length - 1) {
			Block block = this.board.getBlock(this.x + 1, this.y);
			if ((block.blockType == BlockType.WALL || block.blockType == BlockType.VOID) == false) {
				neighbours.add(block);
			}
		}
		
		// Up
		if (this.y > 0) {
			Block block = this.board.getBlock(this.x, this.y - 1);
			if ((block.blockType == BlockType.WALL || block.blockType == BlockType.VOID) == false) {
				neighbours.add(block);
			}
		}
		
		// Down
		if (this.y < this.board.gameBoard.length - 1) {
			Block block = this.board.getBlock(this.x, this.y + 1);
			if ((block.blockType == BlockType.WALL || block.blockType == BlockType.VOID) == false) {
				neighbours.add(block);
			}
		}
		
		return neighbours;
	}
	
	public void printBlock(boolean traversal) {
		if (traversal) {
			if (this.visited) {
				System.out.print('V');
			} else {
				System.out.print('x');
			}
		} else {
			printBlockSymbol();
		}
		
	}
	
	private void printBlockSymbol() {
		switch (this.blockType) {
		case WALL:
			System.out.print('☒');
			break;
		case GOAL:
			System.out.print('✮');
			break;
		case PLAYER:
			System.out.print('☑');
			break;
		case EMPTY:
			System.out.print('☐');
			break;
		case VOID:
			System.out.print(' ');
			break;
		}
	}
	
	// Heuristic Cost Methods
	
	// h'(x)
	public int heuristicCost() {
		return verticalDistanceToGoal() + horizontalDistanceToGoal() + numObstaclesInPath();
	}
	
	private int verticalDistanceToGoal() {
		return Math.abs(board.goal.y - this.y);
	}
	
	private int horizontalDistanceToGoal() {
		return Math.abs(board.goal.x - this.x);
	}
	
	private int numObstaclesInPath() {
		int obstacles = 0;
		
		int startY = board.goal.y > this.y ? this.y : board.goal.y;
		int endY = board.goal.y > this.y ? board.goal.y : this.y;
		for (int y = startY; y < endY; y++) {
			BlockType type = board.getBlock(this.x, y).blockType;
			if (type == BlockType.WALL) {
				obstacles++;
			}
		}
		
		int startX = board.goal.x > this.x ? this.x : board.goal.x;
		int endX = board.goal.x > this.x ? board.goal.x : this.x;
		for (int x = startX; x < endX; x++) {
			BlockType type = board.getBlock(x, board.goal.y).blockType;
			if (type == BlockType.WALL) {
				obstacles++;
			}
		}
		
		return obstacles;
	}

}
