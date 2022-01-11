package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffers;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.BasicTrade;

import java.util.Random;

public class WanderingClericTrades
{
	private VillagerTrades.ITrade[] MusicTrades =
			{
					new BasicTrade(getCost(2), new ItemStack(Items.MUSIC_DISC_CAT), 1, 15, 1),
					new BasicTrade(getCost(3), new ItemStack(Items.MUSIC_DISC_CAT), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_CAT), new ItemStack(Items.MUSIC_DISC_BLOCKS), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_BLOCKS), new ItemStack(Items.MUSIC_DISC_CHIRP), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_CHIRP), new ItemStack(Items.MUSIC_DISC_FAR), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_FAR), new ItemStack(Items.MUSIC_DISC_11), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_11), new ItemStack(Items.MUSIC_DISC_MALL), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_MALL), new ItemStack(Items.MUSIC_DISC_MELLOHI), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_MELLOHI), new ItemStack(Items.MUSIC_DISC_CAT), 1, 15, 1),

					new BasicTrade(getCost(2), new ItemStack(Items.MUSIC_DISC_13), 1, 15, 1),
					new BasicTrade(getCost(3), new ItemStack(Items.MUSIC_DISC_13), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.MUSIC_DISC_PIGSTEP), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_PIGSTEP), new ItemStack(Items.MUSIC_DISC_STAL), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_STAL), new ItemStack(Items.MUSIC_DISC_STRAD), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_STRAD), new ItemStack(Items.MUSIC_DISC_WAIT), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_WAIT), new ItemStack(Items.MUSIC_DISC_WARD), 1, 15, 1),
					new BasicTrade(new ItemStack(Items.MUSIC_DISC_WARD), new ItemStack(Items.MUSIC_DISC_13), 1, 15, 1)
			};
	private VillagerTrades.ITrade[] PotionTradesSlot3 =
			{
					new BasicTrade(getCost(8), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER_BREATHING), 3, 10, 1), //2am
					new BasicTrade(getCost(64), PotionUtils.setPotion(new ItemStack(Items.POTION, 3), Potions.AWKWARD), 2, 10, 1), //16am
					new BasicTrade(getCost(24), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WATER_BREATHING), 3, 10, 1) //4am
			};
	private VillagerTrades.ITrade[] PotionTradesSlot4 =
			{
					new BasicTrade(getCost(8), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), 3, 10, 1), //2am
					new BasicTrade(getCost(8), new ItemStack(Items.GUNPOWDER, 1), 6, 5, 1), //16am
					new BasicTrade(getCost(12), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOW_FALLING), 3, 10, 1) //2am
			};
	private VillagerTrades.ITrade[] HighEndTradesSlot5 = { new BasicTrade(getCost(32), new ItemStack(Items.BLAZE_ROD), 3, 10, 1)}; //16am
	private VillagerTrades.ITrade[] HighEndTradesSlot6 = { new BasicTrade(getCost(32), new ItemStack(Items.ENDER_PEARL), 6, 10, 1)}; //4am

	private VillagerTrades.ITrade[] VeganTradesSlot7 =
			{
					new BasicTrade(new ItemStack(Items.BREAD, 24), new ItemStack(Items.LEATHER, 8), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD, 24), new ItemStack(Items.LEATHER, 8), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD,  8), new ItemStack(Items.INK_SAC, 8), 2, 10, 1)
			}; //2am
	private VillagerTrades.ITrade[] CottonTradesSlot8 =
			{
					new BasicTrade(new ItemStack(Items.BREAD, 2), new ItemStack(PeacefulMod.CottonSeeds, 4), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD, 3), new ItemStack(PeacefulMod.CottonSeeds, 4), 2, 10, 1),
					new BasicTrade(new ItemStack(Items.BREAD, 2), new ItemStack(Items.PUMPKIN_SEEDS, 4), 2, 10, 1)
			}; //2am
	private Random random = new Random();
	private Item currencyItem = null;



	private WanderingClericTrades()
	{
	}



	private static WanderingClericTrades instance = null;
	public static WanderingClericTrades getInstance()
	{
		if (instance == null)
		{
			instance = new WanderingClericTrades();
		}
		return instance;
	}



	private ItemStack getCost(int baseCost)
	{
		int cost = (int) Math.ceil(OptionsHolder.COMMON.ClericCurrencyMultiplier.get() * baseCost);
		cost = Math.min(cost, 64);
		return new ItemStack(this.getCostItem(), cost);
	}



	private Item getCostItem()
	{
		if (this.currencyItem == null)
		{
			this.currencyItem = Registry.ITEM.get(new ResourceLocation(OptionsHolder.COMMON.ClericCurrencyItem.get()));
			if (this.currencyItem.equals(Items.AIR))
			{
				this.currencyItem = Items.GOLD_INGOT;
			}
		}
		return this.currencyItem;
	}



	public void addToTrader(WanderingClericEntity trader, MerchantOffers offers)
	{
		// slots 1 & 2: music disks
		trader.addOffersFromItemListings0(offers, MusicTrades, 2);

		// slots 3 & 4: potions
		int roll = this.random.nextInt(4);
		offers.add(PotionTradesSlot3[roll % 3].getOffer(trader, this.random));
		offers.add(PotionTradesSlot4[roll % 3].getOffer(trader, this.random));

		// slots 5 & 6: ender pearls, etc...
		offers.add(HighEndTradesSlot5[0].getOffer(trader, this.random));
		offers.add(HighEndTradesSlot6[0].getOffer(trader, this.random));

		// slots 7: vegan stuff, etc.
		roll = this.random.nextInt(VeganTradesSlot7.length);
		offers.add(VeganTradesSlot7[roll].getOffer(trader, this.random));

		// slots 8: seeds, etc.
		roll = this.random.nextInt(CottonTradesSlot8.length);
		offers.add(CottonTradesSlot8[roll].getOffer(trader, this.random));
	}
}
