package managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import components.Component;
import components.types.Renderable;
import entities.Entity;

public class EntityManager {
	
	private final int POOL_SIZE_INCRMENT = 10000;
	private final int INTIAL_POOL_SIZE = 10000;
	private int lowestUnassingedID = 1;
	private List<Entity> activeEntities;
	private ArrayDeque<Entity> entityPool;
	private List<Integer> entityIdPool;
	
	/*
	 * Map of Components with map of entity id's that contain an instance of this component  
	 */
	private HashMap<Class<?>,HashMap<Integer, ? extends Component>> componentMap;
	
	public EntityManager(){
		activeEntities = new ArrayList<Entity>(INTIAL_POOL_SIZE);
		entityPool = new ArrayDeque<Entity>(INTIAL_POOL_SIZE);
		entityIdPool = new ArrayList<Integer>(INTIAL_POOL_SIZE);
		componentMap = new HashMap<Class<?>,HashMap<Integer, ? extends Component>>();
		initEntityPool();
	}
	
	public String getPoolSizes(){
		return "Entity pool: " +entityPool.size() + " | activeEntities: " + activeEntities.size()+ " | entityIdPools: " + entityIdPool.size();
	}
	
	private void initEntityPool(){
		while(lowestUnassingedID <= POOL_SIZE_INCRMENT){
			if(lowestUnassingedID < Integer.MAX_VALUE){
				entityIdPool.add(lowestUnassingedID);
				Entity entity = new Entity(lowestUnassingedID++);
				entityPool.add(entity);
			}
		}
	}
	
	private void refillEntityPool(){
		for(int i=0;i< POOL_SIZE_INCRMENT; i++){
			if(lowestUnassingedID < Integer.MAX_VALUE){
				entityIdPool.add(lowestUnassingedID);
				Entity entity = new Entity(lowestUnassingedID++);
				entityPool.add(entity);
				
			}
			else {
				for(int j=1; j < Integer.MAX_VALUE;j++){
					if(!entityIdPool.contains(j)){
						entityIdPool.add(j);
						Entity entity = new Entity(j);
						entityPool.add(entity);
						
					}
				}
				throw new Error("Exception: no Entity IDs are available.");
			}
		}
	}
	
	public Entity retrieveEntity(){
		if(!entityPool.isEmpty()){
			Entity entity = entityPool.pop();
			activeEntities.add(entity);
			return entity;
		}
		else {
			refillEntityPool();
			if(!entityPool.isEmpty()){
				Entity entity = entityPool.pop();
				activeEntities.add(entity);
				return entity;
			}
		}
		throw new Error("Exception: Entity pool can't be filled.");
	}
	/*
	 * Creates and returns one entity without using entity pool
	 */
	@Deprecated
	private Entity createEntity(){
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
	
	public void recycleActiveEntity(Entity entity){
		activeEntities.remove(entity);
		entityPool.add(entity);
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
	
	/**
	 * @param 	entity that may contain the componentType
	 * 
	 * @param	componentType is the implemented class of <code>Component</code> 		
	 * 
	 * @return	If the <i>Entity</i> contains the <code>componentType</code>, the <code>Component</code> 
	 * 			is returned. If the <i>Entity</i> does not contain the specified <code>componentType</code> an
	 * 			exception is thrown.  Currently <i>Entities</i> can't contains more than one 
	 * 			<code>Component</code> of the same type.
	 * 
	 * @throws	IllegalArgumentException is thrown when no entities contain the specified <code>componentType</code> or if
	 * 			the <i>Entity</i> does not contain the specified <code>componentType</code>
	 */	
	public <T extends Component> T getComponent(Entity entity,Class<T> componentType){
		HashMap<Integer, ? extends Component> component = componentMap.get( componentType );
		   
		if( component == null) throw new IllegalArgumentException( "Exception: there are no entities with a Component of class: "+componentType );
		   
		T result = componentType.cast(component.get(entity.getId()));
		if( result == null ) throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class missing: "+componentType );

		return result;
	}
	
	/**
	 * @param 	entity that may contain the componentType
	 * 
	 * @param	componentType is the implemented class of <code>Component</code> 		
	 * 	  
	 * @throws	IllegalArgumentException is thrown when no entities contain the specified <code>componentType</code> or if
	 * 			the <i>Entity</i> does not contain the specified <code>componentType</code>
	 */	
	public <T extends Component> void removeComponent(Entity entity,Class<T> componentType){
		HashMap<Integer, ? extends Component> component = componentMap.get( componentType );
		   
		if( component == null) throw new IllegalArgumentException( "Exception: there are no entities with a Component of class: "+componentType );
		   
		Component result = component.remove(entity.getId());
		if( result == null ) throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class missing: "+componentType );

	}
	
	public <T extends Component> Collection<T> getAllComponentsOfType(Class<T> componentType){
		
			HashMap<Integer, ? extends Component> entitesWithComponent = componentMap.get(componentType);

			if (entitesWithComponent == null)
				return Collections.emptyList();
			
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
