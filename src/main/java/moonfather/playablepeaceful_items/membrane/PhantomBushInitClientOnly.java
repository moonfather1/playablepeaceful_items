package moonfather.playablepeaceful_items.membrane;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class PhantomBushInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event)
	{
		ItemBlockRenderTypes.setRenderLayer(PeacefulMod.Blocks.PhantomBush.get(), RenderType.cutoutMipped());
	}
}
