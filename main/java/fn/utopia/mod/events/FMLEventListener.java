package fn.utopia.mod.events;

import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.main.AtopiaMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Listen for minecraft events.
 * 
 * @author androlo
 *
 */
public class FMLEventListener {

	public static final String LOGIN_EVENT = "login";
	public static final String LOGOUT_EVENT = "logout";
		
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event)
	{
		AtopiaMod.instance.bm.registerPlayerEvent(event.player.getName(), LOGIN_EVENT);
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedOutEvent event)
	{
		AtopiaMod.instance.bm.registerPlayerEvent(event.player.getName(), LOGOUT_EVENT);
	}
	
}
