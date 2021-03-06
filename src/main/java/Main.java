import components.types.*;
import entities.Entity;
import graphs.nodes.GridNode;
import graphs.types.GridGraph;
import managers.EntityManager;
import managers.MapManager;
import managers.RandomGeneratorManager;
import maps.TileType;
import systems.types.InputSystem;
import systems.types.PathSystem;
import systems.types.RenderSystem;
import util.input.KeyBindings;
import util.input.Mouse;

public class Main {

  public static void main(String[] args) {
    String seed = "TESTersss";
    final int TILESIZE = 32;
    final int WIDTH = 2560, HEIGHT = 1440;
    int scaleX = WIDTH / TILESIZE, scaleY = HEIGHT / TILESIZE;

    EntityManager entityManager = new EntityManager();
    RandomGeneratorManager masterRandom = new RandomGeneratorManager(seed);
    MapManager mapManager = new MapManager();

    Long idSeed = masterRandom.getNewSeed();
    mapManager.createMazeData(scaleX, scaleY, idSeed);
    GridGraph map = mapManager.generateRecMaze(idSeed);


    for (GridNode node : map.getNodeList()) {
      Entity entity = entityManager.retrieveEntity();
      Position pos = new Position(node.position.x, node.position.y);
      Renderable r;
      if (node.tile == TileType.START || node.tile == TileType.END) {
        r = new TopRenderable(pos, node.tile);
      } else {
        r = new BaseRenderable(pos, node.tile);
      }

      entityManager.addComponent(entity, pos);
      entityManager.addComponent(entity, r);

      if (node.tile == TileType.START) {
        entity = entityManager.retrieveEntity();
        pos = new Position(node.position.x, node.position.y);

        r = new MiddleRenderable(pos, node.tile);
        entityManager.addComponent(entity, pos);
        entityManager.addComponent(entity, r);

        entity = entityManager.retrieveEntity();
        pos = new Position(node.position.x, node.position.y);

        r = new PlayerRenderable(pos, TileType.ORANGE);
        entityManager.addComponent(entity, pos);
        entityManager.addComponent(entity, r);

      }
    }
    Entity entity = entityManager.retrieveEntity();
    CurrentMapGrid cmg = new CurrentMapGrid(map);
    entityManager.addComponent(entity, cmg);

    Mouse mouse = new Mouse();
    mouse.tileSize = TILESIZE;
    KeyBindings bindings = new KeyBindings();
    InputSystem inputSystem = new InputSystem(entityManager,  TILESIZE, mouse);

    RenderSystem renderSystem = new RenderSystem(WIDTH, HEIGHT, TILESIZE, entityManager);
    PathSystem pathSystem = new PathSystem(entityManager, map);

    renderSystem.setMouseListener(mouse);

    bindings.registerBindingListener(renderSystem.getRootPane());
    bindings.setInputSystem(inputSystem);

    while (true) {
      long currentTick = System.nanoTime();

      inputSystem.processOneTick(currentTick);

      pathSystem.processOneTick(currentTick);

      renderSystem.processOneTick(currentTick);

    }
  }
}
