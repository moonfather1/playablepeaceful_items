package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class PerpetualSlimeSpawning
{
	@SubscribeEvent
	public static void onEnteringChunk(EntityEvent.EnteringSection event)
	{
		if (event.getEntity().level.getDifficulty() == Difficulty.PEACEFUL)
		{
			if (actuallyChangedChunks(event))
			{
				if (!event.getEntity().level.isClientSide && event.getEntity() instanceof Player)
				{
					Player player = (Player) event.getEntity();
					if (isTimeForAnotherSpawn(player))
					{
						if (Biome.getBiomeCategory(event.getEntity().level.getBiome(event.getEntity().blockPosition())) == Biome.BiomeCategory.SWAMP)
						{
							if (onSurface(player))
							{
								BlockPos position = getSpawnPosition(player, event);
								if (position == null)
								{
									return;
								}
								if (enoughSlimes(position, player.level))
								{
									if (player.level.random.nextInt(100) < 85)  // 15% chance NOT to activate 8min cooldown
									{
										resetTimeForAnotherSpawn(player);
									}
									return;
								}
								System.out.println("~~~perpet spawn at: " + position);
								CuteSlimeEntity slime = (CuteSlimeEntity) RegistrationManager.SLIME.get().spawn((ServerLevel) event.getEntity().level, ItemStack.EMPTY, player, position, MobSpawnType.SPAWN_EGG, true, false);
								if (player.level.random.nextInt(100) < 30)
								{
									RegistrationManager.SLIME.get().spawn((ServerLevel) event.getEntity().level, ItemStack.EMPTY, player, position, MobSpawnType.SPAWN_EGG, true, false); // one more
								}
								if (player.level.random.nextInt(100) < 85)  // 15% chance NOT to activate 8min cooldown
								{
									resetTimeForAnotherSpawn(player);
								}
							}
						}
					}
				}
			}
		}
	}

	private static boolean enoughSlimes(BlockPos position, Level level)
	{
		List<CuteSlimeEntity> list = level.getEntitiesOfClass(CuteSlimeEntity.class, new AABB(position).inflate(20.0D, 5.0D, 20.0D));
		return list.size() > 0;
	}

	private static boolean actuallyChangedChunks(EntityEvent.EnteringSection event)
	{
		// because stupid forge fires EnteringChunk event when i jump in place
		//return event.getNewChunkX() != event.getOldChunkX() || event.getNewChunkZ() != event.getOldChunkZ();
		return event.didChunkChange();
	}

	private static BlockPos getSpawnPosition(Player player, EntityEvent.EnteringSection event)
	{
		int spawnChunkX = event.getNewPos().x() - 2 * (event.getNewPos().x() - event.getOldPos().x());
		int spawnChunkZ = event.getNewPos().z() - 2 * (event.getNewPos().z() - event.getOldPos().z());
		int midX = spawnChunkX * 16 + 8;
		int midZ = spawnChunkZ * 16 + 8;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for(int dx = -4; dx <= 4; dx+=2)
		{
			for(int dz = -4; dz <= 4; dz+=2)
			{
				int y = player.level.getHeight(Heightmap.Types.WORLD_SURFACE, midX + dx, midZ + dz);
				pos.set(midX + dx, y, midZ + dz);
				if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, player.level, pos, EntityType.SLIME))
				{
					return pos;
				}
			}
		}
		return null;
	}


	private static boolean onSurface(Player player)
	{
		return player.blockPosition().getY() >= player.level.getLevelData().getYSpawn() - 1;
	}



	private static boolean isTimeForAnotherSpawn(Player player)
	{
		if (!times.containsKey(player))
		{
			return true;
		}
		return player.tickCount - times.get(player) > 8 * 60 * 20; // 8 min
	}



	private static void resetTimeForAnotherSpawn(Player player)
	{
		times.put(player, player.tickCount);
	}

	private static Map<Player, Integer> times = new HashMap<Player, Integer>();
}
