package graphs.nodes;

import java.util.ArrayList;
import graphs.vectors.Vector2d;
import maps.TileType;

public class GridNode implements Comparable<GridNode> {

	public Vector2d position;
	public boolean visited = false;
	public Integer cost = 0, priority = 0;
	public TileType tile = TileType.WALL;
	private ArrayList<GridNode> edges = new ArrayList<GridNode>();

	public GridNode(Vector2d position) {
		this.position = position;
	}

	public void addEdge(GridNode node) {
		edges.add(node);
	}

	public ArrayList<GridNode> getEdges() {
		return edges.size() > 0 ? edges : null;
	}

	@Override
	public int compareTo(GridNode o) {
		if (priority < o.priority)
			return -1;
		if (priority > o.priority)
			return 1;
		else
			return 0;
	}

}
