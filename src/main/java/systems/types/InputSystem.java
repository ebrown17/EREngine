package systems.types;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;

import javax.swing.event.MouseInputListener;

import components.types.BaseRenderable;
import components.types.MiddleRenderable;
import components.types.PathComponent;
import components.types.Position;
import components.types.Renderable;
import components.types.WantsPath;
import entities.Entity;
import graphs.types.GridGraph;
import graphs.vectors.Vector2d;
import managers.EntityManager;
import maps.TileType;
import maps.mazes.RecursiveMaze;
import systems.SystemProcessor;

public class InputSystem implements SystemProcessor, MouseMotionListener, MouseInputListener {

	private EntityManager entityManager;
	private GridGraph map;
	private int cX = 0, cY = 0;
	private int oldX = 0, oldY = 0;
	private boolean mouseClickedFlag = false;
	private final int tileSize;

	public InputSystem(EntityManager entityManger, GridGraph map, int tileSize) {
		this.entityManager = entityManger;
		this.map = map;
		this.tileSize = tileSize;
	}

	@Override
	public void processOneTick(long lastFrameTick) {
		if ((oldX == cX && oldY == cY) || mouseClickedFlag ) {
			return;
		}
		oldX = cX;
		oldY = cY;

		Collection<BaseRenderable> base = entityManager.getAllComponentsOfType(BaseRenderable.class);
		for (Renderable b : base) {
			if (b.position.x == cX && b.position.y == cY && b.tile.solid)
				return;
		}
		
		Collection<Entity> entitiesM = entityManager.getAllEntitiesPossesingComponent(MiddleRenderable.class);
		int removed = 0;
		for (Entity entity : entitiesM) {
			entityManager.recycleActiveEntity(entity);
			removed++;
		}

		Collection<Entity> entitiesPC = entityManager.getAllEntitiesPossesingComponent(WantsPath.class);
		for (Entity entity : entitiesPC) {
			entityManager.recycleActiveEntity(entity);
			removed++;
		}
		Collection<Entity> entitiesP = entityManager.getAllEntitiesPossesingComponent(PathComponent.class);
		for (Entity entity : entitiesP) {
			entityManager.recycleActiveEntity(entity);
			removed++;
		}

		System.out.println("removed: " + removed);
		
		Collection<MiddleRenderable> middle = entityManager.getAllComponentsOfType(MiddleRenderable.class);
		for (Renderable mid : middle) {
			if (mid.position.x == cX && mid.position.y == cY)
				return;
		}
		

		Entity entity = entityManager.retrieveEntity();
		Vector2d current = new Vector2d(cX, cY);
		RecursiveMaze test = (RecursiveMaze) map;
		WantsPath wantsPath = new WantsPath(current, test.getEnd().postion);
		entityManager.addComponent(entity, wantsPath);

		entity = entityManager.retrieveEntity();
		Position pos = new Position(cX, cY);
		Renderable r = new MiddleRenderable(pos, TileType.MAGENTA);
		entityManager.addComponent(entity, pos);
		entityManager.addComponent(entity, r);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("event " );
		cX = e.getX() / tileSize;
		cY = e.getY() / tileSize;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON1){
			mouseClickedFlag = !mouseClickedFlag;
		}
		

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
