package entities;

import java.util.ArrayList;

import components.Component;

public class Entity {

	private Integer id;
	private ArrayList<? extends Component> components;
	
	public Entity(Integer id){
		this.id=id;
	}
	
	/*public <T extends Component> void addComponent(T c){
		if(null == components){
			components = new ArrayList<T>();
		}
		components.add((T)c);
	}*/
	
	public Integer getId(){
		return id;
	}
	
	// TODO verify using this wont effect GC when lots of entities are cleansed
/*	public void cleanseEntity(){
		id = null;
		components = null;
	}
	
	/// not sure if this functionality should be here or only in EntityManager
	public <T extends Component> ArrayList<T> getAllComponents(){
		return components;
	}*/
	/*
	public <T extends Component> T getComponent(Class<T> component){
		
		if(components.contains(component)){
			return components.
		}
	}*/
}
