package components.types;

import components.Component;
import maps.TileType;

public class Renderable implements Component{

	public Position position;
	public TileType tile;
	public Renderable(Position position,TileType tile){
		this.position=position;
		this.tile = tile;
	}
}
