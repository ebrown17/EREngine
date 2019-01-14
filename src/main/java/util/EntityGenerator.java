package util;

import entities.Entity;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class EntityGenerator {
  private String name;

  private final int MAX_ID = Integer.MAX_VALUE;
  private final int POOL_INCREMENT_SIZE = 10000;
  private ArrayList<Entity> activeEntities;
  private ArrayDeque<Entity> entityPool;
  private ArrayList<Integer> entityIdPool;
  private int lowestUnassignedID=1;

  public EntityGenerator(String name) {
    this.name = name;
    entityPool = new ArrayDeque<Entity>(POOL_INCREMENT_SIZE);
    activeEntities = new ArrayList<Entity>(POOL_INCREMENT_SIZE);
    entityIdPool = new ArrayList<Integer>(POOL_INCREMENT_SIZE);

    for (int i = 0; i < POOL_INCREMENT_SIZE; i++) {
      entityIdPool.add(lowestUnassignedID);
      Entity entity = new Entity(lowestUnassignedID++);
      entityPool.add(entity);
    }

  }

  public Entity getEntity() throws Error {
    if (!entityPool.isEmpty()) {
      Entity entity = entityPool.pop();
      activeEntities.add(entity);
      return entity;
    } else {
      fillEntityPool();
      if (!entityPool.isEmpty()) {
        Entity entity = entityPool.pop();
        activeEntities.add(entity);
        return entity;
      }
    }
    throw new Error(
        "Error: No Entities are available. In Use: "
            + activeEntities.size()
            + " Pool Size: "
            + entityPool.size()
            + " Max: "
            + MAX_ID
            + " lowest unassigned "
            + lowestUnassignedID);
  }

  public void recycleEntity(Entity entity){
    if(activeEntities.contains(entity)){
      activeEntities.remove(entity);
      entityPool.add(entity);
    }
  }

  private void fillEntityPool() throws Error {
    for (int i = 0; i < POOL_INCREMENT_SIZE; i++) {
      if (lowestUnassignedID <= MAX_ID) {
        entityIdPool.add(lowestUnassignedID);
        Entity entity = new Entity(lowestUnassignedID++);
        entityPool.add(entity);
        if (lowestUnassignedID > MAX_ID) {
          return;
        }
      }
      else {
        if (activeEntities.size() + entityPool.size() > MAX_ID) {
          throw new Error(
              "Exception: No Entities are available. In Use: "
                  + activeEntities.size()
                  + " Pool Size: "
                  + entityPool.size()
                  + " Max: "
                  + MAX_ID
                  + " lowest unassigned "
                  + lowestUnassignedID);
        }
      }
    }
  }

  public ArrayList<Entity> getActiveEntities(){
    return activeEntities;
  }

  @Override
  public String toString() {
    return "[name="
        + name
        + ", identityPool="
        + entityPool.size()
        + ", identityInUse="
        + activeEntities.size()
        + ", lowestUnassignedID="
        + lowestUnassignedID
        + "]";
  }

  public Integer getEntityPoolSize() {
    return entityPool.size();
  }

  public Integer getActiveSize() {
    return activeEntities.size();
  }

  public Integer getIdPoolSize() {
    return entityIdPool.size();
  }

}
