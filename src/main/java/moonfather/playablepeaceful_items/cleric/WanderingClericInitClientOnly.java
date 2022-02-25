package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class WanderingClericInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(RegistrationManager.CLERIC_HOLDER, WanderingClericRenderer::new);
	}

	@SubscribeEvent
	public static void onItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((s, x) -> x == 1 ? 0xeecc33 : 0x1188ee, RegistrationManager.CLERIC_EGG_HOLDER);
	}

}
