package moonfather.playablepeaceful_items.cleric;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class DonkeyManagement
{
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof Donkey)
		{
			if (event.getEntity().getTags().contains("TraderDonkey"))
			{
				Donkey donkey = (Donkey) event.getEntity();
				donkey.goalSelector.addGoal(3, new LinkDonkeyToOwner(donkey, null));
			}
		}
	}



	public static void tryToSpawnDonkeyFor(ServerLevel world, WanderingTrader trader, int maxHorDif)
	{
		BlockPos blockpos = findSpawnPositionNear(world, trader.blockPosition(), maxHorDif, trader);
		if (blockpos != null)
		{
			Donkey donkey = EntityType.DONKEY.spawn(world, null, (Component)null, (Player)null, blockpos, MobSpawnType.COMMAND, false, false);
			if (donkey != null)
			{
				donkey.setLeashedTo(trader, true);
				donkey.setChest(true);
				donkey.setOwnerUUID(trader.getUUID());
				donkey.setTamed(true);
				donkey.goalSelector.addGoal(3, new LinkDonkeyToOwner(donkey, trader));
				donkey.addTag("TraderDonkey");
				donkey.setDropChance(EquipmentSlot.CHEST, 0f);
			}
		}
	}



	@Nullable
	private static BlockPos findSpawnPositionNear(LevelReader world, BlockPos pos, int maxHorDif, WanderingTrader owner)
	{
		BlockPos blockpos = null;
		for(int i = 0; i < 12; ++i) {
			int j = pos.getX() + owner.getRandom().nextInt(maxHorDif * 2) - maxHorDif;
			int k = pos.getZ() + owner.getRandom().nextInt(maxHorDif * 2) - maxHorDif;
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


	public static void removeDroppedLeash(Entity survivor)
	{
		List<ItemEntity> list = survivor.level.getEntitiesOfClass(ItemEntity.class, survivor.getBoundingBox().inflate(3.0D, 3.0D, 3.0D));
		for (ItemEntity e : list)
		{
			if (e.getItem().getItem().equals(Items.LEAD))
			{
				e.remove(Entity.RemovalReason.DISCARDED);
			}
		}
		list.clear();
	}



	private static class LinkDonkeyToOwner extends Goal
	{
		Donkey donkey;
		WanderingTrader owner;

		public LinkDonkeyToOwner(Donkey donkey, WanderingTrader trader)
		{
			this.donkey = donkey;
			this.owner = trader;
		}

		@Override
		public boolean canUse()
		{
			return this.donkey.isAlive() && this.donkey.position().y > 0 && this.donkey.tickCount > 200 && (this.donkey.tickCount % 40 > 36 || this.donkey.tickCount % 40 == 0);
		}

		@Override
		public void tick()
		{
			if (this.donkey.isAlive() && this.donkey.position().y > 0 && this.donkey.tickCount > 200 && this.donkey.tickCount % 40 == 0)
			{
				if (this.owner == null)
				{
					List<WanderingClericEntity> list = this.donkey.level.getEntitiesOfClass(WanderingClericEntity.class, this.donkey.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
					if (list.size() > 0)
					{
						this.owner = list.get(0);
						list.clear();
					}
				}
				if (this.owner == null || this.owner.isDeadOrDying() || this.owner.isRemoved())
				{
					DonkeyManagement.removeDroppedLeash(this.donkey);
					this.donkey.moveTo(this.donkey.position().x, -15d, this.donkey.position().z, 0f, 0f);
					this.donkey.hurt(DamageSource.OUT_OF_WORLD, 123);
				}
				this.stop();
			}
		}
	}
}
