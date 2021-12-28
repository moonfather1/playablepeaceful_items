package moonfather.playablepeaceful_items.cleric;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class DonkeyManagement
{
	@SubscribeEvent
	public static void onBiomeLoading(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof DonkeyEntity)
		{
			if (event.getEntity().getTags().contains("TraderDonkey"))
			{
				DonkeyEntity donkey = (DonkeyEntity) event.getEntity();
				donkey.goalSelector.addGoal(3, new LinkDonkeyToOwner(donkey, null));
			}
		}
	}

	public static void tryToSpawnDonkeyFor(ServerWorld world, WanderingTraderEntity trader, int maxHorDif)
	{
		BlockPos blockpos = findSpawnPositionNear(world, trader.blockPosition(), maxHorDif, trader);
		if (blockpos != null)
		{
			DonkeyEntity donkey = EntityType.DONKEY.spawn(world, null, (ITextComponent)null, (PlayerEntity)null, blockpos, SpawnReason.EVENT, false, false);
			if (donkey != null)
			{
				donkey.setLeashedTo(trader, true);
				donkey.setChest(true);
				donkey.setOwnerUUID(trader.getUUID());
				donkey.setTamed(true);
				donkey.goalSelector.addGoal(3, new LinkDonkeyToOwner(donkey, trader));
				donkey.addTag("TraderDonkey");
				donkey.setDropChance(EquipmentSlotType.CHEST, 0f);
			}
		}
	}

	@Nullable
	private static BlockPos findSpawnPositionNear(IWorldReader world, BlockPos pos, int maxHorDif, WanderingTraderEntity owner)
	{
		BlockPos blockpos = null;
		for(int i = 0; i < 12; ++i) {
			int j = pos.getX() + owner.getRandom().nextInt(maxHorDif * 2) - maxHorDif;
			int k = pos.getZ() + owner.getRandom().nextInt(maxHorDif * 2) - maxHorDif;
			int l = world.getHeight(Heightmap.Type.WORLD_SURFACE, j, k);
			BlockPos blockpos1 = new BlockPos(j, l, k);
			if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, world, blockpos1, EntityType.WANDERING_TRADER))
			{
				blockpos = blockpos1;
				break;
			}
		}
		return blockpos;
	}

	private static class LinkDonkeyToOwner extends Goal
	{
		DonkeyEntity donkey;
		WanderingTraderEntity owner;

		public LinkDonkeyToOwner(DonkeyEntity donkey, WanderingTraderEntity trader)
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
					}
				}
				if (this.owner == null || this.owner.isDeadOrDying())
				{
					this.donkey.moveTo(this.donkey.position().x, -15d, this.donkey.position().z, 0f, 0f);
				}
				this.stop();
			}
		}
	}
}
