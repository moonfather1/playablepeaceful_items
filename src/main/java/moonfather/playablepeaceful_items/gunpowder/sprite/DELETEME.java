package moonfather.playablepeaceful_items.gunpowder.sprite;

import moonfather.playablepeaceful_items.OptionsHolder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DELETEME
{
    @SubscribeEvent
    public static void onEntityEnteringChunk(EntityEvent.EnteringSection event)
    {
        if (! event.didChunkChange())
        {
            return;
        }
        if (! OptionsHolder.COMMON.GunpowderRelatedLavaSpritesEnabled.get() || ! OptionsHolder.COMMON.GunpowderModuleEnabled.get() || OptionsHolder.COMMON.GunpowderRelatedLavaSpriteSpawnOddsMultiplier.get() == 0f)
        {
            return;
        }
        if (! event.getEntity().level.getDifficulty().equals(Difficulty.PEACEFUL) && OptionsHolder.COMMON.GunpowderModuleOnlyOnPeacefulDifficulty.get())
        {
            return;
        }
        if (event.getEntity().level.isClientSide() || ! (event.getEntity() instanceof SpriteEntity))
        {
            return;
        }
        SpriteEntity e = (SpriteEntity) event.getEntity();
        if (e.homePosition != null) /////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        {
            System.out.println("~~ sprite with home changing chunk");
        }
        else
        {
            System.out.println("~~ sprite with NO HOME changing chunk");
        }
    }
}
