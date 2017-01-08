import components.types.BaseRenderable;
import components.types.Position;
import components.types.Renderable;
import entities.Entity;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;
import managers.EntityManager;
import managers.MapManager;
import managers.RandomGeneratorManager;
import systems.types.InputSystem;
import systems.types.RenderSystem;
import util.RenderPriority;

public class Main {

	
	public static void main(String[] args){
		String seed = "TESTersss";	
		final int TILESIZE=10;
		final int WIDTH=800,HEIGHT=600;
		int scaleX=WIDTH/TILESIZE, scaleY=HEIGHT/TILESIZE;
		
		EntityManager entityManager = new EntityManager();
		RandomGeneratorManager masterRandom = new RandomGeneratorManager(seed);		
		MapManager mapManager = new MapManager();
		
		InputSystem inputSystem = new InputSystem(entityManager);
		RenderSystem renderSystem = new RenderSystem(WIDTH,HEIGHT,TILESIZE,entityManager);
		
		renderSystem.setMouseListener(inputSystem);
		renderSystem.setMouseEventListener(inputSystem);
		
		Long idSeed = masterRandom.getNewSeed();		
		mapManager.createMazeData(scaleX, scaleY, idSeed);
		GridGraph maze = mapManager.generateRecMaze(idSeed);
		
			
		
		for(GridNode node : maze.getNodeList()){
			Entity entity = entityManager.createEntity();
			Position pos = new Position(node.postion.x,node.postion.y);
			Renderable r =  new BaseRenderable(pos,node.tile,RenderPriority.BASE_LAYER);
			entityManager.addComponent(entity,pos);
			entityManager.addComponent(entity,r);
		}
		
		while(true){
			
			long currentTick = System.nanoTime();
			
			inputSystem.processOneTick(currentTick);
			
			renderSystem.processOneTick(currentTick);
			
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
