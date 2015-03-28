package fn.utopia.mod.main;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class AtopiaConfig {
	
	static final String RPC = "rpc";
	
	static final String defaultWebsocketURI = "ws://127.0.0.1:3000";
	
	Configuration config;
	
	protected String websocketURI;
	
	public AtopiaConfig(){
		
	}
	
	public void load(File configFile){
		config =  new Configuration(configFile);
		config.load();
		websocketURI = config.getString(RPC, "websocket_URI", defaultWebsocketURI, "The default websocket endpoint.");
		
		// TODO blockchain stuff
		
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	public String getWebsocketURI(){
		return websocketURI;
	}
}
