package managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import components.Component;

public class EntityManager {
	
	int lowestUnassingedID = 1;
	List<Integer> entities;	
	/*
	 * Map of Components with map of entity id's that contain an instance of this component  
	 */
	HashMap<Class<?>,HashMap<Integer, ? extends Component>> componentMap;
	
	public EntityManager(){
		
		entities = new ArrayList<Integer>();
		componentMap = new HashMap<Class<?>,HashMap<Integer, ? extends Component>>();
		
	}
	
	public int createEntity(){
		
		if(lowestUnassingedID < Integer.MAX_VALUE){
			entities.add(lowestUnassingedID);
			return lowestUnassingedID++;
		}
		else {
			for(int i=1; i < Integer.MAX_VALUE;i++){
				if(!entities.contains(i)){
					entities.add(i);
					return i;
				}
			}
			throw new Error("Exception: no Entity IDs are available.");
		}		
	}
	
	public void killEntity(Integer entity){
		
		entities.remove(entity);
		for(HashMap<Integer, ? extends Component> map: componentMap.values()){
			map.remove(entity);
		}
		
	}
	
	public <T extends Component> void addComponent(int entity, T component){
		HashMap<Integer, ? extends Component> entitesWithComponent = componentMap.get(component.getClass());
		if(entitesWithComponent == null){
			entitesWithComponent = new HashMap<Integer,T>();
			componentMap.put(component.getClass(), entitesWithComponent);
		}
		((HashMap<Integer,T>)entitesWithComponent).put(entity, component);
		
	}
	
	public <T extends Component> T getComponent(int entity,Class<T> componentType){
		HashMap<Integer, ? extends Component> component = componentMap.get( componentType );
		   
		if( component == null) throw new IllegalArgumentException( "Exception: there are no entities with a Component of class: "+componentType );
		   
		T result = componentType.cast(component.get(entity));
		if( result == null ) throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class missing: "+componentType );

		return result;
	}
	
	public <T extends Component> Collection<T> getAllComponentsOfType(Class<T> componentType){
		
			HashMap<Integer, ? extends Component> entitesWithComponent = componentMap.get(componentType);

			if (entitesWithComponent == null)
				return new LinkedList<T>();
			
			
			return (Collection<T>) entitesWithComponent.values();
		
	}

	
}
