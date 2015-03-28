package fn.utopia.mod.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.blockchain.Laws;
import fn.utopia.mod.blockchain.data.BlockchainData.LawData;
import fn.utopia.mod.blockchain.messages.Messages;
import fn.utopia.mod.main.AtopiaMod;
import fn.utopia.mod.util.AMath;
import fn.utopia.mod.util.AMath.Direction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;

public class Law extends AtopiaCommand {
	
	public static final double MESSAGE_RADIUS = 40.0;

	public Law() {
		this.aliases.add("law");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "law";
	}

	@Override
	public List getAliases() {
		// TODO Auto-generated method stub
		return this.aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args)
			throws CommandException {
		if(args.length == 0){
			return;
		}
		if(!(sender instanceof EntityPlayerMP)){
			return;
		}
		EntityPlayerMP pl = (EntityPlayerMP)sender;
		
		String pArg = args[0];
		BlockchainManager bcm = AtopiaMod.instance.bcm;
		Laws laws = bcm.getLaws();
		Messages msg = bcm.getMessages();
		
		if("create".equals(pArg)){
			if(pl.dimension != 0){
				pl.addChatMessage(_cct(msg.law.notOverworld()));
				return;
			}
			// Check if we're clear. If this returns a law object then that law is in the way.
			LawData ld = laws.createLaw(pl);
			if(ld != null){
				pl.addChatMessage(_cct(msg.law.overlap(ld.creator)));
				return;
			} 
			
			LawData currentLaw = laws.getLaw(pl.getName());
			// If player already has a law in effect.
			if(currentLaw != null){
				pl.addChatMessage(_cct(msg.law.alreadyExist()));
				return;
			}
			
			pl.addChatMessage(_cct(msg.law.creating(pl.getName(),true)));
			List players = pl.worldObj.playerEntities;
			for(int i = 0; i < players.size();i++){
				EntityPlayerMP p = (EntityPlayerMP)players.get(i);
				// Broadcast to players nearby.
				if(p.getName() == pl.getName()){
					continue;
				}
				if(AMath.isInsideSphere(p.posX, p.posY, p.posZ, pl.posX, pl.posY, pl.posZ, MESSAGE_RADIUS)){
					p.addChatMessage(_cct(msg.law.creating(pl.getName(),false)));
				}
			}
			
		} else if ("abandon".equals(pArg)){
			LawData currentLaw = laws.abandonLaw(pl.getName());
			// If player has no law in effect.
			if(currentLaw == null){
				pl.addChatMessage(_cct(msg.law.abandonedFail()));
				return;
			}
			pl.addChatMessage(_cct(msg.law.abandoning()));
		} else if ("check".equals(pArg)){
			
			if(args.length >= 2){
				String lArg = args[1];
				
				if("all".equals(lArg)){
					
					if(args.length == 3){
						String lArg3 = args[2];
						double radius;
						try{
							radius = this.parseDouble(lArg3);
							List<LawData> lds = laws.findLaws(pl,radius);
							for(int i = 0; i < lds.size(); i++){
								LawData ld = lds.get(i);
								_printDir(pl,ld,msg);
							}
						} catch(NumberInvalidException e){
							pl.addChatMessage(_cct(msg.common.noDouble(lArg3)));
						}
					} else {
						List<LawData> lds =laws.findLaws(pl);
						for(int i = 0; i < lds.size(); i++){
							LawData ld = lds.get(i);
							_printDir(pl,ld,msg);
						}
					}
				} else if ("here".equals(lArg)){
					LawData ld = laws.getLawAt(pl.posX,pl.posZ);
					if(ld == null){
						pl.addChatMessage(_cct(msg.law.lawless()));
						return;
					}
					boolean them = pl.getName() == ld.creator;
					pl.addChatMessage(_cct(msg.law.rightHere(ld.creator, them)));
					return;
				} else if ("player".equals(lArg)){
					if(args.length == 2){
						String lArg3 = args[2];
						LawData ld = laws.getLaw(lArg3);
						if(ld == null){
							pl.addChatMessage(_cct(msg.law.haveNoLaw(lArg3, true)));
						} else {
							_printDir(pl,ld,msg);
						}
					}
				}
			} else {
				// Check for your own law if no argument.
				LawData ld = laws.getLaw(pl.getName());
				if(ld == null){
					pl.addChatMessage(_cct(msg.law.haveNoLaw(pl.getName(), true)));
				} else {
					_printDir(pl,ld,msg);
				}
			}
		}
	}
	
	protected void _printDir(EntityPlayerMP pl, LawData ld, Messages msg){
		Direction dir = AMath.getDirection(pl.posX, pl.posZ, ld.posX, ld.posZ);
		double x = (ld.posX - pl.posX);
		double z = (ld.posZ - pl.posZ);
		double dist = MathHelper.sqrt_double(x*x + z*z);
		boolean them = ld.creator == pl.getName();
		
		if(dist < ld.radius){
			pl.addChatMessage(_cct(msg.law.rightHere(pl.getName(), them)));
		} else {
			pl.addChatMessage(_cct(msg.law.direction(ld.creator, dir, dist, them)));
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,
			BlockPos pos) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "";
	}
}
