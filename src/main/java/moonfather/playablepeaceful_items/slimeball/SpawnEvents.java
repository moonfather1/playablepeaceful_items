package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpawnEvents
{
    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (Biome.getBiomeCategory(event.getWorld().getBiome(event.getEntity().blockPosition())).equals(Biome.BiomeCategory.SWAMP))
        {
            //System.out.println("~~~swamp spawn check: " + event.getEntity());
            if (event.getEntity() instanceof Cow)
            {
                if (event.getWorld().getRandom().nextInt(10) != 6)
                {
                    event.setResult(Event.Result.DENY);
                    if (event.getWorld() instanceof ServerLevel)
                    {
                        RegistrationManager.SLIME.get().spawn((ServerLevel)event.getWorld(), ItemStack.EMPTY, null, event.getEntity().blockPosition(), MobSpawnType.NATURAL, false, false);
                        System.out.println("~~~swamp spawn check: added slime at " + event.getEntity().blockPosition());
                    }
                }
            }
            if (event.getEntity() instanceof Sheep)
            {
                if (event.getWorld().getRandom().nextInt(10) != 7)
                {
                    event.setResult(Event.Result.DENY);
                    if (event.getWorld() instanceof ServerLevel)
                    {
                        RegistrationManager.SLIME.get().spawn((ServerLevel)event.getWorld(), ItemStack.EMPTY, null, event.getEntity().blockPosition(), MobSpawnType.NATURAL, false, false);
                        System.out.println("~~~swamp spawn check2: added slime at " + event.getEntity().blockPosition());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        if (Biome.getBiomeCategory(event.getWorld().getBiome(event.getEntity().blockPosition())).equals(Biome.BiomeCategory.SWAMP))
        {
            //System.out.println("~~~swamp spawn: " + event.getEntity());
        }
    }
}
