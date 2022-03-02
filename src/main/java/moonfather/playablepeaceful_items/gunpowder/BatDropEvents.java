package moonfather.playablepeaceful_items.gunpowder;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.gunpowder.sprite.SpriteEntity;
import moonfather.playablepeaceful_items.gunpowder.items.LavaPoopItemEntity;
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

		if (event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL && OptionsHolder.COMMON.GunpowderModuleOnlyOnPeacefulDifficulty.get())
		{
			return;
		}

		if (!event.getEntity().isAlive() || !(event.getEntity() instanceof BatEntity))
		{
			return;
		}

		if (event.getEntity() instanceof SpriteEntity)
		{
			if (OptionsHolder.COMMON.GunpowderRelatedLavaSpritePoopFreqMultiplier.get() > 0)
			{
				int everyXTicks = 12000;  // 2x in 1 day (24k is one day, i think)
				everyXTicks = (int) Math.round(everyXTicks / OptionsHolder.COMMON.GunpowderRelatedLavaSpritePoopFreqMultiplier.get());
				if (random.nextInt(everyXTicks) == 1)
				{
					LivingEntity ourBat = event.getEntityLiving();
					ServerWorld ourWorld = (ServerWorld) ourBat.level;
					ItemEntity entityitem2 = new LavaPoopItemEntity(ourWorld, ourBat.getX(), ourBat.getY() - 0.25, ourBat.getZ(), new ItemStack(PeacefulMod.LavaSpritePoop));
					entityitem2.setPickUpDelay(20);
					ourWorld.addFreshEntity(entityitem2);
				}
			}
		}
		else
		{
			if (OptionsHolder.COMMON.GunpowderRelatedBatsDropPoop.get() && OptionsHolder.COMMON.GunpowderRelatedBatPoopFreqMultiplier.get() > 0)
			{
				int everyXTicks = 18000;  // 4x in 3 days (24k is one day, i think)
				everyXTicks = (int) Math.round(everyXTicks / OptionsHolder.COMMON.GunpowderRelatedBatPoopFreqMultiplier.get());
				if (random.nextInt(everyXTicks) == 1)
				{
					LivingEntity ourBat = event.getEntityLiving();
					ServerWorld ourWorld = (ServerWorld) ourBat.level;
					ItemEntity entityitem = new ItemEntity(ourWorld, ourBat.getX(), ourBat.getY() - 0.25, ourBat.getZ(), new ItemStack(PeacefulMod.BatPoop));
					entityitem.setPickUpDelay(20);
					ourWorld.addFreshEntity(entityitem);
				}
			}
		}
	}

	private static Random random = new Random();
}
