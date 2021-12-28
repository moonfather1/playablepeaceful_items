package moonfather.playablepeaceful_items.slimeball;

import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SlimeInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(RegistrationManager.SLIME.get(), CuteSlimeRenderer::new);
	}

	@SubscribeEvent
	public static void onItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((s, x) -> x == 1 ? 0x229900 : 0x44cc22, RegistrationManager.EGG_SLIME);
	}
}
