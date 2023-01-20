package moonfather.playablepeaceful_items.cotton.worldgen;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CottonPlacementEvent
{
	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		if (event.getCategory() == Biome.BiomeCategory.SAVANNA)
		{
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SimpleWildCropGeneration.PLACEMENT_WILD_COTTON_HIGHER_CHANCE);
		}
		else if (event.getCategory() == Biome.BiomeCategory.FOREST && event.getClimate().temperature > 0.3F && event.getClimate().temperature < 1.0F)
		{
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SimpleWildCropGeneration.PLACEMENT_WILD_COTTON_LOWER_CHANCE);
		}
		else if (event.getCategory() == Biome.BiomeCategory.PLAINS && event.getClimate().temperature > 0.3F && event.getClimate().temperature < 1.0F)
		{
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SimpleWildCropGeneration.PLACEMENT_WILD_COTTON_LOWER_CHANCE);
		}
	}
}
