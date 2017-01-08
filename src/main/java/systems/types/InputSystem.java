package systems.types;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;

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
	private long previousGameTick = 0;
	
	public InputSystem(EntityManager entityManger){
		this.entityManger = entityManger;
	
	}
	

	@Override
	public void processOneTick(long lastFrameTick) {
		Collection<MiddleRenderable> middle = entityManger.getAllComponentsOfType(MiddleRenderable.class);
		for(MiddleRenderable mid : middle){
			if(mid.position.x == cX/10 && mid.position.y == cY/10 ) return;
		}
		Entity entity = entityManger.createEntity();
		Position pos = new Position(cX/10,cY/10);
		Renderable r =  new MiddleRenderable(pos,TileType.PATH,RenderPriority.MIDDLE_LAYER);
		entityManger.addComponent(entity,pos);
		entityManger.addComponent(entity,r);
		/*
		if(lastFrameTick-previousGameTick>2000000000){
			previousGameTick=lastFrameTick;
			System.out.println("x: " + cX/10 + " y: " +cY/10 );
			
			int entity = entityManger.createEntity();
			Position pos = new Position(cX/10,cY/10);
			Renderable r =  new MiddleRenderable(pos,TileType.PATH,RenderPriority.MIDDLE_LAYER);
			entityManger.addComponent(entity,pos);
			entityManger.addComponent(entity,r);
			
			
		}*/
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("event "  );
		cX = e.getX();
		cY = e.getY();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		/*// TODO Auto-generated method stub
		Collection<MiddleRenderable> middle = entityManger.getComponent(entity, componentType)
		
		for(MiddleRenderable mid : middle){
			entityManger.killEntity(mid.);
		}
		*/
		
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
