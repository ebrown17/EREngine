package systems.types;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import components.types.BaseRenderable;
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
import maps.mazes.RecursiveMaze;
import systems.SystemProcessor;
import util.RenderPriority;

public class InputSystem implements SystemProcessor,MouseMotionListener, MouseInputListener{
	
	private EntityManager entityManager;
	private GridGraph map;
	private int cX=0, cY=0;
	private boolean mouseClickedFlag = false;
	private final int tileSize;
	
	public InputSystem(EntityManager entityManger,GridGraph map, int tileSize){
		this.entityManager = entityManger;
		this.map=map;
		this.tileSize = tileSize;
	}
	
	@Override
	public void processOneTick(long lastFrameTick) {
		
		if(mouseClickedFlag){
			Collection<Entity> entities = entityManager.getAllEntitiesPossesingComponent(MiddleRenderable.class);
			int removed =0;
			for(Entity entity : entities){
				entityManager.recycleActiveEntity(entity);
					removed++;				
			}	
			
			entities = entityManager.getAllEntitiesPossesingComponent(PathCoordinates.class);
			for(Entity entity : entities){
				entityManager.recycleActiveEntity(entity);
				removed++;				
			}
			entities = entityManager.getAllEntitiesPossesingComponent(Path.class);
			for(Entity entity : entities){
				entityManager.recycleActiveEntity(entity);
				removed++;				
			}
			
			
			System.out.println("removed: " +  removed);
			mouseClickedFlag = false;
			
			Entity entity = entityManager.retrieveEntity();
			Vector2d current = new Vector2d(cX,cY);
			RecursiveMaze test = (RecursiveMaze)map;
			PathCoordinates coords = new PathCoordinates(current,test.getEnd().postion);
			entityManager.addComponent(entity,coords);
			//Renderable r =  new MiddleRenderable(pos,TileType.MAGENTA);
			
			
		}
		
		Collection<Entity> entities = entityManager.getAllEntitiesPossesingComponent(MiddleRenderable.class);
		int removed =0;
		for(Entity entity : entities){
			Renderable rend = entityManager.getComponent(entity, MiddleRenderable.class);
			entityManager.recycleActiveEntity(entity);
			removed++;
			
		}
		
		Collection<MiddleRenderable> middle = entityManager.getAllComponentsOfType(MiddleRenderable.class);
		for(Renderable mid : middle){
			if(mid.position.x == cX && mid.position.y == cY ) return;
		}
		Collection<BaseRenderable> base = entityManager.getAllComponentsOfType(BaseRenderable.class);
		
		for(Renderable b : base){
			if(b.position.x == cX && b.position.y == cY && b.tile.solid ) return;
		}
		Entity entity = entityManager.retrieveEntity();
		Position pos = new Position(cX,cY);
		Renderable r =  new MiddleRenderable(pos,TileType.MAGENTA);
		entityManager.addComponent(entity,pos);
		entityManager.addComponent(entity,r);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("event "  );
		cX = e.getX()/tileSize;
		cY = e.getY()/tileSize;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseClickedFlag = true;
		
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
