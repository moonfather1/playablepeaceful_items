package moonfather.playablepeaceful_items.gunpowder.sprite;

import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
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
			CompoundNBT nbt = event.getData().getCompound("PPI_sprites");
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
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt("sprites_died", info.spritesDied);
			nbt.putInt("x", info.chunkX);
			nbt.putInt("z", info.chunkZ);
			event.getData().put("PPI_sprites", nbt);
		}
	}



	@SubscribeEvent
	public static void onEntityEnteringChunk(EntityEvent.EnteringChunk event)
	{
		if (! OptionsHolder.COMMON.GunpowderRelatedLavaSpritesEnabled.get() || ! OptionsHolder.COMMON.GunpowderModuleEnabled.get() || OptionsHolder.COMMON.GunpowderRelatedLavaSpriteSpawnOddsMultiplier.get() == 0f)
		{
			return;
		}
		if (! event.getEntity().level.getDifficulty().equals(Difficulty.PEACEFUL) && OptionsHolder.COMMON.GunpowderModuleOnlyOnPeacefulDifficulty.get())
		{
			return;
		}
		if (event.getEntity().level.isClientSide() || ! (event.getEntity() instanceof PlayerEntity))
		{
			return;
		}
		SpriteChunkInfo info = GetFromMap(event.getNewChunkX(), event.getNewChunkZ());
		if (info == null)
		{
			TrySpawn(event.getEntity().level, event.getNewChunkX(), event.getNewChunkZ(), 4);
			info = new SpriteChunkInfo();
			info.chunkX = event.getNewChunkX();
			info.chunkZ = event.getNewChunkZ();
			info.visited = true;
			PutToMap(info, event.getNewChunkX(), event.getNewChunkZ());
		}
		else
		{
			if (info.spritesDied >= 2)
			{
				info.spritesDied -= 2;
				TrySpawn(event.getEntity().level, event.getNewChunkX(), event.getNewChunkZ(), 1);
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



	private static Map<Long, SpriteChunkInfo> chunkMap = new HashMap<>();
	private static class SpriteChunkInfo
	{
		boolean visited = false;
		int spritesDied = 0;
		int chunkX, chunkZ;
	}

	///--------------------------------------------------------------------------------------

	private static void TrySpawn(World level, int newChunkX, int newChunkZ, int maxSprites)
	{
		if (! (level instanceof ServerWorld))
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
		BlockPos.Mutable pos = new BlockPos.Mutable();
		FluidState fs;
		for (int loopX = 0; loopX < 4; loopX++)
		{
			for (int loopZ = 0; loopZ < 4; loopZ++)
			{
				x = (newChunkX << 4) + loopX * 4; // +0,+4,+8,+12
				z = (newChunkZ << 4) + loopZ * 4; // +0,+4,+8,+12

				// part 1: world bottom
				pos.set(x, 9, z);
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
						RegistrationManager.SPRITE_HOLDER.spawn((ServerWorld) level, null, null, pos, SpawnReason.NATURAL, true, false);
						//pos.set(x, 11, z);
						//level.setBlockAndUpdate(pos, Blocks.EMERALD_BLOCK.defaultBlockState());
						//System.out.println("~~~ placed block at " + pos);
						maxSprites -= 1;
						if (maxSprites == 0)
						{
							return;
						}
					}
				}

				// part 2: surface
				int y = level.getChunk(newChunkX, newChunkZ).getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
				pos.set(x, y, z);
				fs = level.getFluidState(pos);
				if (fs.is(FluidTags.LAVA) && fs.isSource())
				{
					//pos.set(x, y + 3, z);
					//level.setBlockAndUpdate(pos, Blocks.EMERALD_BLOCK.defaultBlockState());
					//level.setBlockAndUpdate(pos.above(), Blocks.GLOWSTONE.defaultBlockState());
					System.out.println("~~~ placed block at " + pos);
					pos.set(x, y, z);
					RegistrationManager.SPRITE_HOLDER.spawn((ServerWorld) level, null, null, pos, SpawnReason.NATURAL, true, false);
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
