package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BiomeEvents
{
	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		if (event.getCategory() == Biome.BiomeCategory.SWAMP)
		{
			event.getSpawns().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(RegistrationManager.SLIME.get(), 32, 1, 4));
		}
	}
}
