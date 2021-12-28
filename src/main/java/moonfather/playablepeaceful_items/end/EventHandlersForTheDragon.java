package moonfather.playablepeaceful_items.end;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlersForTheDragon
{
	@SubscribeEvent
	public static void onEnteringChunk(EntityEvent.EnteringChunk event)
	{
		// here, we kill the dragon once the players enters the end
		if (event.getEntity().level.isClientSide || event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}

		if (event.getEntity().isAlive() && event.getEntity() instanceof EnderDragonEntity)
		{
			EnderDragonEntity dragon = (EnderDragonEntity) event.getEntity();
			if (dragon.isAlive())
			{
				if (dragon.getHealth() > 1)
				{
					dragon.head.hurt(new EntityDamageSource("peaceful", dragon).setThorns().setExplosion(), 1234567);
				}
			}
		}
	}



	@SubscribeEvent
	public static void onLeaveWorldEvent(EntityLeaveWorldEvent event)
	{
		// here we create 19 gateways towards outer islands
		//
		// LivingDeathEvent happens when i hit it with hardcoded damage, but then lightshow starts
		// LivingExperienceDropEvent doesn't trigger
		if (event.getEntity().level.isClientSide || event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}
		if (event.getEntity() instanceof EnderDragonEntity)
		{
			EnderDragonEntity dragon = (EnderDragonEntity) event.getEntity();
			if (dragon.getDragonFight() != null)
			{
				for (int i = 1; i <= 19; i++)
				{
					dragon.getDragonFight().spawnNewGateway();
				}
			}
		}
	}



	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		// here we kill shulkers as soon as the game spawns them
		if (event.getEntity().level.isClientSide || event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}

		if (event.getEntity().isAlive() && event.getEntity() instanceof ShulkerEntity)
		{
			event.getEntity().kill();
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event)
	{
		// here we ensure that the shulkers we killed above drop nothing
		if (event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}

		if (event.getEntity() instanceof ShulkerEntity)
		{
			event.setCanceled(true);
		}
	}
}
