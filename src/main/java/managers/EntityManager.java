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
import util.EntityGenerator;

public class EntityManager {

	private EntityGenerator generator;
	/*
	 * Map of Components with map of entity id's that contain an instance of this component
	 */
	private HashMap<Class<?>,HashMap<Entity, ? extends Component>> componentMap;


	public EntityManager(){
		generator = new EntityGenerator("Entity");
		componentMap = new HashMap<Class<?>,HashMap<Entity, ? extends Component>>();
	}

	public String getPoolSizes(){
		return "Entity pool: " +generator.getEntityPoolSize() + " | activeEntities: " + generator.getActiveSize()+ " | entityIdPools: " + generator.getIdPoolSize();
	}


	public Entity retrieveEntity(){
		return generator.getEntity();
	}


	public void recycleActiveEntity(Entity entity){
		generator.recycleEntity(entity);

		for( HashMap<Entity, ? extends Component> store : componentMap.values() )
		{
			store.remove(entity);
		}
	}

	public <T extends Component> void addComponent(Entity entity, T component){
		HashMap<Entity, ? extends Component> entitesWithComponent = componentMap.get(component.getClass());
		if(entitesWithComponent == null){
			entitesWithComponent = new HashMap<Entity,T>();
			componentMap.put(component.getClass(), entitesWithComponent);
		}

		((HashMap<Entity,T>)entitesWithComponent).put(entity, component);

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
		HashMap<Entity, ? extends Component> component = componentMap.get( componentType );

		if( component == null) throw new IllegalArgumentException( "Exception: there are no entities with a Component of class: "+componentType );

		T result = componentType.cast(component.get(entity));
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
		HashMap<Entity, ? extends Component> component = componentMap.get( componentType );

		if( component == null) throw new IllegalArgumentException( "Exception: there are no entities with a Component of class: "+componentType );

		Component result = component.remove(entity);
		if( result == null ) throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class missing: "+componentType );

	}

	public <T extends Component> Collection<T> getAllComponentsOfType(Class<T> componentType){

		HashMap<Entity, ? extends Component> entitesWithComponent = componentMap.get(componentType);

		if (entitesWithComponent == null)
			return Collections.emptyList();

		return (Collection<T>) entitesWithComponent.values();

	}

	public <T extends Component> Collection<Entity> getAllEntitiesPossesingComponent(Class<T> componentType){

		HashMap<Entity, ? extends Component> entitesWithComponent = componentMap.get(componentType);
		if (entitesWithComponent == null)
			return Collections.emptyList();

		return entitesWithComponent.keySet();
	}

}
