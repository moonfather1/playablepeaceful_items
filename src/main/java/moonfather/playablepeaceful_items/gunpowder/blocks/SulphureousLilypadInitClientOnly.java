package moonfather.playablepeaceful_items.gunpowder.blocks;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SulphureousLilypadInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(PeacefulMod.LilypadBlock, RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(PeacefulMod.BatPoopBlock, RenderType.cutoutMipped());
	}
}
