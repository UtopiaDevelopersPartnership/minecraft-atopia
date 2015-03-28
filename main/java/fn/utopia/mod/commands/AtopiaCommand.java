package fn.utopia.mod.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.util.ChatComponentTranslation;

// Base class for atopia commands. Will use blockchain-based
// permissions instead of the Minecraft defaults.
public abstract class AtopiaCommand extends CommandBase {
	protected List<String> aliases = new ArrayList();
	
	// Convenience method to create a chat component.
	protected ChatComponentTranslation _cct(String str){
		return new ChatComponentTranslation(str); 
	}
}
