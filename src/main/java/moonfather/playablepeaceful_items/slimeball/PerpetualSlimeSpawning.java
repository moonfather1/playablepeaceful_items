package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
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
	public static void onEnteringChunk(EntityEvent.EnteringChunk event)
	{
		if (event.getEntity().level.getDifficulty() == Difficulty.PEACEFUL)
		{
			if (actuallyChangedChunks(event))
			{
				if (!event.getEntity().level.isClientSide && event.getEntity() instanceof PlayerEntity)
				{
					PlayerEntity player = (PlayerEntity) event.getEntity();
					if (isTimeForAnotherSpawn(player))
					{
						if (event.getEntity().level.getBiome(event.getEntity().blockPosition()).getBiomeCategory() == Biome.Category.SWAMP)
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
									resetTimeForAnotherSpawn(player);
									return;
								}
								CuteSlimeEntity slime = (CuteSlimeEntity) RegistrationManager.SLIME_HOLDER.spawn((ServerWorld) event.getEntity().level, ItemStack.EMPTY, player, position, SpawnReason.SPAWN_EGG, true, false);
								resetTimeForAnotherSpawn(player);
							}
						}
					}
				}
			}
		}
	}

	private static boolean enoughSlimes(BlockPos position, World level)
	{
		List<CuteSlimeEntity> list = level.getEntitiesOfClass(CuteSlimeEntity.class, new AxisAlignedBB(position).inflate(20.0D, 5.0D, 20.0D));
		return list.size() > 0;
	}

	private static boolean actuallyChangedChunks(EntityEvent.EnteringChunk event)
	{
		// because stupid forge fires EnteringChunk event when i jump in place
		return event.getNewChunkX() != event.getOldChunkX() || event.getNewChunkZ() != event.getOldChunkZ();
	}

	private static BlockPos getSpawnPosition(PlayerEntity player, EntityEvent.EnteringChunk event)
	{
		int spawnChunkX = event.getNewChunkX() - 2 * (event.getNewChunkX() - event.getOldChunkX());
		int spawnChunkZ = event.getNewChunkZ() - 2 * (event.getNewChunkZ() - event.getOldChunkZ());
		int midX = spawnChunkX * 16 + 8;
		int midZ = spawnChunkZ * 16 + 8;
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for(int dx = -4; dx <= 4; dx+=2)
		{
			for(int dz = -4; dz <= 4; dz+=2)
			{
				int y = player.level.getHeight(Heightmap.Type.WORLD_SURFACE, midX + dx, midZ + dz);
				pos.set(midX + dx, y, midZ + dz);
				if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, player.level, pos, EntityType.SLIME))
				{
					return pos;
				}
			}
		}
		return null;
	}


	private static boolean onSurface(PlayerEntity player)
	{
		return player.blockPosition().getY() >= player.level.getLevelData().getYSpawn() - 1;
	}



	private static boolean isTimeForAnotherSpawn(PlayerEntity player)
	{
		if (!times.containsKey(player))
		{
			return true;
		}
		return player.tickCount - times.get(player) > 8 * 60 * 20; // 8 min
	}



	private static void resetTimeForAnotherSpawn(PlayerEntity player)
	{
		times.put(player, player.tickCount);
	}

	private static Map<PlayerEntity, Integer> times = new HashMap<PlayerEntity, Integer>();
}
