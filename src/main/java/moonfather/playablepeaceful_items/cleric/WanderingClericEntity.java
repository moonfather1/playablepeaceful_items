package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.BasicTrade;
import moonfather.playablepeaceful_items.slimeball.RegistrationManager;

import java.util.List;

public class WanderingClericEntity extends WanderingTraderEntity
{
	DonkeyEntity donkey;

	public WanderingClericEntity(World world)
	{
		super(RegistrationManager.CLERIC_HOLDER, world);
	}

	public WanderingClericEntity(EntityType<WanderingClericEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes()
	{
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, (double)0.6F);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(RegistrationManager.CLERIC_EGG_HOLDER);
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
					this.remove();
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
				List<DonkeyEntity> list = this.level.getEntitiesOfClass(DonkeyEntity.class, this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
				if (list.size() > 0)
				{
					this.donkey = list.get(0);
				}
			}
			if (this.donkey == null || this.donkey.isDeadOrDying() || this.donkey.removed)
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



	public void addOffersFromItemListings0(MerchantOffers passedOffers, VillagerTrades.ITrade[] trades, int count)
	{
		this.addOffersFromItemListings(passedOffers, trades, count); // protected to public
	}
}
