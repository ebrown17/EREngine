package systems.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import components.types.MiddleRenderable;
import components.types.Path;
import components.types.PathCoordinates;
import components.types.Position;
import components.types.Renderable;
import components.types.TopRenderable;
import entities.Entity;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;
import graphs.vectors.Vector2d;
import managers.EntityManager;
import maps.TileType;
import systems.SystemProcessor;

public class PathSystem  implements SystemProcessor{
	
	private GridGraph currentMap;
	private EntityManager entityManager;
	private ArrayList<Vector2d> path;
	
	public PathSystem(EntityManager entityManager,GridGraph map){
		this.entityManager=entityManager;		
		currentMap = map;
	}

	@Override
	public void processOneTick(long lastFrameTick) {

		Collection<PathCoordinates> coords =entityManager.getAllComponentsOfType(PathCoordinates.class);
		if(!coords.isEmpty()){
			for(PathCoordinates coord : coords){
				int index = (coord.start.x*currentMap.COLUMNS)+coord.start.y;
				GridNode start = currentMap.getNodeList().get(index);
				index = (coord.goal.x*currentMap.COLUMNS)+coord.goal.y;
				GridNode end = currentMap.getNodeList().get(index);
				path = aStarSearch( start, end);
						
			}
			
			for(Vector2d v : path){
				Entity entity = entityManager.retrieveEntity();
				Position pos = new Position(v.x,v.y);
				Renderable r =  new MiddleRenderable(pos,TileType.PATH);
				Path pathe = new Path(pos);
				entityManager.addComponent(entity,pos);
				entityManager.addComponent(entity,pathe);
				entityManager.addComponent(entity,r);
			}
		}
		
	}
	
	public void setCurrentMap(GridGraph current){
		currentMap=current;
	}
	
	public ArrayList<Vector2d> aStarSearch(GridNode start,GridNode end){		
		
		Queue<GridNode> frontier = new PriorityQueue<GridNode>();
		HashMap<GridNode,GridNode> cameFrom = new HashMap<GridNode,GridNode>();
		HashMap<GridNode,Integer> costSoFar = new HashMap<GridNode,Integer>();		
		ArrayList<GridNode> path = new ArrayList<GridNode>();
		ArrayList<Vector2d> vectorPath = new ArrayList<Vector2d>();
		
		frontier.add(start);		
		cameFrom.put(start, start);		// maybe start not null
		costSoFar.put(start, 0);
		
		GridNode current = null;		
		Integer cost =0;
		
		while(!frontier.isEmpty()){			
			
			current = frontier.poll();
			if(current == end)break;			
			for(GridNode next: current.getEdges()){
				if(next.tile==TileType.WALL)continue;
				cost = 1+ costSoFar.get(current);
				if(!costSoFar.containsKey(next) || cost < costSoFar.get(next)){
					costSoFar.put(next, cost);
					next.priority= cost + heuristic(next.postion,end.postion);
					frontier.add(next);
					cameFrom.put(next, current);
					
				}				
			}				
		}
		if(current != end){
			return new ArrayList<Vector2d>();
		}
		path.add(current);
		while(current != start) {			
			current = cameFrom.get(current);		
			path.add(current);			
		}
		int count=0;
		for(GridNode node: path ){
			if(node.postion.sameVector(start.postion) || node.postion.sameVector(end.postion))continue;
			//node.tile = TileType.PATH;
			vectorPath.add(node.postion);
			count++;
		}	
		
		//System.out.println(count + " moves to solve");
		return vectorPath;
	}
	
	private static Integer heuristic(Vector2d a, Vector2d b){
		return (Math.abs(a.x - b.x) + Math.abs(a.y-b.y));
	}	

}