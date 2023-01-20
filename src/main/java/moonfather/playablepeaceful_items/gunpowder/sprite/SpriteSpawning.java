package moonfather.playablepeaceful_items.gunpowder.sprite;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpriteSpawning
{
	@SubscribeEvent
	public static void onChunkDataLoad(ChunkDataEvent.Load event)
	{
		if (PresentInMap(event.getChunk().getPos().x, event.getChunk().getPos().z))
		{
			return;
		}
		if (event.getData().contains("PPI_sprites"))
		{
			CompoundTag nbt = event.getData().getCompound("PPI_sprites");
			SpriteChunkInfo info = new SpriteChunkInfo();
			info.chunkX = event.getChunk().getPos().x;
			info.chunkZ = event.getChunk().getPos().z;
			info.spritesDied = nbt.getInt("sprites_died");
			PutToMap(info, event.getChunk().getPos().x, event.getChunk().getPos().z);
		}
	}



	@SubscribeEvent
	public static void onChunkDataSave(ChunkDataEvent.Save event)
	{
		SpriteChunkInfo info = GetFromMap(event.getChunk().getPos().x, event.getChunk().getPos().z);
		if (info != null)
		{
			CompoundTag nbt = new CompoundTag();
			nbt.putInt("sprites_died", info.spritesDied);
			nbt.putInt("x", info.chunkX);
			nbt.putInt("z", info.chunkZ);
			event.getData().put("PPI_sprites", nbt);
		}
	}



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
		if (event.getEntity().level.isClientSide() || ! (event.getEntity() instanceof Player))
		{
			return;
		}
		SpriteChunkInfo info = GetFromMap(event.getNewPos().getX(), event.getNewPos().getZ());
		if (info == null)
		{
			System.out.println("~~SS enter new chunk");
			TrySpawn(event.getEntity().level, event.getNewPos().getX(), event.getNewPos().getZ(), 4);
			info = new SpriteChunkInfo();
			info.chunkX = event.getNewPos().getX();
			info.chunkZ = event.getNewPos().getZ();
			info.visited = true;
			PutToMap(info, event.getNewPos().getX(), event.getNewPos().getZ());
		}
		else
		{
			System.out.println("~~SS enter old chunk (" + info.spritesDied + ")");
			if (info.spritesDied >= 2)
			{
				info.spritesDied -= 2;
				TrySpawn(event.getEntity().level, event.getNewPos().getX(), event.getNewPos().getZ(), 1);
			}
		}
	}



	private static SpriteChunkInfo GetFromMap(int chunkX, int chunkZ)
	{
		long chunkKey = ((long)chunkX << 32) + chunkZ;
		return chunkMap.getOrDefault(chunkKey, null);
	}



	private static void PutToMap(SpriteChunkInfo record, int chunkX, int chunkZ)
	{
		long chunkKey = ((long)chunkX << 32) + chunkZ;
		chunkMap.put(chunkKey, record);
	}



	private static boolean PresentInMap(int chunkX, int chunkZ)
	{
		long chunkKey = ((long)chunkX << 32) + chunkZ;
		return chunkMap.containsKey(chunkKey);
	}



	private static final Map<Long, SpriteChunkInfo> chunkMap = new HashMap<>();
	private static class SpriteChunkInfo
	{
		boolean visited = false;
		int spritesDied = 0;
		int chunkX, chunkZ;
	}

	///--------------------------------------------------------------------------------------

	private static void TrySpawn(Level level, int newChunkX, int newChunkZ, int maxSprites)
	{
		if (! (level instanceof ServerLevel))
		{
			return;
		}
		float odds = 0.25f; // 25% by default
		odds *= OptionsHolder.COMMON.GunpowderRelatedLavaSpriteSpawnOddsMultiplier.get();
		if (level.random.nextFloat() >= odds)
		{
			return;
		}
		int x, z;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		FluidState fs;
		for (int loopX = 0; loopX < 4; loopX++)
		{
			for (int loopZ = 0; loopZ < 4; loopZ++)
			{
				x = (newChunkX << 4) + loopX * 4; // +0,+4,+8,+12
				z = (newChunkZ << 4) + loopZ * 4; // +0,+4,+8,+12

				// part 1: world bottom
				pos.set(x, -64+10-1, z);
				fs = level.getFluidState(pos);
				if (fs.is(FluidTags.LAVA) && fs.isSource())
				{
					boolean ok = true;
					for (int loopY = 1; loopY < 4; loopY++)
					{
						pos.set(x, 9 + loopY, z);
						if (! level.getBlockState(pos).isAir() && ! level.getFluidState(pos).is(FluidTags.LAVA))
						{
							ok = false;
						}
					}
					if (ok)
					{
						pos.set(x, 9, z);
						RegistrationManager.SPRITE.get().spawn((ServerLevel) level, null, null, pos, MobSpawnType.NATURAL, true, false);
						pos.set(x, 11, z);
						level.setBlockAndUpdate(pos, Blocks.EMERALD_BLOCK.defaultBlockState());
						//System.out.println("~~~ placed block at " + pos);
						maxSprites -= 1;
						if (maxSprites == 0)
						{
							return;
						}
					}
				}

				// part 2: surface
				int y = level.getChunk(newChunkX, newChunkZ).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
				pos.set(x, y, z);
				fs = level.getFluidState(pos);
				if (fs.is(FluidTags.LAVA) && fs.isSource())
				{
					pos.set(x, y + 3, z);
					//level.setBlockAndUpdate(pos, Blocks.EMERALD_BLOCK.defaultBlockState());
					level.setBlockAndUpdate(pos.above(), Blocks.GLOWSTONE.defaultBlockState());
					System.out.println("~~~ placed block at " + pos);
					pos.set(x, y, z);
					RegistrationManager.SPRITE.get().spawn((ServerLevel) level, null, null, pos, MobSpawnType.NATURAL, true, false);
					maxSprites -= 1;
					if (maxSprites == 0)
					{
						return;
					}
				}
			}
		}
	}
}
