package moonfather.playablepeaceful_items.membrane.worldgen;

import moonfather.playablepeaceful_items.Constants;
import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.RegistrationManager;
import moonfather.playablepeaceful_items.cotton.worldgen.SimpleFeatureConfiguration;
import moonfather.playablepeaceful_items.cotton.worldgen.SimpleWildCropGeneration;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

public class SimpleCropPatchGenerationForPhantomBush extends SimpleWildCropGeneration
{
	public static void registerConfiguredFeatures()
	{
		int chance = OptionsHolder.COMMON.HedgeWorldGenOdds.get(); //40 by def
		if (chance > 0)
		{
			ResourceLocation id1 = new ResourceLocation(Constants.MODID, "patch_hedge1");
			ResourceLocation id2 = new ResourceLocation(Constants.MODID, "patch_hedge2");
			FEATURE_HEDGE = register(id1, RegistrationManager.PHANTOM_BUSH.get(), new SimpleFeatureConfiguration());
			PLACEMENT_HEDGE_HIGHER_CHANCE = registerPlacement(id1, FEATURE_HEDGE, RarityFilter.onAverageOnceEvery(chance / 2), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), HedgePlacementFilter.create(false));
			PLACEMENT_HEDGE_LOWER_CHANCE = registerPlacement(id2, FEATURE_HEDGE, RarityFilter.onAverageOnceEvery(chance), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), HedgePlacementFilter.create(true));
		}
	}

	public static Holder<ConfiguredFeature<SimpleFeatureConfiguration, Feature<SimpleFeatureConfiguration>>> FEATURE_HEDGE;
	public static Holder<PlacedFeature> PLACEMENT_HEDGE_HIGHER_CHANCE;
	public static Holder<PlacedFeature> PLACEMENT_HEDGE_LOWER_CHANCE;
}
