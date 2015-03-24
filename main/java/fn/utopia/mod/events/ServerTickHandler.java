package fn.utopia.mod.events;

import java.util.Queue;

import fn.utopia.mod.blockchain.actions.Action;
import fn.utopia.mod.main.AtopiaMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerTickHandler {
	
	// TODO see what value to use.
	static final int UPDATE_TICKS = 10;
	
	protected int ticks = 0;
	
	@SubscribeEvent
    public void foo(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START){
        	if(ticks++ == UPDATE_TICKS){
        		ticks = 0;
        		Queue<Action> aq = AtopiaMod.instance.bm.actionQueue;
        		while(!aq.isEmpty()){
        			aq.poll().execute();
        		}
        	}
        		
        }
    }
}
