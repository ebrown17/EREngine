package maps;

import java.awt.Color;

public enum TileType {	
	
	START("S",Color.GREEN,false),
	END("E",Color.RED,false),
	CLEAR(" ",Color.WHITE,false),
	PATH("o",Color.CYAN,false),
	WALL("I",Color.BLACK,true);
	
	public final String chr;
	public final Color color;
	public final boolean solid;
	
	TileType(String chr,Color color, boolean solid){
		this.chr=chr;
		this.color = color;
		this.solid = solid;
	}
}
