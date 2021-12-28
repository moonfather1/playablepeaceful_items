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
			if (this.donkey == null || this.donkey.isDeadOrDying())
			{
				this.moveTo(this.position().x, -15d, this.position().z, 0f, 0f);
			}
		}
	}

    private static VillagerTrades.ITrade[] MusicTrades =
		    {
				    new BasicTrade(new ItemStack(Items.GOLD_INGOT,  2), new ItemStack(Items.MUSIC_DISC_CAT), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.GOLD_INGOT,  3), new ItemStack(Items.MUSIC_DISC_CAT), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_CAT), new ItemStack(Items.MUSIC_DISC_BLOCKS), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_BLOCKS), new ItemStack(Items.MUSIC_DISC_CHIRP), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_CHIRP), new ItemStack(Items.MUSIC_DISC_FAR), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_FAR), new ItemStack(Items.MUSIC_DISC_11), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_11), new ItemStack(Items.MUSIC_DISC_MALL), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_MALL), new ItemStack(Items.MUSIC_DISC_MELLOHI), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_MELLOHI), new ItemStack(Items.MUSIC_DISC_CAT), 1, 15, 1),

				    new BasicTrade(new ItemStack(Items.GOLD_INGOT,  2), new ItemStack(Items.MUSIC_DISC_13), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.GOLD_INGOT,  3), new ItemStack(Items.MUSIC_DISC_13), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.MUSIC_DISC_PIGSTEP), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_PIGSTEP), new ItemStack(Items.MUSIC_DISC_STAL), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_STAL), new ItemStack(Items.MUSIC_DISC_STRAD), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_STRAD), new ItemStack(Items.MUSIC_DISC_WAIT), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_WAIT), new ItemStack(Items.MUSIC_DISC_WARD), 1, 15, 1),
				    new BasicTrade(new ItemStack(Items.MUSIC_DISC_WARD), new ItemStack(Items.MUSIC_DISC_13), 1, 15, 1)
		    };
	private static VillagerTrades.ITrade[] PotionTradesSlot3 =
			{
				new BasicTrade(new ItemStack(Items.GOLD_INGOT,  8), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER_BREATHING), 3, 10, 1), //2am
				new BasicTrade(new ItemStack(Items.GOLD_INGOT, 64), PotionUtils.setPotion(new ItemStack(Items.POTION, 3), Potions.AWKWARD), 2, 10, 1), //16am
				new BasicTrade(new ItemStack(Items.GOLD_INGOT, 24), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WATER_BREATHING), 3, 10, 1) //4am
			};
	private static VillagerTrades.ITrade[] PotionTradesSlot4 =
			{
					new BasicTrade(new ItemStack(Items.GOLD_INGOT,  8), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), 3, 10, 1), //2am
					new BasicTrade(new ItemStack(Items.GOLD_INGOT,  8), new ItemStack(Items.GUNPOWDER, 1), 6, 5, 1), //16am
					new BasicTrade(new ItemStack(Items.GOLD_INGOT, 12), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOW_FALLING), 3, 10, 1) //2am
			};
	private static VillagerTrades.ITrade[] HighEndTradesSlot5 = { new BasicTrade(new ItemStack(Items.GOLD_INGOT, 32), new ItemStack(Items.BLAZE_ROD), 3, 10, 1)}; //16am
	private static VillagerTrades.ITrade[] HighEndTradesSlot6 = { new BasicTrade(new ItemStack(Items.GOLD_INGOT, 32), new ItemStack(Items.ENDER_PEARL), 6, 10, 1)}; //4am

	private static VillagerTrades.ITrade[] VeganTradesSlot7 =
			{
					new BasicTrade(new ItemStack(Items.BREAD, 24), new ItemStack(Items.LEATHER, 8), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD, 24), new ItemStack(Items.LEATHER, 8), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD,  8), new ItemStack(Items.INK_SAC, 8), 2, 10, 1)
			}; //2am
	private static VillagerTrades.ITrade[] CottonTradesSlot8 =
			{
					new BasicTrade(new ItemStack(Items.BREAD, 2), new ItemStack(PeacefulMod.CottonSeeds, 4), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD, 3), new ItemStack(PeacefulMod.CottonSeeds, 4), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD, 2), new ItemStack(Items.PUMPKIN_SEEDS, 4), 2, 10, 1)
			}; //2am


	protected void updateTrades()
	{
		if (this.offers == null)
		{
			this.offers = new MerchantOffers();
		}
		this.offers.clear();

		// slots 1 & 2: music disks
		this.addOffersFromItemListings(this.offers, MusicTrades, 2);

		// slots 3 & 4: potions
		int roll = this.random.nextInt(4);
		this.offers.add(PotionTradesSlot3[roll % 3].getOffer(this, this.random));
		this.offers.add(PotionTradesSlot4[roll % 3].getOffer(this, this.random));

		// slots 5 & 6: ender pearls, etc...
		this.offers.add(HighEndTradesSlot5[0].getOffer(this, this.random));
		this.offers.add(HighEndTradesSlot6[0].getOffer(this, this.random));

		// slots 7: vegan stuff, etc.
		roll = this.random.nextInt(VeganTradesSlot7.length);
		this.offers.add(VeganTradesSlot7[roll].getOffer(this, this.random));

		// slots 8: seeds, etc.
		roll = this.random.nextInt(CottonTradesSlot8.length);
		this.offers.add(CottonTradesSlot8[roll].getOffer(this, this.random));
	}
}
