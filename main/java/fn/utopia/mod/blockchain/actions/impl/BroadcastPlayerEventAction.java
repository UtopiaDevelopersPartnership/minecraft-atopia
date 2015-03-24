package fn.utopia.mod.blockchain.actions.impl;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.fml.server.FMLServerHandler;
import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.blockchain.actions.AbstractAction;
import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;
import fn.utopia.mod.blockchain.profile.Profile;
import fn.utopia.mod.main.AtopiaMod;

public class BroadcastPlayerEventAction extends AbstractAction<PlayerEventData>{
	
	public BroadcastPlayerEventAction(BlockchainManager bm,
			PlayerEventData params) {
		super(bm, params);
	}

	@Override
	public boolean execute() {
		MinecraftServer mcs = FMLServerHandler.instance().getServer();
		List players = mcs.getConfigurationManager().playerEntityList;
		
		if (mcs == null || players.isEmpty()){
			return false;
		}
		
		for (int i = 0; i < players.size(); i++)
		{
			EntityPlayerMP player = (EntityPlayerMP) players.get(i);
			Profile profile = AtopiaMod.instance.bm.activeProfile;
			player.addChatMessage(new ChatComponentTranslation(profile.getPlayerEventMessage(params.userName, params.eventType)));
		}
		return true;
	}

}
