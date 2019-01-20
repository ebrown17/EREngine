package systems.types;

import java.util.ArrayDeque;
import java.util.Collection;

import components.types.*;
import entities.Entity;
import graphs.vectors.Vector2d;
import managers.EntityManager;
import maps.TileType;
import systems.SystemProcessor;
import util.input.Mouse;

public class InputSystem implements SystemProcessor {
  private EntityManager entityManager;
  private int cX, cY;
  private int prevCX = 0, prevMX = 0, prevCY = 0, prevMY = 0;
  private boolean moveAction = false;
  private final int tileSize;
  private Mouse mouse;
  private Vector2d moveVector = new Vector2d(0, 0);
  private Vector2d goal = null;

  public InputSystem(EntityManager entityManger, int tileSize, Mouse mouse) {
    this.entityManager = entityManger;
    this.tileSize = tileSize;
    this.mouse = mouse;
  }

  @Override
  public void processOneTick(long lastFrameTick) {
    Collection<CurrentMapGrid> currentMapC =
        entityManager.getAllComponentsOfType(CurrentMapGrid.class);
    if (currentMapC.size() <= 0 || currentMapC.size() > 1) {
      System.out.println("Error current map size: " + currentMapC.size());
      return;
    }
    CurrentMapGrid currentMap = currentMapC.iterator().next();
    int maxX = currentMap.maxX;
    int maxY = currentMap.maxY;

    Collection<PlayerRenderable> curPos =
        entityManager.getAllComponentsOfType(PlayerRenderable.class);

    if (curPos.size() <= 0 || curPos.size() > 1) {
      System.out.println("Error current position size: " + curPos.size());
      return;
    }

    Collection<Entity> playerEntity =
        entityManager.getAllEntitiesPossesingComponent(PlayerRenderable.class);
    Entity playerE = playerEntity.iterator().next();

    PlayerRenderable playerR = curPos.iterator().next();
    cX = playerR.position.x;
    cY = playerR.position.y;

    boolean mouseClickedFlag = mouse.isMouseClickedFlag();
    int mX = mouse.getCurrentX() / tileSize;
    int mY = mouse.getCurrentY() / tileSize;
    int kX = cX;
    int kY = cY;

    Collection<BaseRenderable> base = entityManager.getAllComponentsOfType(BaseRenderable.class);

    if (!mouseClickedFlag) {
      for (Renderable b : base) {
        if (b.position.x == mX && b.position.y == mY && b.tile.solid) {
          return;
        }
      }
      goal = new Vector2d(mX, mY);
    }
    if (moveAction) {
      kX += moveVector.x;
      kY += moveVector.y;
      moveAction = false;
      for (Renderable b : base) {
        if (b.position.x == kX && b.position.y == kY && b.tile.solid) {
          return;
        }
      }
      cX = kX;
      cY = kY;
      playerR.position.setPostion(kX, kY);
    }

    /*    Vector2d goal= null;
    Collection<TopRenderable> top = entityManager.getAllComponentsOfType(TopRenderable.class);
    for (Renderable b : top) {
      if(b.tile == TileType.END){
        goal = new Vector2d( b.position.x, b.position.y);
      }
    }*/

    if ((prevCX == cX && prevCY == cY) && (prevMX == mX && prevMY == mY)) {
      return;
    }

    prevCX = cX;
    prevCY = cY;
    prevMX = mX;
    prevMY = mY;

    ArrayDeque<Entity> entitiesToRemove = new ArrayDeque<Entity>();
    Collection<Entity> entitiesM =
        entityManager.getAllEntitiesPossesingComponent(MiddleRenderable.class);
    /*    Collection<Entity> entitiesPC = entityManager.getAllEntitiesPossesingComponent(WantsPath.class);*/
    /*Collection<Entity> entitiesP =
        entityManager.getAllEntitiesPossesingComponent(PathComponent.class);

    if (entitiesP.iterator().hasNext()) {
      System.out.println("cX cY: " + cX + " " + cY);
      Entity t = entitiesP.iterator().next();
      PathComponent pc = entityManager.getComponent(t, PathComponent.class);
      if (pc.path.size() >= 1) {
        Vector2d startPath = pc.path.get(pc.path.size() - 1);
        System.out.println("SpX SPy: " + startPath.x + " " + startPath.y);
        if (startPath.x == cX && startPath.y == cY) {
          pc.path.remove(pc.path.size() - 1);
          entitiesP.remove(t);
        }
      }
    }*/

    entitiesToRemove.addAll(entitiesM);
    // entitiesToRemove.addAll(entitiesPC);
    // entitiesToRemove.addAll(entitiesP);

    int removed = 0;
    for (Entity entity : entitiesToRemove) {
      entityManager.recycleActiveEntity(entity);
      removed++;
    }
    System.out.println("rem: " + removed);

    Collection<MiddleRenderable> middle =
        entityManager.getAllComponentsOfType(MiddleRenderable.class);
    for (Renderable mid : middle) {
      if (mid.position.x == cX && mid.position.y == cY) return;
    }

    if (mX >= maxX || mY >= maxY) {
      return;
    }

    Collection<Entity> entitiesWp = entityManager.getAllEntitiesPossesingComponent(WantsPath.class);

    for (Entity entity : entitiesWp) {
      entityManager.removeComponent(entity, PathComponent.class);
    }

    if (goal != null) {
      Vector2d current = new Vector2d(cX, cY);
      WantsPath wantsPath = new WantsPath(current, goal);
      entityManager.addComponent(playerE, wantsPath);
    }

    if (mouseClickedFlag && goal != null) {
      Entity entity = entityManager.retrieveEntity();
      Position pos = new Position(goal.x, goal.y);
      Renderable r = new MiddleRenderable(pos, TileType.MAGENTA);
      entityManager.addComponent(entity, pos);
      entityManager.addComponent(entity, r);
    }
    Entity entity = entityManager.retrieveEntity();
    Position pos = new Position(mX, mY);
    Renderable r = new MiddleRenderable(pos, TileType.MAGENTA);
    entityManager.addComponent(entity, pos);
    entityManager.addComponent(entity, r);

    moveAction = false;
  }

  public void move(int x, int y, String actionName) {
    System.out.println("Action: " + actionName + " " + x + " " + y);
    moveVector.setXY(x, y);
    moveAction = true;
  }
}
