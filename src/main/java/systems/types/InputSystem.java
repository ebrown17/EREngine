package systems.types;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
  private int cX , cY;
  private int oldX = 0, oldY = 0;
  private int maxX, maxY;
  private boolean moveAction = false;
  private final int tileSize;
  private Mouse mouse;
  private Vector2d moveVector = new Vector2d(0,0);

  public InputSystem(EntityManager entityManger, RecursiveMaze map, int tileSize, Mouse mouse) {
    this.entityManager = entityManger;
    this.map = map;
    this.tileSize = tileSize;
    this.mouse = mouse;
    this.cX=map.getStart().postion.x;
    this.cY=map.getStart().postion.y;
    this.maxX=map.ROWS;
    this.maxY=map.COLUMNS;

  }

  @Override
  public void processOneTick(long lastFrameTick) {

    boolean mouseClickedFlag = mouse.isMouseClickedFlag();
    int mX = mouse.getCurrentX() / tileSize;
    int mY = mouse.getCurrentY() / tileSize;
    int kX = cX;
    int kY = cY;

    Collection<BaseRenderable> base = entityManager.getAllComponentsOfType(BaseRenderable.class);
    if(!mouseClickedFlag){
      for (Renderable b : base) {
        if (b.position.x == mX && b.position.y == mY && b.tile.solid){
          return;
        }
      }
      if(mX >= maxX || mY >= maxY){
        return;
      }
      cX = mX;
      cY = mY;
    }
    if(moveAction){
      kX += moveVector.x;
      kY += moveVector.y;
      moveAction = false;
      for (Renderable b : base) {
        if (b.position.x == kX && b.position.y == kY && b.tile.solid){
          return;
        }
      }
      cX = kX;
      cY = kY;

    }

    if ((oldX == cX && oldY == cY) ) {
      return;
    }

    oldX = cX;
    oldY = cY;

    ArrayDeque<Entity> entitiesToRemove = new ArrayDeque<Entity>();
    Collection<Entity> entitiesM = entityManager.getAllEntitiesPossesingComponent(MiddleRenderable.class);
    Collection<Entity> entitiesPC = entityManager.getAllEntitiesPossesingComponent(WantsPath.class);
    Collection<Entity> entitiesP = entityManager.getAllEntitiesPossesingComponent(PathComponent.class);

    entitiesToRemove.addAll(entitiesM);
    entitiesToRemove.addAll(entitiesPC);
    entitiesToRemove.addAll(entitiesP);

    for (Entity entity : entitiesToRemove) {
      entityManager.recycleActiveEntity(entity);
    }

    Collection<MiddleRenderable> middle = entityManager.getAllComponentsOfType(MiddleRenderable.class);
    for (Renderable mid : middle) {
      if (mid.position.x == cX && mid.position.y == cY)
        return;
    }

    Entity entity = entityManager.retrieveEntity();
    Vector2d current = new Vector2d(cX, cY);
    WantsPath wantsPath = new WantsPath(current, map.getEnd().postion);
    entityManager.addComponent(entity, wantsPath);

    Position pos = new Position(cX, cY);
    Renderable r = new MiddleRenderable(pos, TileType.MAGENTA);
    entityManager.addComponent(entity, pos);
    entityManager.addComponent(entity, r);
    moveAction = false;
  }

  public void move(int x, int y,String actionName){
    System.out.println("Action: " + actionName + " " + x + " " + y);
    moveVector.setXY(x,y);
    moveAction = true;

  }



}
