package managers;

import java.util.HashMap;

import graphs.types.GridGraph;
import maps.mazes.RecursiveMaze;

public class MapManager {
	
	/*
	 * String: Map type key
	 * Inner HashMap: seed key 
	 */
	private HashMap<String,HashMap<Long,MapData>> mapCache;	
	private HashMap<Long,MapData> mazeStores;
	
	public MapManager(){
			
		mapCache = new HashMap<String,HashMap<Long,MapData>>();
		generateMazeCache();
	}
	
	private void generateMazeCache(){
		mazeStores = new HashMap<Long,MapData>();
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
	public void createMazeData(int scaledWidth,int scaledHeight,Long seed){
		HashMap<Long,MapData> mazeStore = mapCache.get("MAZE");
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
	public GridGraph generateRecMaze(Long seed){
		HashMap<Long,MapData> mazeStore = mapCache.get("MAZE");
		if(mazeStore.get(seed) == null){
			return null;
		}
		MapData map = mazeStore.get(seed);
		RecursiveMaze maze = new RecursiveMaze(map.width,map.height,map.seed);		
		return maze;		
	}

	
	private class MapData {
		
		private int width, height;
		private Long seed;
		
		private MapData(int width,int height,Long seed){
			this.width=width;
			this.height=height;
			this.seed=seed;
		}
	}
	
}
