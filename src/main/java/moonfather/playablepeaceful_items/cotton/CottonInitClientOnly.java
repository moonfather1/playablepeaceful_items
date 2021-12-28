package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class CottonInitClientOnly
{
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(PeacefulMod.CottonBush, RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(PeacefulMod.CottonSeedling, RenderType.cutoutMipped());

		Minecraft.getInstance().getBlockColors().register(new IBlockColor()
		{
			@Override
			public int getColor(BlockState state, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tintIndex)
			{
				if (world != null && pos != null)
				{
					return BiomeColors.getAverageGrassColor(world, pos);//mozda ColorizerGrass.getGrassColor(0.5D, 1.0D)
					//return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
				}
				return -1;
			}
		}, PeacefulMod.CottonSeedling);

		Minecraft.getInstance().getBlockColors().register(new IBlockColor()
		{
			@Override
			public int getColor(BlockState state, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tintIndex)
			{
				if (world != null && pos != null)
				{
					return BiomeColors.getAverageGrassColor(world, pos);//mozda ColorizerGrass.getGrassColor(0.5D, 1.0D)
					//return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
				}
				return -1;
			}
		}, PeacefulMod.CottonBush);
	}
}
