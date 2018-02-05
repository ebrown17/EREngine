package systems.types;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashMap;

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
import util.input.Keyboard;
import util.input.Mouse;

public class InputSystem implements SystemProcessor {

  private EntityManager entityManager;
  private RecursiveMaze map;
  private int cX = 0, cY = 0;
  private boolean isUpMove = false, isDownMove=false, isLMove = false, isRMove=false;
  private int oldX = 0, oldY = 0,dX=0,dY=0;
  private int maxX, maxY;
  private boolean mouseClickedFlag = false;
  private final int tileSize;
  private Mouse mouse;
  private Keyboard keyboard;

  public InputSystem(EntityManager entityManger, RecursiveMaze map, int tileSize, Mouse mouse, Keyboard keyboard) {
    this.entityManager = entityManger;
    this.map = map;
    this.tileSize = tileSize;
    this.mouse = mouse;
    this.keyboard = keyboard;
    this.cX=map.getStart().postion.x;
    this.cY=map.getStart().postion.y;
    dX=cX;
    dY=cY;
    this.maxX=map.ROWS;
    this.maxY=map.COLUMNS;
    
  }

  @Override
  public void processOneTick(long lastFrameTick) {
    HashMap<Integer, Boolean> keyMap = keyboard.getKeyMap();
    
    isUpMove = keyMap.get(KeyEvent.VK_W);
    isDownMove = keyMap.get(KeyEvent.VK_S);
    isLMove = keyMap.get(KeyEvent.VK_A);
    isRMove = keyMap.get(KeyEvent.VK_D);
    
   /* cX = mouse.getCurrentX() / tileSize;
    cY = mouse.getCurrentY() / tileSize;*/
    dX=cX;
    dY=cY;
    
    if(isUpMove ){
      if(dY < 2) return;
      dY-=1;
      System.out.println("up: dY " + dY);
    }
    else if(isDownMove){
      if(dY >= maxY-2) return;
      dY+=1;
      System.out.println("down: dY " + dY);
    }
    else if(isLMove){
      if(dX < 2) return;
      dX-=1;
      System.out.println("left: dX " + dX);
    }
    else if(isRMove){
      if(dX >= maxX-2) return;
      dX+=1;
      System.out.println("right: dX " + dX);
    }
    

    if ((oldX == dX && oldY == dY) ) {
      return;
    }
   
    
    Collection<BaseRenderable> base = entityManager.getAllComponentsOfType(BaseRenderable.class);
    for (Renderable b : base) {
      if (b.position.x == dX && b.position.y == dY && b.tile.solid)
        return;
    }
    cX = dX;
    cY= dY;
    oldX = cX;
    oldY = cY;
    
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

    Position pos = new Position(cX, cY);
    Renderable r = new MiddleRenderable(pos, TileType.MAGENTA);
    entityManager.addComponent(entity, pos);
    entityManager.addComponent(entity, r);

  }

}
