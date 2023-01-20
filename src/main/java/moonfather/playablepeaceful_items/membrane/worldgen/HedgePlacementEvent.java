package moonfather.playablepeaceful_items.membrane.worldgen;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HedgePlacementEvent
{
	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		//System.out.println("~~onBiomeLoading cat==" + event.getCategory().toString() + "  biome==" + event.getName().toString());
		if (event.getCategory() == Biome.BiomeCategory.THEEND)
		{
			String biome = event.getName().toString();
			if (biome.equals("minecraft:end_highlands"))
			{
				event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SimpleCropPatchGenerationForPhantomBush.PLACEMENT_HEDGE_LOWER_CHANCE);
				//System.out.println("~~onBiomeLoading lower chance");
			}
			else if (biome.equals("byg:bulbis_gardens_edge") || biome.equals("byg:ivis_fields")
					|| biome.equals("byg:shattered_desert_isles") || biome.equals("byg:shattered_desert")
					|| biome.equals("byg:purpur_peaks") || biome.equals("byg:nightshade_forest"))
			{
				event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SimpleCropPatchGenerationForPhantomBush.PLACEMENT_HEDGE_HIGHER_CHANCE);
				//System.out.println("~~onBiomeLoading higher chance");
			}
		}
	}
}
