package maps.mazes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import graphs.types.GridGraph;
import graphs.nodes.GridNode;
import maps.TileType;


public class RecursiveMaze extends GridGraph {
	
	public GridNode start,end;
	
	/**	  
	 * @param rows number of rows a maze will have
	 * @param columns number of columns a maze will have
	 */
	
	public RecursiveMaze(int rows, int columns,Long seed){
		super(rows,columns,seed);
		generateRecursiveMaze();
	}	
	
	private void generateRecursiveMaze(){
		Stack<GridNode> stack = new Stack<GridNode>();
		ArrayList<GridNode> unVisited = new ArrayList<GridNode>();
		
		for(GridNode node: nodeList){
			unVisited.add(node);
		}
		
		ArrayList<GridNode> neighbors;
		
		GridNode current = unVisited.remove(COLUMNS+1);		
		current.cost=0;
		current.visited=true;
		current.tile=TileType.START;
		start=current;
		
		while(unVisited.size()>0){
			current.visited=true;
			neighbors = new ArrayList<GridNode>();
			for(GridNode neighbor:current.getEdges()){
				if(neighbor.visited)continue;
				//if(neighbor.postion.sameVector(start.postion))continue;
				//if(neighbor.postion.x == 0 && neighbor.postion.y == 0 && (neighbor.postion.x > ROWS || neighbor.postion.y > COLUMNS))continue; // ?				
				
				int count =0;
				for(GridNode nextNeghbor: neighbor.getEdges()){
					if(nextNeghbor.tile == TileType.WALL)count++;
				}
				if(count>=3){
					if(neighbor.postion.x > 0 && neighbor.postion.y > 0 && neighbor.postion.x < ROWS-1 && neighbor.postion.y < COLUMNS-1){
						neighbors.add(neighbor);
					}
				}
			}
			if(neighbors.size()>0){
				int size = neighbors.size();
				
				stack.push(current);				
				current = neighbors.remove((int)rand.nextInt(size));			
				current.tile=TileType.CLEAR;				
				
			}
			else if (stack.size()!=0){
				current = stack.pop();
			}
			else {
				current = unVisited.remove((int)rand.nextInt(unVisited.size()));
				
			}			
			if(unVisited.size()<=0){				
				//current= nodeList.get(nodeList.size()-1);
				current.tile=TileType.END;				
				current.cost=0;
				current.visited=true;				
				end=current;
			}
		}
	}
	
	public GridNode getStart(){
		return start;
	}
	
	public GridNode getEnd(){
		return end;
	} 

}
