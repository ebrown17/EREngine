package components.types;

import components.Component;
import graphs.vectors.Vector2d;

public class WantsPath implements Component {

	public Vector2d start,end;
	
	public WantsPath(Vector2d start,Vector2d end){
		this.start=start;
		this.end=end;
	}
}
