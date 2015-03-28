package fn.utopia.mod.blockchain.actions.impl;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.server.FMLServerHandler;
import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.blockchain.actions.AbstractAction;
import fn.utopia.mod.blockchain.data.BlockchainData.LawData;
import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;
import fn.utopia.mod.blockchain.messages.Messages;
import fn.utopia.mod.main.AtopiaMod;

public class BroadcastLawCreated extends AbstractAction{
	
	protected boolean success;
	protected LawData ld;
	protected String name;
	
	public BroadcastLawCreated(BlockchainManager bcm, LawData ld, String name, boolean success) {
		super(bcm);
		this.ld = ld;
		this.name = name;
		this.success = success;
	}

	@Override
	public boolean execute() {
		
		MinecraftServer mcs = FMLServerHandler.instance().getServer();
		
		List players = mcs.getConfigurationManager().playerEntityList;
		
		if (mcs == null || players.isEmpty()){
			return false;
		}
		
		Messages msg = bcm.getMessages();
		
		if(!success){
			for (int i = 0; i < players.size(); i++)
			{
				EntityPlayerMP player = (EntityPlayerMP) players.get(i);
				if(player.getName() == name){
					player.addChatMessage(new ChatComponentTranslation(msg.law.createdFail(ld.creator)));
				}
			}
			return false;
		}
		
		BiomeGenBase bgb = mcs.worldServerForDimension(0).getBiomeGenForCoords(new BlockPos((int)ld.posX,0,(int)ld.posZ));
		String biomeName = bgb.biomeName;
		for (int i = 0; i < players.size(); i++)
		{
			EntityPlayerMP player = (EntityPlayerMP) players.get(i);
			player.addChatMessage(new ChatComponentTranslation(msg.law.createdSuccess(name,biomeName)));
		}
		return true;
	}

}
