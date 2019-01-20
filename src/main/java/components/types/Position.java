package components.types;

import components.Component;

public class Position implements Component{

	public int x,y;
	
	public Position(int x, int y){
		this.x=x;
		this.y=y;
	}

	public void setPostion(int x,int y){
		this.x = x;
		this.y = y;
	}

}
