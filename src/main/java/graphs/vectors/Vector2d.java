package graphs.vectors;

public class Vector2d {
	
	public int x,y;	

	public Vector2d(int x, int y){
		setXY(x,y);
	}
	
	public Vector2d(Vector2d vector){
		setXY(vector.x,vector.y);
	}
	
	public void setXY(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public boolean sameVector(Vector2d vector){		
		return ((vector.x == x) && (vector.y == y));
	}
	
}
