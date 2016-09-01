package managers;

import java.util.ArrayList;
import java.util.HashMap;

import managers.EntityManager;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;

public class WorldManager {
	
	private HashMap<String,WorldData> worldMaps;
	private final int TILESIZE;
	private EntityManager em;
	
	public WorldManager(int tileSize,EntityManager em){
		worldMaps = new HashMap<String,WorldData>();
		this.TILESIZE=tileSize;
		this.em=em;
	}
	
	public void generateWorld(int width,int height,String seed){
		int row = width/TILESIZE;
		int column = height/TILESIZE;
		worldMaps.put(seed, new WorldData(row,column,seed));
		ArrayList<GridNode> test =	new GridGraph(row,column).getNodeList();
		
	}
	
	private class WorldData {
		
		private int width, height;
		private String seed;
		
		protected WorldData(int width,int height,String seed){
			this.width=width;
			this.height=height;
			this.seed=seed;
		}
	
	}
	
}
