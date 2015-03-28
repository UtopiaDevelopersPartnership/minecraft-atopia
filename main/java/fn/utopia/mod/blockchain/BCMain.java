package fn.utopia.mod.blockchain;

import java.net.URI;

public class BCMain {
	
	protected static final String uri = System.getProperty("url",
			"ws://127.0.0.1:3000");
	static final int NUM_OPS = 1;
	
	public static void main(String[] args) {
		BlockchainManager bm = new BlockchainManager(uri);
		bm.init();
		for(int i = 0; i < NUM_OPS; i++){
			bm.getNetwork().createLaw("Tester" + i,1.1, 5.4, 25.0);
			//bm.getNetwork().registerPlayerEvent("AnotherTest", "logout");
		}
	}
	
}
