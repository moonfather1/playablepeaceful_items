package moonfather.playablepeaceful_items.end;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlersForTheDragon
{
	//@SubscribeEvent
	public static void onEnteringChunk(EntityEvent.EnteringChunk event)
	{
		if (event.getEntity().level.isClientSide || event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}

		if (event.getEntity().isAlive() && event.getEntity() instanceof EnderDragonEntity)
		{
			EnderDragonEntity dragon = (EnderDragonEntity) event.getEntity();
			System.out.println("~~~~~~~~~~~~ dragon found: " + dragon.getPhaseManager().getCurrentPhase().getPhase() + " ~~~~~~~~~~```");
			if (dragon.isAlive())
			{
				System.out.println("~~~~~~~~~~~~ dragon hp==" + dragon.getHealth() + "/" +  dragon.getMaxHealth() + " ~~~~~~~~~~```");
				dragon.head.hurt(new EntityDamageSource("peaceful", dragon).setThorns().setExplosion(), 1234567);
				System.out.println("~~~~~~~~~~~~ dragon hp==" + dragon.getHealth() + "/" +  dragon.getMaxHealth() + " ~~~~~~~~~~```");
				if (dragon.getHealth() == 0 && dragon.getDragonFight() != null)
				{
					for (int i = 1; i <= 19; i++)
					{
						//dragon.getDragonFight().setDragonKilled(dragon);
						//dragon.getDragonFight().spawnNewGateway();
					}
				}
			}
		} // sjebah neshto chunkove
	}

	//@SubscribeEvent
	public static void oEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.getEntity().level.isClientSide || event.getEntity().level.getDifficulty() != Difficulty.PEACEFUL)
		{
			return;
		}

		if (event.getEntity().isAlive() && event.getEntity() instanceof ShulkerEntity)
		{
			System.out.println("~~~~~~~~~~~~ shulker at " + event.getEntity().blockPosition() + " ~~~~~~~~~~```");
			event.getEntity().absMoveTo(event.getEntity().position().x, -5, event.getEntity().position().z);
			event.getEntity().setDeltaMovement(0, -1, 0);
			event.getEntity().kill();
		}
	}

}
