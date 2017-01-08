package managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import components.Component;
import entities.Entity;

public class EntityManager {
	
	int lowestUnassingedID = 1;
	List<Entity> activeEntities;
	List<Integer> entityIdPool;
	/*
	 * Map of Components with map of entity id's that contain an instance of this component  
	 */
	HashMap<Class<?>,HashMap<Integer, ? extends Component>> componentMap;
	
	HashMap<Class<?>,HashMap<? extends Component, Entity>> entityMap;
	
	public EntityManager(){
		
		activeEntities = new ArrayList<Entity>();
		entityIdPool = new ArrayList<Integer>();
		componentMap = new HashMap<Class<?>,HashMap<Integer, ? extends Component>>();
		
	}
	
	public Entity createEntity(){
		
		if(lowestUnassingedID < Integer.MAX_VALUE){
			entityIdPool.add(lowestUnassingedID);
			Entity entity = new Entity(lowestUnassingedID++);
			activeEntities.add(entity);
			return entity;
		}
		else {
			for(int i=1; i < Integer.MAX_VALUE;i++){
				if(!entityIdPool.contains(i)){
					Entity entity = new Entity(i);
					activeEntities.add(entity);
					return entity;
				}
			}
			throw new Error("Exception: no Entity IDs are available.");
		}		
	}
	
	public void removeActiveEntity(Entity entity){
		activeEntities.remove(entity);
		
		for( HashMap<Integer, ? extends Component> store : componentMap.values() )
		{
			store.remove(entity.getId());
		}
	}
	
	public <T extends Component> void addComponent(Entity entity, T component){
		HashMap<Integer, ? extends Component> entitesWithComponent = componentMap.get(component.getClass());
		if(entitesWithComponent == null){
			entitesWithComponent = new HashMap<Integer,T>();
			componentMap.put(component.getClass(), entitesWithComponent);
		}
		((HashMap<Integer,T>)entitesWithComponent).put(entity.getId(), component);
		
	}
	
	public <T extends Component> T getComponent(Entity entity,Class<T> componentType){
		HashMap<Integer, ? extends Component> component = componentMap.get( componentType );
		   
		if( component == null) throw new IllegalArgumentException( "Exception: there are no entities with a Component of class: "+componentType );
		   
		T result = componentType.cast(component.get(entity.getId()));
		if( result == null ) throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class missing: "+componentType );

		return result;
	}
	
	public <T extends Component> Collection<T> getAllComponentsOfType(Class<T> componentType){
		
			HashMap<Integer, ? extends Component> entitesWithComponent = componentMap.get(componentType);

			if (entitesWithComponent == null)
				return new LinkedList<T>();
			
			
			return (Collection<T>) entitesWithComponent.values();
		
	}

	public <T extends Component> Collection<Entity> getAllEntitiesPossesingComponent(Class<T> componentType){
		
		HashMap<Integer, ? extends Component> entitesWithComponent = componentMap.get(componentType);
		if (entitesWithComponent == null)
			return new LinkedList<Entity>();
		
		Collection<Integer> ids = entitesWithComponent.keySet();
		
		Collection<Entity> entities = new ArrayList<Entity>();
		
		for(Integer id: ids ){
			for(Entity entity : activeEntities){
				if(id == entity.getId()){
					entities.add(entity);
				}
			}			
		}
		return entities;
	}
	
	
}
