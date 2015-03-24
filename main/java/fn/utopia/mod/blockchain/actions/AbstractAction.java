package fn.utopia.mod.blockchain.actions;

import fn.utopia.mod.blockchain.BlockchainManager;

public abstract class AbstractAction<T> implements Action {

	protected BlockchainManager bm;
	protected String id;
	protected T params;
	
	public AbstractAction(BlockchainManager bm, T params){
		this.bm = bm;
		this.params = params;
	}
	
	@Override
	public String getId() {
		return id;
	}

	public abstract boolean execute();

}
