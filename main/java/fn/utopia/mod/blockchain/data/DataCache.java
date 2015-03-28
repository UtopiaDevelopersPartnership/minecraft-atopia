package fn.utopia.mod.blockchain.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.locks.ReentrantLock;

import org.mapdb.Atomic;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import fn.utopia.mod.blockchain.data.BlockchainData.LawData;
import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;
import fn.utopia.mod.blockchain.data.BlockchainData.PollData;

/**
 * Keeps a local copy of the blockchain data for fast lookup.
 * 
 * @author androlo
 *
 */
public class DataCache {
	
	protected DB db;
	protected ConcurrentNavigableMap<Integer,PlayerEventData> playerEvents;
	protected Atomic.Integer peKey;
	
	protected ConcurrentMap<String,LawData> laws;
	protected ConcurrentNavigableMap<Integer,PollData> polls;
	
	public DataCache(){
		db = DBMaker.newMemoryDB().transactionDisable().cacheSize(256).make();
		
		playerEvents = db.getTreeMap("playerEvents");
		peKey = db.getAtomicInteger("peKey");
		
		laws = db.getHashMap("laws");
	}
	
	public void addEventData(PlayerEventData data){
		playerEvents.put(peKey.incrementAndGet(), data);
	}
	
	public ConcurrentNavigableMap<Integer,PlayerEventData> getPlayerEvents(){
		return playerEvents;
	}
	
	public PlayerEventData getPlayerEvent(int index){
		return playerEvents.get(index);
	}
	
	public void addPollData(int pollId, PollData data){
		polls.put(pollId, data);
	}
	
	public PollData getPoll(int index){
		return polls.get(index);
	}
	
	public int getPollSize(){
		return polls.size();
	}
	
	public void addLawData(LawData data){
		laws.put(data.creator, data);
	}
	
	public LawData removeLawData(String creator){
		return laws.remove(creator);
	}
	
	public ConcurrentMap<String,LawData> getLaws(){
		return laws;
	}
	
	public LawData getLaw(String creator){
		return laws.get(creator);
	}
	
	
}
