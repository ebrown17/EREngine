package components.types;

import java.util.ArrayList;

import components.Component;
import graphs.vectors.Vector2d;

public class PathComponent implements Component {
	
	public Position pos;
	public ArrayList<Vector2d> path; 
	
	public PathComponent(ArrayList<Vector2d> path){
		this.path = new ArrayList<Vector2d>(path);
	}

}
