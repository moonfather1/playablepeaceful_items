package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.cleric.storage.PPIWorldSavedData;
import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber
public class WanderingClericSpawning
{
	private static final int twoMinutes = 2 * 60 * 20;
	private static int basicTickDelay = twoMinutes; // 2min;  yes it's static, it's fine.
	private static int spawnDelay = -65432;
	private static SavedData storageData = null;


	@SubscribeEvent
	public static void WorldTick(TickEvent.WorldTickEvent event)
	{
		if (event.world.isClientSide() || event.world.getDifficulty() != Difficulty.PEACEFUL || event.phase == TickEvent.Phase.START)
		{
			return;
		}
		if (!event.world.dimensionType().natural())
		{
			return;
		}
		if (basicTickDelay-- > 0)
		{
			return;
		}
		basicTickDelay = twoMinutes;
		if (!event.world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING))
		{
			return;
		}

		//System.out.println("!!!!!!!!!!!!-!---  passed basic ticks");
		verifyWorldSavedDataIsInitialized(event.world);
		if (spawnDelay < -10000)
		{
			spawnDelay = getDelayBetweenTwoSpawns(event.world); //2.5 ingame days
		}
		spawnDelay -= basicTickDelay;
		if (spawnDelay <= 0)
		{
			if (trySpawn((ServerLevel) event.world))
			{
				spawnDelay = getDelayBetweenTwoSpawns(event.world);
			}
		}
		//else System.out.println("!!!!!!!!!!!!!---  spawnDelay " + spawnDelay + ">0, wait " + spawnDelay / 20 / 60 + "min," + spawnDelay / 20 % 60 + "sec");
		storageData.setDirty();
	}



	private static boolean trySpawn(ServerLevel world)
	{
		Player player = getRandomPlayerEx(world);
		if (player == null)
		{
			//System.out.println("!!!!!!!!!!!!!  return true, player is null");
			return true;
		}
		else if (world.random.nextInt(100) < 20) //20% chance to just spawn nothing, return success and skip 2 days.    will consider.
		{
			//System.out.println("!!!!!!!!!!!!!  return false, chance");
			return true;
		}
		else
		{
			BlockPos playerPos = player.blockPosition();
			PoiManager poiManager = world.getPoiManager();
			Optional<BlockPos> pos1 = poiManager.find(PoiType.MEETING.getPredicate(), (p_221241_0_) -> { return true; }, playerPos, 48, PoiManager.Occupancy.ANY);
			BlockPos pos2 = (BlockPos)pos1.orElse(playerPos);
			BlockPos pos3 = findSpawnPositionNear(world, pos2, 48, world.random);
			if (pos3 != null && hasEnoughSpace(world, pos3))
			{
				WanderingClericEntity cleric = (WanderingClericEntity) RegistrationManager.CLERIC.get().spawn(world, (CompoundTag)null, (Component)null, (Player)null, pos3, MobSpawnType.EVENT, false, false);
				if (cleric != null)
				{
					DonkeyManagement.tryToSpawnDonkeyFor(world, cleric, 4);
					cleric.setDespawnDelay(48000);
					cleric.setWanderTarget(pos2);
					cleric.restrictTo(pos2, 32); // was 16, reconsider
//					System.out.println("!!!!!!!!!!!!!  spawned at " + pos2 + " remember to return time to 48000");
					return true;
				}
			}

//			System.out.println("!!!!!!!!!!!!!  return false, final");
			return false;
		}
	}



	@Nullable
	public static BlockPos findSpawnPositionNear(LevelReader world, BlockPos pos, int maxHorDif, Random random)
	{
		BlockPos blockpos = null;
		for(int i = 0; i < 12; ++i)
		{
			int j = pos.getX() + random.nextInt(maxHorDif * 2) - maxHorDif;
			int k = pos.getZ() + random.nextInt(maxHorDif * 2) - maxHorDif;
			int l = world.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
			BlockPos blockpos1 = new BlockPos(j, l, k);
			if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, world, blockpos1, EntityType.WANDERING_TRADER))
			{
				blockpos = blockpos1;
				break;
			}
		}
		return blockpos;
	}



	public static boolean hasEnoughSpace(BlockGetter reader, BlockPos pos)
	{
		Iterator iterator = BlockPos.betweenClosed(pos, pos.offset(1, 2, 1)).iterator();

		BlockPos currentPos;
		do
		{
			if (!iterator.hasNext())
			{
				return true;
			}
			currentPos = (BlockPos)iterator.next();
		} while(reader.getBlockState(currentPos).getCollisionShape(reader, currentPos).isEmpty());

		return false;
	}



	private static int getDelayBetweenTwoSpawns(Level world)
	{
		int delay = 60000; //2.5 ingame days
		delay = (int) Math.round(delay * OptionsHolder.COMMON.ClericAppearanceDelayMultiplier.get()); // optional multiplier
		int playerCountNormal = 0;
		for (Player player : world.players())
		{
			if (!player.isCreative() && !player.isSpectator())
			{
				playerCountNormal += 1;
			}
		}
		if (playerCountNormal == 1)
		{
			delay *= 2; // double if singleplayer
		}
		return delay;
	}



	private static Player getRandomPlayerEx(ServerLevel world)
	{
		List<Player> list = new ArrayList<Player>(10);
		for (ServerPlayer player : world.players())
		{
			if (!player.isSpectator() && !player.isCreative())
			{
				list.add(player);
			}
		}
		if (!list.isEmpty())
		{
			Player result = list.get(world.random.nextInt(list.size()));
			list.clear();
			return result;
		}
		for (ServerPlayer player : world.players())
		{
			if (!player.isSpectator())
			{
				list.add(player);
			}
		}
		if (!list.isEmpty())
		{
			Player result = list.get(world.random.nextInt(list.size()));
			list.clear();
			return result;
		}
		return null;
	}




	// used by WorldSavedData class
	public static void onWorldLoading(String id, int remainingSpawnDelay)
	{
		//System.out.println("    !!!!!!!!!!!!!!!    !!!!!!!!     !!!!!!   onWorldLoading(" + id + ")   returned " + remainingSpawnDelay / 20 / 60 + "min," + remainingSpawnDelay / 20 % 60 + "sec");
		spawnDelay = remainingSpawnDelay;
	}

	// used by WorldSavedData class
	public static int getRemainingSpawnDelay()
	{
		if (spawnDelay > -10000)
		{
			//System.out.println("    !!!!!!!!!!!!!!!    !!!!!!!!     !!!!!!   getRemainingSpawnDelay(" + "id" + ")   returned " + spawnDelay);
			return spawnDelay;
		}
		else
		{
			//System.out.println("    !!!!!!!!!!!!!!!    !!!!!!!!     !!!!!!   getRemainingSpawnDelay(" + "id" + ")   failed!");
			return 60001;
		}
	}

	// used to connect to WorldSavedData class
	private static void verifyWorldSavedDataIsInitialized(Level world)
	{
		if (storageData != null)
		{
			return;
		}
		if (world instanceof ServerLevel)
		{
			String id = "PPI";
			storageData = ((ServerLevel) world).getServer().overworld().getDataStorage().computeIfAbsent(PPIWorldSavedData::new, PPIWorldSavedData::new, id);
			//System.out.println("    !!!!!!!!!!!!!!!    !!!!!!!!     !!!!!!   verifyWorldSavedDataIsInitialized(" + storageData.getId() + ")  ");
		}
	}
}
