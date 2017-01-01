import java.util.ArrayList;

import components.types.Position;
import components.types.Renderable;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;
import managers.EntityManager;
import managers.MapManager;
import systems.types.RenderSystem;

public class Main {

	
	public static void main(String[] args){
		String seed = "TESTersss";	
		final int TILESIZE=5;
		final int WIDTH=800,HEIGHT=600;
		int scaleX=WIDTH/TILESIZE, scaleY=HEIGHT/TILESIZE;
		
		EntityManager em = new EntityManager();
		MapManager mapManager = new MapManager(em);
		
		mapManager.createMazeData(scaleX, scaleY, seed);
		GridGraph maze = mapManager.generateRecMaze(seed);
		
		RenderSystem renderSystem = new RenderSystem(WIDTH,HEIGHT,TILESIZE,em);
	
		
		
		for(GridNode node : maze.getNodeList()){
			int entity = em.createEntity();
			Position pos = new Position(node.postion.x,node.postion.y);
			Renderable r =  new Renderable(pos,node.tile);
			em.addComponent(entity,pos);
			em.addComponent(entity,r);
		}
	
		
		
		while(true){
			renderSystem.processOneTick(System.nanoTime());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		/*EntityManager em = new EntityManager();
		
		for(int i=0; i < 5; i++){
			for(int j=0;j<5; j++) {
				int entity = em.createEntity();				
				em.addComponent(entity, new Position(i,j));
			}			
		}
		
		for(int entity: em.entities){
			
			System.out.println((em.getComponent(entity, Position.class)).x + " " + (em.getComponent(entity, Position.class)).y);
		}*/
		
	}
}
