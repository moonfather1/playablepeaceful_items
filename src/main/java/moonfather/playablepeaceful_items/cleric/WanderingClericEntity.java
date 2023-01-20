package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class WanderingClericEntity extends WanderingTrader
{
	Donkey donkey;

	public WanderingClericEntity(Level world)
	{
		super(RegistrationManager.CLERIC.get(), world);
	}

	public WanderingClericEntity(EntityType<WanderingClericEntity> entityType, Level world)
	{
		super(entityType, world);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, (double)0.6F);
	}

	public static float GetScaleH() { return 0.6f; }
	public static float GetScaleV() { return 1.95f; }

	@Override
	public ItemStack getPickedResult(HitResult target)
	{
		return new ItemStack(RegistrationManager.CLERIC_SPAWN_EGG.get());
	}


	public void aiStep()         // despawning
	{
		super.aiStep();
		if (!this.level.isClientSide)
		{
			if (this.getDespawnDelay() > 0 && !this.isTrading())
			{
				this.setDespawnDelay(this.getDespawnDelay() - 1);
				if (this.getDespawnDelay() == 0)
				{
					if (this.donkey != null && this.donkey.isAlive() && this.donkey.getY() > 0)
					{
						this.donkey.dropLeash(true, false);
					}
					this.remove(RemovalReason.DISCARDED);
				}
			}
		}
	}



	@Override
	public void tick()
	{
		super.tick();
		if (this.isAlive() && this.tickCount > 200 && this.tickCount % 40 == 0 && this.position().y > 0)
		{
			if (this.donkey == null)
			{
				List<Donkey> list = this.level.getEntitiesOfClass(Donkey.class, this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
				if (list.size() > 0)
				{
					this.donkey = list.get(0);
				}
			}
			if (this.donkey == null || this.donkey.isDeadOrDying() || this.donkey.isRemoved())
			{
				DonkeyManagement.removeDroppedLeash(this);
				this.moveTo(this.position().x, -15d, this.position().z, 0f, 0f);
			}
		}
	}



	protected void updateTrades()
	{
		if (this.offers == null)
		{
			this.offers = new MerchantOffers();
		}
		this.offers.clear();
		WanderingClericTrades.getInstance().addToTrader(this, this.offers);
	}



	public void addOffersFromItemListings0(MerchantOffers passedOffers, VillagerTrades.ItemListing[] trades, int count)
	{
		this.addOffersFromItemListings(passedOffers, trades, count); // protected to public
	}
}
