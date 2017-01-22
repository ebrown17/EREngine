package util;

public enum RenderPriority {
	BASE_LAYER(1),
	MIDDLE_LAYER(2),
	TOP_LAYER(3);
	
	public int priority;
	
	RenderPriority(int priority){
		this.priority=priority;
	}
	
}
