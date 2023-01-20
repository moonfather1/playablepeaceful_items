package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SlimeInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		EntityRenderers.register(RegistrationManager.SLIME.get(), CuteSlimeRenderer::new);
	}
}
