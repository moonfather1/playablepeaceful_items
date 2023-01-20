package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class WanderingClericInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		EntityRenderers.register(RegistrationManager.CLERIC.get(), WanderingClericRenderer::new);
	}
}
