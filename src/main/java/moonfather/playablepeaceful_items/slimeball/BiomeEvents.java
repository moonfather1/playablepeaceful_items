package moonfather.playablepeaceful_items.slimeball;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BiomeEvents
{
	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		if (event.getCategory() == Biome.Category.SWAMP)
		{
			event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(RegistrationManager.SLIME.get(), 32, 1, 4));
		}
	}
}
