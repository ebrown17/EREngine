package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import components.types.BaseRenderable;
import components.types.MiddleRenderable;
import components.types.Renderable;
import components.types.TopRenderable;

public enum RenderPriority {
	
	INSTANCE;
	
	private enum  RenderLayers {
		BASE_LAYER(BaseRenderable.class,0),
		MIDDLE_LAYER(MiddleRenderable.class,1),
		TOP_LAYER(TopRenderable.class,2);
		
		private Class<? extends Renderable> layer;
		private int priority;
		
		private RenderLayers(Class<? extends Renderable> renderableLayer, int priority){
			this.layer = renderableLayer;
			this.priority = priority;
		}
	}

	private final ArrayList<Class<? extends Renderable>> RENDERLAYERS = new ArrayList<Class<? extends Renderable>>();
	
	private RenderPriority(){
		for(RenderLayers layer: RenderLayers.values()){
			RENDERLAYERS.add(layer.priority, layer.layer);
		}
	}

	/**
	 * @return	An unmodifiable List of Renderable.class objects.
	 * 			Used to retrieve Renderable components from <i>EntityManger.getAllComponentsOfType
	 */
	public List<Class<? extends Renderable>> getRenderLayers(){
		return Collections.unmodifiableList(RENDERLAYERS);
	}
	
}
