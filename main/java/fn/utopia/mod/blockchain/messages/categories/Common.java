package fn.utopia.mod.blockchain.messages.categories;

public class Common {
	protected String pre;
	
	public Common(String pre){
		this.pre = pre;
	}
	
	public String noDouble(String param) {
		return pre + "Error: Not a double: " + param;
	}
}
