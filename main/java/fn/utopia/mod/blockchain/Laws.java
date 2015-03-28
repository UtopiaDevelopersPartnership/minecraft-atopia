package fn.utopia.mod.blockchain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import fn.utopia.mod.blockchain.data.BlockchainData.LawData;
import fn.utopia.mod.util.AMath;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

public class Laws {
	
	static final double BASE_CHECK_RADIUS = 500.0;
	static final double BASE_LAW_RADIUS = 30.0;
	
	public String getMessage(){
		return "";
	}
	
	protected BlockchainManager bm;
	
	
	public Laws(BlockchainManager bm){
		this.bm = bm;
	}
	
	/**
	 * Find all the laws around the player that are within the distance BASE_CHECK_RADIUS.
	 * 
	 * @param player The player
	 * @return
	 */
	public List<LawData> findLaws(EntityPlayerMP player){
		return findLaws(player,BASE_CHECK_RADIUS);
	}
	
	/**
	 *  Finds all laws active within a certain distance of the player.
	 *  
	 * @param player 
	 * @param radius
	 * @return All laws within the region.
	 */
	public List<LawData> findLaws(EntityPlayerMP player, double radius){
		
		List<LawData> list = new ArrayList<LawData>();
		
		double pX = player.posX;
		double pZ = player.posZ;
		
		ConcurrentMap<String,LawData> laws = bm.dataCache.getLaws();
		Iterator<Entry<String,LawData>> it = laws.entrySet().iterator();
		
		while(it.hasNext()){
			Entry<String,LawData> entry = it.next();
			LawData ld = entry.getValue();
			// If the new law would interfere with another man's law, we disallow.
			// Later this will be resolved differently.
			if(!AMath.circlesDisjoint(pX,pZ,radius,ld.posX,ld.posZ,ld.radius)){
				list.add(ld);
			}
		}
		return list;
	}
	
	/**
	 * Get the law of a given player.
	 * 
	 * @param player
	 * @param name
	 * @return
	 */
	public LawData getLaw(String name){
		return bm.getDataCache().getLaw(name);	
	}
	
	/**
	 * Create a new law. If there is already a law in place in this particular area,
	 * it will be returned. 
	 * 
	 * @param player
	 * @return
	 */
	public LawData createLaw(EntityPlayerMP player){
		double pX = player.posX;
		double pZ = player.posZ;
		
		ConcurrentMap<String,LawData> laws = bm.dataCache.getLaws();
		Iterator<Entry<String,LawData>> it = laws.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,LawData> entry = it.next();
			LawData ld = entry.getValue();
			// If the new law would interfere with another man's law, we disallow.
			// Later this will be resolved differently.
			if(!AMath.circlesDisjoint(pX,pZ,BASE_LAW_RADIUS,ld.posX,ld.posZ,ld.radius)){
				// If the law was made by the own player, just overwrite.
				if(ld.creator != player.getName()){
					return ld;
				}
			}
		}
		// Create the new LawData object and pass it to the blockchain.
		bm.getNetwork().createLaw(player.getName(),player.posX,player.posZ,BASE_LAW_RADIUS);
		return null;
	}
	
	public LawData abandonLaw(String name){
		LawData ld = bm.getDataCache().getLaw(name);
		if(ld != null){
			bm.getNetwork().abandonLaw(name);
		}
		return ld;
	}
	
	/**
	 * Get the active law at a given position.
	 * 
	 * @param pX
	 * @param pZ
	 * @return
	 */
	public LawData getLawAt(double pX, double pZ){
		ConcurrentMap<String,LawData> laws = bm.dataCache.getLaws();
		Iterator<Entry<String,LawData>> it = laws.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,LawData> entry = it.next();
			LawData ld = entry.getValue();
			
			double x = pX - ld.posX;
			double z = pZ - ld.posZ;
			// If the distance from the player to the center of the law zone is
			// less then the radius, it's a match.
			if(x*x + z*z < ld.radius*ld.radius){
				return ld;
			}
		}
		return null;
	}
	
}
