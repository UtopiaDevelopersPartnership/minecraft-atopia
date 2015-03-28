package fn.utopia.mod.events;

import java.util.Queue;

import fn.utopia.mod.blockchain.actions.Action;
import fn.utopia.mod.main.AtopiaMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerTickHandler {
	
	@SubscribeEvent
    public void foo(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START){
        	AtopiaMod.instance.bcm.tick();	
        }
    }
}
