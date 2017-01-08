package components.types;

import components.Component;
import maps.TileType;
import util.RenderPriority;

public abstract class Renderable implements Component,Comparable<Renderable>{

	public Position position;
	public TileType tile;
	public int priority;
	
	public Renderable(Position position,TileType tile,RenderPriority priority){
		this.position=position;
		this.tile = tile;
		this.priority=priority.getPriority();
	}

	@Override
	public int compareTo(Renderable o) {
		if (priority < o.priority)
			return -1;
		if (priority > o.priority)
			return 1;
		else
			return 0;
	}
	
}
