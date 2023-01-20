package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class CottonInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event)
	{
		ItemBlockRenderTypes.setRenderLayer(PeacefulMod.Blocks.CottonBush.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(PeacefulMod.Blocks.CottonSeedling.get(), RenderType.cutoutMipped());

		Minecraft.getInstance().getBlockColors().register(new BlockColor()
		{
			@Override
			public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex)
			{
				if (world != null && pos != null)
				{
					return BiomeColors.getAverageGrassColor(world, pos);//mozda ColorizerGrass.getGrassColor(0.5D, 1.0D)
					//return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
				}
				return -1;
			}
		}, PeacefulMod.Blocks.CottonSeedling.get());

		Minecraft.getInstance().getBlockColors().register(new BlockColor()
		{
			@Override
			public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex)
			{
				if (world != null && pos != null)
				{
					return BiomeColors.getAverageGrassColor(world, pos);//mozda ColorizerGrass.getGrassColor(0.5D, 1.0D)
					//return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
				}
				return -1;
			}
		}, PeacefulMod.Blocks.CottonBush.get());
	}
}
