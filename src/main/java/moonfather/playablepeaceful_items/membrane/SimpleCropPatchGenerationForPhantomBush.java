package moonfather.playablepeaceful_items.membrane;

import com.google.common.collect.ImmutableSet;
import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;

import java.util.Random;

public class SimpleCropPatchGenerationForPhantomBush
{
	public static final BlockClusterFeatureConfig PHANTOM_BUSH_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(PeacefulMod.PhantomBush.getStateForAge(1)), new PhantomHedgePlacer())).tries(6).xspread(4).zspread(4).whitelist(ImmutableSet.of(Blocks.END_STONE.getBlock())).noProjection().build();

	public static final ConfiguredFeature<?, ?> PHANTOM_BUSH_PATCH_LOWER_CHANCE = Feature.RANDOM_PATCH.configured(PHANTOM_BUSH_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(OptionsHolder.COMMON.HedgeWorldGenOdds.get() * 4);

	public static final ConfiguredFeature<?, ?> PHANTOM_BUSH_PATCH_HIGHER_CHANCE = Feature.RANDOM_PATCH.configured(PHANTOM_BUSH_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(OptionsHolder.COMMON.HedgeWorldGenOdds.get());

	public static void registerConfiguredFeatures()
	{
		if (OptionsHolder.COMMON.HedgeWorldGenOdds.get() > 0)
		{
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "patch_phantom_bush_pp2", PHANTOM_BUSH_PATCH_HIGHER_CHANCE);
		}
	}

	////////////////////////////////////////

	private static  class PhantomHedgePlacer extends SimpleBlockPlacer
	{
		private static int[] ageDistribution = {0, 1, 1, 1, 2, 2, 2, 3};

		public void place(IWorld world, BlockPos pos, BlockState state, Random p_225567_4_)
		{
			int age = ageDistribution[world.getRandom().nextInt(8)];
			world.setBlock(pos.above(2), state.setValue(PhantomBushBlock.LEVEL, 1).setValue(PeacefulMod.PhantomBush.getAgeProperty(), age), 2);
			world.setBlock(pos, state.setValue(PhantomBushBlock.LEVEL, 0).setValue(PeacefulMod.PhantomBush.getAgeProperty(), age), 2);
		}
	}
}
