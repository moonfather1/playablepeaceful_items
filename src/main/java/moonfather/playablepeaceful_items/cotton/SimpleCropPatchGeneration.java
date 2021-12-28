package moonfather.playablepeaceful_items.cotton;

import com.google.common.collect.ImmutableSet;
import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;

public class SimpleCropPatchGeneration
{
	public static final BlockClusterFeatureConfig COTTON_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(PeacefulMod.CottonBush.getStateForAge(1)), new SimpleBlockPlacer())).tries(6).xspread(1).zspread(1).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).noProjection().build();

	public static final ConfiguredFeature<?, ?> COTTON_PATCH_LOWER_CHANCE = Feature.RANDOM_PATCH.configured(COTTON_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(OptionsHolder.COMMON.CottonWorldGenOdds.get() * 3);  // 8*3 (default) is 4%

	public static final ConfiguredFeature<?, ?> COTTON_PATCH_HIGHER_CHANCE = Feature.RANDOM_PATCH.configured(COTTON_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(OptionsHolder.COMMON.CottonWorldGenOdds.get()); // default 8 is 12.5%

	public static void registerConfiguredFeatures()
	{
		if (OptionsHolder.COMMON.CottonWorldGenOdds.get() > 0)
		{
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "patch_wild_cotton_pp1", COTTON_PATCH_LOWER_CHANCE);
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "patch_wild_cotton_pp2", COTTON_PATCH_HIGHER_CHANCE);
		}
	}
}
