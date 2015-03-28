package fn.utopia.mod.blockchain.messages.categories;

import fn.utopia.mod.util.AMath.Direction;

public class Law {
		
	protected String pre;
	
	public Law(String pre){
		this.pre = pre;
	}
	
	public String overlap(String owner) {
		return pre + "This area is currently ruled over by '" + owner + "'.";
	}

	public String createdSuccess(String owner, String biome) {
		return pre + "Warning. The mighty " + owner + " just claimed a tract of prime " + biome + ".";
	}
	
	public String createdFail(String name) {
		return pre + "Hmm, looks like " + name + " beat you to it.";
	}

	public String creating(String name, boolean them) {
		if(them){
			return pre + "You start laying down the law...";
		}
		return pre + name + " starts to look intimidating.";
	}

	public String alreadyExist() {
		return pre + "You must first abandon your previous claim.";
	}

	public String abandoning() {
		return pre + "You start to abandon your previous claim.";
	}

	public String abandonedSuccess(String name) {
		return pre + name + " just abandoned their claim. That land is now lawless.";
	}
	
	public String abandonedFail() {
		return pre + "You have no claims to abandon.";
	}
	
	public String direction(String name, Direction dir, double distance, boolean them) {
		String dist;
		if(distance < 50){
			dist = "very close to where you're standing.";
		} else if (distance < 200){
			dist = "a short distance away.";
		} else if (distance < 500){
			dist = "a long distance away.";
		} else if (distance < 1000){
			dist = "a very long distance away.";
		} else {
			dist = "far, far away.";
		}
		
		if(them){
			return pre + "Your law can be found to the " + dir.getLowerCase() + ". It is " + dist;
		}
		String names = name.endsWith("s") ? name + "'" : name + "'s";
		return pre + names + " law can be found to the " + dir.getLowerCase() + ". It is " + dist;
	}
	
	public String rightHere(String name, boolean them) {
		if(them){
			return pre + "Your law is active here.";
		}
		String names = name.endsWith("s") ? name + "'" : name + "'s";
		return pre + names + " law is active here.";
	}
	
	public String lawless() {
		return pre + "This land is lawless.";
	}
	
	public String notOverworld() {
		return pre + "You may only claim land in the overworld.";
	}
	
	public String haveNoLaw(String name, boolean them){
		if(them){
			return pre + "You have not declared any law.";
		}
		return pre + name + " has not declared any law.";
	}
}
