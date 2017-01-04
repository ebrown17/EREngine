package systems.types;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import components.types.BaseRenderable;
import components.types.MiddleRenderable;
import components.types.Position;
import components.types.Renderable;
import components.types.TopRenderable;
import managers.EntityManager;
import maps.TileType;
import systems.SystemProcessor;
import util.RenderPriority;

public class InputSystem implements SystemProcessor,MouseMotionListener{
	
	private EntityManager entityManger;
	private int cX=0, cY=0;
	private long previousGameTick = 0;
	
	public InputSystem(EntityManager entityManger){
		this.entityManger = entityManger;
	
	}
	

	@Override
	public void processOneTick(long lastFrameTick) {
		//renderFrame.addMouseMotionListener(l);
		/*int entity = entityManger.createEntity();
		Position pos = new Position(cX,cY);
		Renderable r =  new Renderable(pos,TileType.PATH);
		entityManger.addComponent(entity,pos);
		entityManger.addComponent(entity,r);*/
		int entity = entityManger.createEntity();
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

}
