package fn.utopia.mod.blockchain;

public class BCMain {

	public static void main(String[] args) {
		BlockchainManager bm = new BlockchainManager();
		bm.initRpcClient("ws://localhost:3000");
		bm.registerPlayerEvent("Tester","login");
	}

}
