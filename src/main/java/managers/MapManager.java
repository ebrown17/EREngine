package managers;

import java.util.ArrayList;
import java.util.HashMap;

import managers.EntityManager;
import maps.mazes.RecursiveMaze;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;

public class MapManager {
	
	/*
	 * String: Map type key
	 * Inner HashMap: seed key 
	 */
	private HashMap<String,HashMap<String,MapData>> mapCache;	
	private HashMap<String,MapData> mazeStores;
	
	private EntityManager em;
	
	public MapManager(EntityManager em){
		this.em=em;
		mapCache = new HashMap<String,HashMap<String,MapData>>();
		generateMazeCache();
	}
	
	private void generateMazeCache(){
		mazeStores = new HashMap<String,MapData>();
		mapCache.put("MAZE", mazeStores);
	}

	/**
	 * Stores the width, height, and seed in a hashmap. Map data can later be
	 * retrieved by using this same seed. If the seed has already been used to 
	 * create maze data, this will silently be skipped and old data will be kept
	 * intact.
	 * 
	 * @param	scaledWidth
	 * 			needs to be the scaled width 
	 * 			<br><i>scaledWidth = (width of maze in pixels) / tilesize </i>
	 * 
	 * @param 	scaledHeight 
	 * 			needs to be the scaled height 
	 * 			<br><i>scaledHeight = (height of maze in pixels) / tilesize </i>
	 * 
	 * @param	seed 
	 * 			used to generate a unique maze and as key for retrieving <i>Maze Data</i> 
	 */
	public void createMazeData(int scaledWidth,int scaledHeight,String seed){
		HashMap<String,MapData> mazeStore = mapCache.get("MAZE");
		if(mazeStore.get(seed) == null){
			mazeStore.put(seed,new MapData(scaledWidth,scaledHeight,seed));
		}		
	}
	/**
	 * Generates a maze using the algorithm in <i>maps.mazes.RecursiveMaze</i>
	 * <p>The seed is required to have already been used to generate a 
	 * <i>Maze Data</i> object.  Will return null if seed is unused.
	 * 
	 * @param	seed
	 * 			key for retrieving previously created <i>Maze Data</i> object
	 * 
	 * @return	a <code>GridGraph</code> whose nodes have been generated and arranged to form a recursive maze.
	 * 			<code>null</code> if seed was not used to create a <i>Maze Data</i> object
	 */
	public GridGraph generateRecMaze(String seed){
		HashMap<String,MapData> mazeStore = mapCache.get("MAZE");
		if(mazeStore.get(seed) == null){
			return null;
		}
		MapData map = mazeStore.get(seed);
		RecursiveMaze maze = new RecursiveMaze(map.getWidth(),map.getHeight(),map.getSeed());		
		return maze;		
	}

	
	private class MapData {
		
		private int width, height;
		private String seed;
		
		private MapData(int width,int height,String seed){
			this.width=width;
			this.height=height;
			this.seed=seed;
		}

		public int getWidth() {
			return width;
		}
		
		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getSeed() {
			return seed;
		}

		public void setSeed(String seed) {
			this.seed = seed;
		}
	
	}
	
}
