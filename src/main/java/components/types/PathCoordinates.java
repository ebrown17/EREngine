package components.types;

import components.Component;
import graphs.vectors.Vector2d;

public class PathCoordinates implements Component {

	public Vector2d start,goal;
	
	public PathCoordinates(Vector2d start,Vector2d goal){
		this.start=start;
		this.goal=goal;
	}
}
