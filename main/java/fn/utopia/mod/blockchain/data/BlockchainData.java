package fn.utopia.mod.blockchain.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BlockchainData {	
	
	public static class PlayerEventData implements Serializable {
		
		private static final long serialVersionUID = 1L;
		// The name of the user in question.
		public String userName;
		// The type of event
		// "login" - the player is logging in.
		// "logout" - the player is logging out.
		public String eventType;
		// The time of this event.
		public int timeStamp;
	}
	
	public static class LawData implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		public String creator;
		// This is the zone where the law is in effect.
		public double posX, posZ, radius;
		public int timeStamp;
		public boolean mining;
	}
	
	public static class PollData implements Serializable {
		
		private static final long serialVersionUID = 1L;
		// The auto-assigned id of the poll.
		public int id;
		// The short description of the poll.
		public String description;
		// The time when the poll was registered.
		public int timeStamp;
		// The duration of the poll.
		public int duration;
		// The quorum.
		public float quorum;
		// The name of the person initiating the poll.
		public String initiator;
		// The current status.
		// 1 - pending
		// 2 - failed
		// 3 - succeeded
		public int status;
		// Map of voter names and whether or not they voted.
		public Map<String,Boolean> hasVoted;
	}
	
}
