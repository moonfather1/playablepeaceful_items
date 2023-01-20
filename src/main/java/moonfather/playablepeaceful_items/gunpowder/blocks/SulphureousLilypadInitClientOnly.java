package moonfather.playablepeaceful_items.gunpowder.blocks;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SulphureousLilypadInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event)
	{
		ItemBlockRenderTypes.setRenderLayer(PeacefulMod.Blocks.LilypadBlock.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(PeacefulMod.Blocks.BatPoopBlock.get(), RenderType.cutoutMipped());
	}
}
