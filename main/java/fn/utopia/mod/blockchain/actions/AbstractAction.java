package fn.utopia.mod.blockchain.actions;

import fn.utopia.mod.blockchain.BlockchainManager;

public abstract class AbstractAction implements Action {

	protected BlockchainManager bcm;
	
	public AbstractAction(BlockchainManager bcm){
		this.bcm = bcm;
	}

}
