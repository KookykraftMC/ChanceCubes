package chanceCubes.listeners;

import chanceCubes.util.Scheduler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class TickListener
{
	//TODO: Not sure how much I like this...
	
	@SubscribeEvent
	public void onTick(ServerTickEvent event)
	{
		Scheduler.tickTasks();
	}
}