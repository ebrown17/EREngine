package systems.types;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import components.types.BaseRenderable;
import components.types.MiddleRenderable;
import components.types.Position;
import components.types.Renderable;
import components.types.TopRenderable;
import entities.Entity;
import managers.EntityManager;
import maps.TileType;
import systems.SystemProcessor;
import util.RenderPriority;

public class InputSystem implements SystemProcessor,MouseMotionListener, MouseInputListener{
	
	private EntityManager entityManger;
	private int cX=0, cY=0;
	private boolean mouseClickedFlag = false;
	private final int tileSize;
	
	public InputSystem(EntityManager entityManger, int tileSize){
		this.entityManger = entityManger;
		this.tileSize = tileSize;
	}
	
	@Override
	public void processOneTick(long lastFrameTick) {
		
		if(mouseClickedFlag){
			Collection<Entity> entities = entityManger.getAllEntitiesPossesingComponent(MiddleRenderable.class);
			int removed =0;
			for(Entity entity : entities){
				Renderable rend = entityManger.getComponent(entity, MiddleRenderable.class);
					entityManger.recycleActiveEntity(entity);
					removed++;
				
			}
			
			System.out.println("removed: " +  removed);
			mouseClickedFlag = false;
		}
		
		Collection<MiddleRenderable> middle = entityManger.getAllComponentsOfType(MiddleRenderable.class);
		
		for(Renderable mid : middle){
			if(mid.position.x == cX && mid.position.y == cY ) return;
		}
		Collection<BaseRenderable> base = entityManger.getAllComponentsOfType(BaseRenderable.class);
		
		for(Renderable b : base){
			if(b.position.x == cX && b.position.y == cY && b.tile.solid ) return;
		}
		Entity entity = entityManger.retrieveEntity();
		Position pos = new Position(cX,cY);
		Renderable r =  new MiddleRenderable(pos,TileType.MAGENTA);
		entityManger.addComponent(entity,pos);
		entityManger.addComponent(entity,r);
		
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
