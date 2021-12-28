package moonfather.playablepeaceful_items.gunpowder;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
public class BatDropEvents
{
	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntity().level.isClientSide || OptionsHolder.COMMON.GunpowderModuleEnabled.get() == false)
		{
			return;
		}

		if (!event.getEntity().isAlive() || !(event.getEntity() instanceof BatEntity))
		{
			return;
		}

		if (event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL && OptionsHolder.COMMON.GunpowderModuleOnlyOnPeacefulDifficulty.get())
		{
			return;
		}

		if (OptionsHolder.COMMON.GunpowderRelatedBatsDropPoop.get() && OptionsHolder.COMMON.GunpowderRelatedBatPoopFreqMultiplier.get() > 0)
		{
			if (random.nextInt((int) Math.round(18000 / OptionsHolder.COMMON.GunpowderRelatedBatPoopFreqMultiplier.get())) == 15) // 4x in 3 days (24k is one day, i think)
			{
				LivingEntity ourBat = event.getEntityLiving();
				ServerWorld ourWorld = (ServerWorld) ourBat.level;
				ItemEntity entityitem = new ItemEntity(ourWorld, ourBat.getX(), ourBat.getY() - 0.25, ourBat.getZ(), new ItemStack(PeacefulMod.BatPoop));
				entityitem.setPickUpDelay(20);
				ourWorld.addFreshEntity(entityitem);
				ItemEntity entityitem2 = new ItemEntity(ourWorld, ourBat.getX(), ourBat.getY() - 0.25, ourBat.getZ(), new ItemStack(PeacefulMod.LavaSpritePoop));
				entityitem2.setPickUpDelay(20);
				ourWorld.addFreshEntity(entityitem2);
			}
		}
	}

	private static Random random = new Random();
}
