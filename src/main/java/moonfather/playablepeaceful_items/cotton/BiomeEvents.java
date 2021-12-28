package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.membrane.SimpleCropPatchGenerationForPhantomBush;
import moonfather.playablepeaceful_items.slimeball.RegistrationManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
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
		else if (event.getName().getPath().equals("end_highlands"))
		{
			event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SimpleCropPatchGenerationForPhantomBush.PHANTOM_BUSH_PATCH_LOWER_CHANCE);
		}

	}
}
