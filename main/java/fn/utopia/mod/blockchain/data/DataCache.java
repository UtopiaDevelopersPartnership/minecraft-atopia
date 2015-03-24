package fn.utopia.mod.blockchain.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;

/**
 * Simple cache mirrors the data on the blockchain. Will perhaps use a MapDB backend 
 * or something later.
 * 
 * @author androlo
 *
 */
public class DataCache {
	// TODO more sophisticated maybe.
	protected Object edLock = new Object();
	
	protected List<PlayerEventData> eventData = new ArrayList<>();
	
	public void addEventData(PlayerEventData data){
		synchronized (edLock){
			eventData.add(data);
		}
	}
	
	public PlayerEventData[] getEventData(){
		synchronized (edLock){
			PlayerEventData[] data = new PlayerEventData[eventData.size()];
			eventData.toArray(data);
			return data;
		}
	}
	
}
