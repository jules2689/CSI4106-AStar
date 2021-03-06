package game;

public class AStarStats implements Comparable<AStarStats>
{
	public Block player;
    public int numVisited;
    public int lengthOfPath;
    
    public AStarStats(Block player, int numVisited, int lengthOfPath) {
    	this.player = player;
    	this.numVisited = numVisited;
    	this.lengthOfPath = lengthOfPath;
    }

	@Override
	public int compareTo(AStarStats o) {
		if (this.lengthOfPath > o.lengthOfPath) {
			return 1;
	    } else if (this.lengthOfPath < o.lengthOfPath){
	        return -1;
	    } else {
	        return 0;
	    }
	}
} 