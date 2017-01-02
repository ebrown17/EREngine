package systems.types;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import components.types.Position;
import components.types.Renderable;
import managers.EntityManager;
import maps.TileType;
import systems.SystemProcessor;

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
		
		if(lastFrameTick-previousGameTick>2000000000){
			previousGameTick=lastFrameTick;
			System.out.println("x: " + cX/10 + " y: " +cY/10 );
		}
		
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
