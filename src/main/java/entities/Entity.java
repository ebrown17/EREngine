package entities;

import java.util.ArrayList;

import components.Component;

public class Entity {

	private Integer id;
	private ArrayList<Class<?>> components;
	
	public Entity(Integer id){
		this.id=id;
	}
	
	public <T extends Component> void addComponent(Class<T> c){
		if(null == components){
			components = new ArrayList<Class<?>>();
		}
		components.add(c);
	}
	
	public Integer getId(){
		return id;
	}
	
	// TODO verify using this wont effect GC when lots of entities are cleansed
	public void cleanseEntity(){
		id = null;
		components = null;
	}
	
	/// not sure if this functionality should be here or only in EntityManager
/*	public <T extends Component> ArrayList<Class<?>> getAllComponents(){
		return components;
	}
	
	public <T extends Component> T getComponent(Class<T> component){
		
		if(components.contains(component)){
			return components.
		}
	}*/
}
