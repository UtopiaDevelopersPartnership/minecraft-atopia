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
import fn.utopia.mod.blockchain.net.RpcClient;
import fn.utopia.mod.blockchain.profile.Profile;
import fn.utopia.mod.blockchain.profile.Robotic;

public class BlockchainManager {

	protected static final String URL = System.getProperty("url",
			"ws://127.0.0.1:3000");

	protected RpcClient client;
	
	public Queue<Action> actionQueue = new ConcurrentLinkedQueue<>();
	public DataCache dataCache = new DataCache();
	public Profile activeProfile = new Robotic(); 

	public BlockchainManager() {
		
	}

	public void initRpcClient(String hostAddress) {
		try {
			client = new RpcClient(this, new URI(URL));
			client.start();
		} catch (URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void echo(String message) {
		List<Object> params = new ArrayList<>();
		params.add(message);
		client.sendJson("server_echo", params);
	}

	public void registerPlayerEvent(String userName, String eventType) {
		List<Object> params = new ArrayList<>();
		params.add(userName);
		params.add(eventType);
		client.sendJson("playerEvents_register", params);
	}

	public void handleIncoming(String method, Map<String,Object> result) {
		switch (method) {
		case "server_echo":
			System.out.println("Echo received: " + result.get("message"));
			break;
		case "playerEvents_register":
			PlayerEventData ped = new PlayerEventData();
			ped.userName = (String)result.get("userName");
			ped.eventType = (String)result.get("eventType");
			System.out.println(ped);
			dataCache.addEventData(ped);
			System.out.println(dataCache);
			actionQueue.add(new BroadcastPlayerEventAction(this, ped));
			break;
		default:
			System.out.println("Unhandled event: " + method);
		}
	}
}
