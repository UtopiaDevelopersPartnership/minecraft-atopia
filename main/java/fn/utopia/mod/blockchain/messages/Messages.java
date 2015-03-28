package fn.utopia.mod.blockchain.messages;

import fn.utopia.mod.blockchain.messages.categories.Common;
import fn.utopia.mod.blockchain.messages.categories.Law;
import fn.utopia.mod.blockchain.messages.categories.PlayerEvents;

public class Messages {
	
	protected final String pre = "[Atopia] ";
	
	// Player events
	
	public Law law = new Law(pre);
	public PlayerEvents playerEvents = new PlayerEvents(pre);
	public Common common = new Common(pre);
	
}
