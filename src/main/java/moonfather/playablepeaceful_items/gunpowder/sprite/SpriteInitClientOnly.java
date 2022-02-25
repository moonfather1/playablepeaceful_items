package moonfather.playablepeaceful_items.gunpowder.sprite;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SpriteInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(RegistrationManager.SPRITE.get(), SpriteRenderer::new);
	}

	@SubscribeEvent
	public static void onItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((s, x) -> x == 1 ? 0xffcc22 : 0xcc5511, RegistrationManager.SPRITE_EGG_HOLDER);
	}
}
