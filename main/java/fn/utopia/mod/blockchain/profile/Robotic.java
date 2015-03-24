package fn.utopia.mod.blockchain.profile;

public class Robotic implements Profile {
	
	public static final String pre = "[Blockchain] ";
	
	@Override
	public String getPlayerEventMessage(String userName, String eventType) {
		return pre + "*bzzz* Human entity '" + userName + ("login".equals(eventType) ? "' registered. System activated." : "' de-registered.");
	}
	
}
