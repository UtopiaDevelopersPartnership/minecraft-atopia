package fn.utopia.mod.blockchain.messages.categories;

public class PlayerEvents {
	protected String pre;
	
	public PlayerEvents(String pre){
		this.pre = pre;
	}
	
	public String event(String userName, String eventType) {
		return pre + "User: '" + userName + "' just logged " + ("login".equals(eventType) ? "in." : "out.");
	}
}
