import components.types.BaseRenderable;
import components.types.Position;
import components.types.Renderable;
import components.types.TopRenderable;
import entities.Entity;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;
import managers.EntityManager;
import managers.FontManager;
import managers.MapManager;
import managers.RandomGeneratorManager;
import maps.TileType;
import systems.types.InputSystem;
import systems.types.PathSystem;
import systems.types.RenderSystem;
import util.RenderPriority;

public class Main {

	
	public static void main(String[] args){
		String seed = "TESTersss";	
		final int TILESIZE=25;
		final int WIDTH=800,HEIGHT=800;
		int scaleX=WIDTH/TILESIZE, scaleY=HEIGHT/TILESIZE;
		
		
		EntityManager entityManager = new EntityManager();
		RandomGeneratorManager masterRandom = new RandomGeneratorManager(seed);		
		MapManager mapManager = new MapManager();
		
		Long idSeed = masterRandom.getNewSeed();		
		mapManager.createMazeData(scaleX, scaleY, idSeed);
		GridGraph maze = mapManager.generateRecMaze(idSeed);
		
			
		
		for(GridNode node : maze.getNodeList()){
			Entity entity = entityManager.retrieveEntity();
			Position pos = new Position(node.postion.x,node.postion.y);
			Renderable r;
			if(node.tile == TileType.START || node.tile == TileType.END){
				r =  new TopRenderable(pos,node.tile);
			}
			else{
				r =  new BaseRenderable(pos,node.tile);
			}
			entityManager.addComponent(entity,pos);
			entityManager.addComponent(entity,r);
		}
		
		
		InputSystem inputSystem = new InputSystem(entityManager,maze,TILESIZE);
		RenderSystem renderSystem = new RenderSystem(WIDTH,HEIGHT,TILESIZE,entityManager);
		PathSystem pathSystem = new PathSystem(entityManager,maze);
		
		renderSystem.setMouseListener(inputSystem);
		renderSystem.setMouseEventListener(inputSystem);
		
		while(true){
			
			long currentTick = System.nanoTime();
			
			inputSystem.processOneTick(currentTick);
			pathSystem.processOneTick(currentTick);
			renderSystem.processOneTick(currentTick);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
