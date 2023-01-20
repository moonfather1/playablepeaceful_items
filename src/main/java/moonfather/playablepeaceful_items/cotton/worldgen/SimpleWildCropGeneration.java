package moonfather.playablepeaceful_items.cotton.worldgen;

import moonfather.playablepeaceful_items.Constants;
import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class SimpleWildCropGeneration
{
	/*public static final BlockClusterFeatureConfig COTTON_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(PeacefulMod.CottonBush.getStateForAge(1)), new SimpleBlockPlacer())).tries(6).xspread(1).zspread(1).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).noProjection().build();

	public static final ConfiguredFeature<?, ?> COTTON_PATCH_LOWER_CHANCE = Feature.RANDOM_PATCH.configured(COTTON_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(OptionsHolder.COMMON.CottonWorldGenOdds.get() * 3);  // 8*3 (default) is 4%

	public static final ConfiguredFeature<?, ?> COTTON_PATCH_HIGHER_CHANCE = Feature.RANDOM_PATCH.configured(COTTON_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(OptionsHolder.COMMON.CottonWorldGenOdds.get()); // default 8 is 12.5%
*/
	public static void registerConfiguredFeatures()
	{
		if (OptionsHolder.COMMON.CottonWorldGenOdds.get() > 0)
		{
			ResourceLocation id1 = new ResourceLocation(Constants.MODID, "patch_wild_cotton1");
			ResourceLocation id2 = new ResourceLocation(Constants.MODID, "patch_wild_cotton2");
			FEATURE_WILD_COTTON = register(id1, RegistrationManager.WILD_COTTON.get(), new SimpleFeatureConfiguration(/*BlockPredicate.matchesBlock(Blocks.SAND, BLOCK_BELOW)*/));
			int chance = 15; // make 30
			PLACEMENT_WILD_COTTON_HIGHER_CHANCE = registerPlacement(id1, FEATURE_WILD_COTTON, RarityFilter.onAverageOnceEvery(chance), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), CategoryBiomeFilter.create().assign(Biome.BiomeCategory.SAVANNA));
			PLACEMENT_WILD_COTTON_LOWER_CHANCE = registerPlacement(id2, FEATURE_WILD_COTTON, RarityFilter.onAverageOnceEvery(chance*3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), CategoryBiomeFilter.create().assign(Biome.BiomeCategory.PLAINS).assign(Biome.BiomeCategory.FOREST));
		}
	}

	public static Holder<ConfiguredFeature<SimpleFeatureConfiguration, Feature<SimpleFeatureConfiguration>>> FEATURE_WILD_COTTON;
	public static Holder<PlacedFeature> PLACEMENT_WILD_COTTON_HIGHER_CHANCE;
	public static Holder<PlacedFeature> PLACEMENT_WILD_COTTON_LOWER_CHANCE;


	protected static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, F>> register(ResourceLocation id, F feature, FC featureConfig) {
		return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	protected static <V extends T, T> Holder<V> register(Registry<T> registry, ResourceLocation id, V value) {
		return (Holder<V>) BuiltinRegistries.<T>register(registry, id, value);
	}

	protected static Holder<PlacedFeature> registerPlacement(ResourceLocation id, Holder<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
	}
}
