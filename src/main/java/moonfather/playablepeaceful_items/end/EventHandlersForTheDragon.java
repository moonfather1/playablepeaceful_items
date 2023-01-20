package moonfather.playablepeaceful_items.end;


import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.block.Blocks;
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
	public static void onEnteringChunk(EntityEvent.EnteringSection event)
	{
		// here, we kill the dragon once the players enters the end
		if (event.getEntity().level.isClientSide || event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}

		if (event.getEntity().isAlive() && event.getEntity() instanceof EnderDragon)
		{
			EnderDragon dragon = (EnderDragon) event.getEntity();
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
		if (event.getEntity() instanceof EnderDragon)
		{
			EnderDragon dragon = (EnderDragon) event.getEntity();
			if (dragon.getDragonFight() != null)
			{
				for (int i = 1; i <= 19; i++)
				{
					dragon.getDragonFight().spawnNewGateway();
				}
				// one other thing: lose the egg
				event.getWorld().setBlockAndUpdate(new BlockPos(0, 64+2, 0), Blocks.VOID_AIR.defaultBlockState());
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

		if (event.getEntity().isAlive() && event.getEntity() instanceof Shulker)
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

		if (event.getEntity() instanceof Shulker)
		{
			event.setCanceled(true);
		}
	}
}
