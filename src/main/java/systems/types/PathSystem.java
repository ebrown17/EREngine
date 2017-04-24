package systems.types;

import graphs.types.GridGraph;
import managers.EntityManager;
import systems.SystemProcessor;

public class PathSystem  implements SystemProcessor{
	
	private GridGraph currentMap;
	private EntityManager entityManger;
	
	public PathSystem(EntityManager entityManager){
		this.entityManger=entityManager;
	}

	@Override
	public void processOneTick(long lastFrameTick) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCurrentMap(GridGraph current){
		currentMap=current;
	}

}
