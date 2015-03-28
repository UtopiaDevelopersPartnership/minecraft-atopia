package fn.utopia.mod.main;

import java.net.URI;

import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.commands.Law;
import fn.utopia.mod.commands.Poll;
import fn.utopia.mod.events.FMLEventListener;
import fn.utopia.mod.events.ServerTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class AtopiaMod {

	@Instance(Reference.MOD_ID)
	public static AtopiaMod instance;
	public BlockchainManager bcm;

	protected AtopiaConfig config = new AtopiaConfig();

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {

		config.load(event.getSuggestedConfigurationFile());
		bcm = new BlockchainManager(config.getWebsocketURI());
		bcm.init();
		
		FMLCommonHandler.instance().bus().register(new FMLEventListener());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new Law());
	}

}
