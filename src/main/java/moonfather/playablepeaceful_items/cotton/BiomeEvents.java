package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.membrane.SimpleCropPatchGenerationForPhantomBush;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BiomeEvents
{
	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		if (event.getCategory() == Biome.Category.SAVANNA)
		{
			event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SimpleCropPatchGeneration.COTTON_PATCH_HIGHER_CHANCE);
		}
		else if (event.getCategory() == Biome.Category.FOREST && event.getClimate().temperature > 0.3F && event.getClimate().temperature < 1.0F)
		{
			event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SimpleCropPatchGeneration.COTTON_PATCH_LOWER_CHANCE);
		}
		else if (event.getCategory() == Biome.Category.PLAINS && event.getClimate().temperature > 0.3F && event.getClimate().temperature < 1.0F)
		{
			event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SimpleCropPatchGeneration.COTTON_PATCH_LOWER_CHANCE);
		}
		else if (event.getCategory() == Biome.Category.THEEND)
		{
			String biome = event.getName().toString();
			if (biome.equals("minecraft:end_highlands"))
			{
				event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SimpleCropPatchGenerationForPhantomBush.PHANTOM_BUSH_PATCH_LOWER_CHANCE);
			}
			else if (biome.equals("byg:bulbis_gardens_edge") || biome.equals("byg:ivis_fields")
					|| biome.equals("byg:shattered_desert_isles") || biome.equals("byg:shattered_desert")
					|| biome.equals("byg:purpur_peaks") || biome.equals("byg:nightshade_forest"))
			{
				event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SimpleCropPatchGenerationForPhantomBush.PHANTOM_BUSH_PATCH_HIGHER_CHANCE);
			}
		}
	}
}
