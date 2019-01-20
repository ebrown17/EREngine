package components.types;

import components.Component;
import graphs.types.GridGraph;

public class CurrentMapGrid implements Component {

  public int maxX, maxY;

  public CurrentMapGrid(GridGraph map){
    maxX = map.ROWS;
    maxY = map.COLUMNS;
    map.getNodeList();
  }
}
