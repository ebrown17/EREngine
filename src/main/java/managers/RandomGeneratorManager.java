package managers;

import java.util.HashSet;
import java.util.Random;

public class RandomGeneratorManager {
	
	private Random masterRandom;
	private HashSet<Long> usedLongs = new HashSet<Long>();
	
	public RandomGeneratorManager(String seed){
		this.masterRandom = new Random(seed.hashCode());
	}
	
	public Random getRandom(){		
		return new Random(masterRandom.nextLong());
	}
	
	public Long getNewSeed(){
		
		Long seed = masterRandom.nextLong();
		while(!usedLongs.add(seed)){
			 seed = masterRandom.nextLong();
		}
		
		return seed;
	}
	
	

}
