package fn.utopia.mod.blockchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fn.utopia.mod.blockchain.actions.Action;
import fn.utopia.mod.blockchain.actions.impl.BroadcastPlayerEventAction;
import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;
import fn.utopia.mod.blockchain.data.DataCache;
import fn.utopia.mod.blockchain.messages.Messages;
import fn.utopia.mod.blockchain.net.BlockchainNetwork;
import fn.utopia.mod.blockchain.net.RpcClient;
import fn.utopia.mod.main.AtopiaMod;

public class BlockchainManager {
	
	// TODO see what values to use.
	static final int ACTION_TICKS = 10;
	
	protected RpcClient client;
	protected BlockchainNetwork bcn;
	
	protected Queue<Action> actionQueue = new ConcurrentLinkedQueue<Action>();
	
	protected DataCache dataCache = new DataCache();
	protected Messages messages = new Messages();
	
	protected Laws laws;
	
	protected int aTicks = 0;
	
	public BlockchainManager(String hostAddress) {
		bcn = new BlockchainNetwork(this, hostAddress);
		laws = new Laws(this);
	}
	
	public void init() {
		bcn.init();
		_loadBlockchainData();
	}
	
	public void tick(){
		if(++aTicks == ACTION_TICKS){
    		aTicks = 5;
    		while(!actionQueue.isEmpty()){
    			actionQueue.poll().execute();
    		}
    	}
	}
	
	public void queueAction(Action a){
		actionQueue.add(a);
	}
	
	public Laws getLaws(){
		return laws;
	}
	
	public Messages getMessages(){
		return messages;
	}
	
	public BlockchainNetwork getNetwork(){
		return bcn;
	}
	
	public DataCache getDataCache(){
		return dataCache;
	}
	
	protected void _loadBlockchainData(){
		// TODO fill up with all the blockchain stuff.
	}
}