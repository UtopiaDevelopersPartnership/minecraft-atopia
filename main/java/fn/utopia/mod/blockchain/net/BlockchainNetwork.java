package fn.utopia.mod.blockchain.net;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.blockchain.actions.impl.BroadcastLawAbandoned;
import fn.utopia.mod.blockchain.actions.impl.BroadcastLawCreated;
import fn.utopia.mod.blockchain.actions.impl.BroadcastPlayerEventAction;
import fn.utopia.mod.blockchain.data.BlockchainData.LawData;
import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;

public class BlockchainNetwork {
	
	protected String uri;
	
	protected BlockchainManager bcm;
	protected RpcClient client;
	
	public BlockchainNetwork(BlockchainManager bcm, String uri){
		this.bcm = bcm;
		this.uri = uri;
	}
	
	public void init(){
		try {
			this.client = new RpcClient(this, new URI(uri));
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public RpcClient getClient(){
		return client;
	}
	
	/**************************** server calls ********************************/
	
	public void registerPlayerEvent(String userName, String eventType) {
		List<Object> params = new ArrayList<Object>();
		params.add(userName);
		params.add(eventType);
		client.sendJson("playerEvents_register", params);
	}
	
	public void createLaw(String creator, double posX, double posZ, double radius) {
		List<Object> params = new ArrayList<Object>();
		params.add(creator);
		params.add(posX);
		params.add(posZ);
		params.add(radius);
		client.sendJson("laws_create", params);
	}
	
	public void abandonLaw(String creator) {
		List<Object> params = new ArrayList<Object>();
		params.add(creator);
		client.sendJson("laws_abandon", params);
	}
	
	/**************************** handle incoming ********************************/
	
	// Keep these here for now.
	public void handleIncoming(String method, Map<String,Object> result) {
		if("server_echo".equals(method)){
			System.out.println("Echo received: " + result.get("message"));
		} else if ("playerEvents_register".equals(method)){
			_incomingPlayerEvents(result);
		} else if ("laws_create".equals(method)) {
			_incomingLawsCreate(result);
		} else if ("laws_abandon".equals(method)){
			_incomingLawsAbandon(result);
		} else {
			System.out.println("Unknown or un-handled method: " + method);
		}
	}
	
	protected void _incomingPlayerEvents(Map<String,Object> result){
		PlayerEventData ped = new PlayerEventData();
		ped.userName = (String)result.get("userName");
		ped.eventType = (String)result.get("eventType");
		bcm.getDataCache().addEventData(ped);
		bcm.queueAction(new BroadcastPlayerEventAction(bcm, ped));
	}
	
	protected void _incomingLawsCreate(Map<String,Object> result){
		LawData ld = new LawData();
		ld.creator = (String)result.get("creator");
		Object posX = result.get("posX");
		Object posZ = result.get("posZ");
		Object radius = result.get("radius");
		if(posX instanceof Long){
			ld.posX = ((Long)posX).doubleValue();
		} else {
			ld.posX = (Double)posX;
		}
		if(posZ instanceof Long){
			ld.posZ = ((Long)posZ).doubleValue();
		} else {
			ld.posZ = (Double)posZ;
		}
		if(radius instanceof Long){
			ld.radius = ((Long)radius).doubleValue();
		} else {
			ld.radius = (Double)radius;
		}
		LawData currentLd = bcm.getLaws().getLawAt(ld.posX, ld.posZ);
		// If someone beat him to it.
		if(currentLd == null){
			bcm.getDataCache().addLawData(ld);
			bcm.queueAction(new BroadcastLawCreated(bcm, ld, ld.creator, true));
		} else {
			bcm.queueAction(new BroadcastLawCreated(bcm, currentLd, ld.creator, false));
		}
	}
	
	protected void _incomingLawsAbandon(Map<String,Object> result){
		String creator = (String)result.get("creator");
		LawData removed = bcm.getDataCache().removeLawData(creator);
		boolean success = removed != null;
		bcm.queueAction(new BroadcastLawAbandoned(bcm, creator, success));
	}
	
}
