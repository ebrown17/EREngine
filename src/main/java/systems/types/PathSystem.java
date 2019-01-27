package systems.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import components.types.MiddleRenderable;
import components.types.PathComponent;
import components.types.Position;
import components.types.Renderable;
import components.types.WantsPath;
import entities.Entity;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;
import graphs.vectors.Vector2d;
import managers.EntityManager;
import maps.TileType;
import systems.SystemProcessor;

public class PathSystem implements SystemProcessor {

  private GridGraph currentMap;
  private EntityManager entityManager;
  private ArrayList<Vector2d> path;

  public PathSystem(EntityManager entityManager, GridGraph map) {
    this.entityManager = entityManager;
    currentMap = map;
  }

  @Override
  public void processOneTick(long lastFrameTick) {

    Collection<Entity> entitiesWantingPath =
        entityManager.getAllEntitiesPossesingComponent(WantsPath.class);

    if (!entitiesWantingPath.isEmpty()) {
      for (Entity entity : entitiesWantingPath) {
        WantsPath wp = entityManager.getComponent(entity, WantsPath.class);
        int index = (wp.start.x * currentMap.COLUMNS) + wp.start.y;
        GridNode start = currentMap.getNodeList().get(index);
        index = (wp.end.x * currentMap.COLUMNS) + wp.end.y;
        GridNode end = currentMap.getNodeList().get(index);
        path = aStarSearch(start, end);

        for (Vector2d v : path) {
          Entity nEntity = entityManager.retrieveEntity();
          Position pos = new Position(v.x, v.y);
          Renderable r = new MiddleRenderable(pos, TileType.PATH);
          entityManager.addComponent(nEntity, pos);
          entityManager.addComponent(nEntity, r);
        }

        PathComponent pathe = new PathComponent(path);
        entityManager.addComponent(entity, pathe);
      }

      for (Entity entity : entitiesWantingPath) {
        entityManager.removeComponent(entity, WantsPath.class);
      }
    }
  }

  public void setCurrentMap(GridGraph current) {
    currentMap = current;
  }

  public ArrayList<Vector2d> aStarSearch(GridNode start, GridNode end) {

    Queue<GridNode> frontier = new PriorityQueue<GridNode>();
    HashMap<GridNode, GridNode> cameFrom = new HashMap<GridNode, GridNode>();
    HashMap<GridNode, Integer> costSoFar = new HashMap<GridNode, Integer>();
    ArrayList<GridNode> path = new ArrayList<GridNode>();
    ArrayList<Vector2d> vectorPath = new ArrayList<Vector2d>();

    frontier.add(start);
    cameFrom.put(start, start); // maybe start not null
    costSoFar.put(start, 0);

    GridNode current = null;
    Integer cost;

    while (!frontier.isEmpty()) {

      current = frontier.poll();
      if (current == end) break;
      for (GridNode next : current.getEdges()) {
        if (next.tile == TileType.WALL) continue;
        cost = 1 + costSoFar.get(current);
        if (!costSoFar.containsKey(next) || cost < costSoFar.get(next)) {
          costSoFar.put(next, cost);
          next.priority = cost + heuristic(next.position, end.position);
          frontier.add(next);
          cameFrom.put(next, current);
        }
      }
    }
    if (current != end) {
      return new ArrayList<Vector2d>();
    }
    path.add(current);
    while (current != start) {
      current = cameFrom.get(current);
      path.add(current);
    }

    for (GridNode node : path) {
      if (node.position.sameVector(start.position) || node.position.sameVector(end.position)) continue;
      // node.tile = TileType.PATH;
      vectorPath.add(node.position);
    }

    return vectorPath;
  }

  private static Integer heuristic(Vector2d a, Vector2d b) {
    return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
  }
}
