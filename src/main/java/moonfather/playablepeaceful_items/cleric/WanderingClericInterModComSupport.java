package moonfather.playablepeaceful_items.cleric;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class WanderingClericInterModComSupport
{
	private static Random random = new Random();


	public void onInterModEnqueue(InterModEnqueueEvent event)
	{
		// -----------------------------------------------
		// seeds can be added to wandering cleric merchant.
		// (any item really, i don't check whether it can be planted.)
		// this item shares slot 8 with cotton seeds.
		// ------------------------------------------------------------------
		//InterModComms.sendTo("playablepeaceful_items", "seeds", () -> "minecraft:apple,3");      // values 1 to 5 range from very cheap to very expensive. 3 is medium.
		//InterModComms.sendTo("playablepeaceful_items", "seeds", () -> "minecraft:carrot,31");    // values 1 to 5 range from very cheap to very expensive. 31 is out of range but will be ok (price will be 64 shinies).
		//InterModComms.sendTo("playablepeaceful_items", "seeds", () -> "minecraft:paprika,1");    // non-existant item will be silently skipped, no worries.
		// -----------------------------------------------
		// music discs can be added to wandering cleric merchant.
		// (discs only)
		// this item shares slots 1-2 with other discs.
		// ------------------------------------------------------------------
		//InterModComms.sendTo("playablepeaceful_items", "disc-for-shiny", () -> "minecraft:carrot,31");  // will be skipped because it is not a disc.
		//InterModComms.sendTo("playablepeaceful_items", "disc-for-shiny", () -> "minecraft:music_disc_far,34");    // player will be able to buy "far" for 34 shinies (adjusted for percentage set in mod options).
		//InterModComms.sendTo("playablepeaceful_items", "disc-for-any-disc", () -> "minecraft:music_disc_wait");   // player will be able to trade "wait" one-for-one (random other disc).
		//InterModComms.sendTo("playablepeaceful_items", "disc-for-exact-disc", () -> "minecraft:music_disc_far,minecraft:music_disc_mall");  // player will be able to buy "far" disc if he offers "mall".
	}



	public static void onInterModProcess(InterModProcessEvent event)
	{
		event.getIMCStream().forEach(m ->
		{
			if (m.getMethod().equals("seeds"))
			{
				String[] itemRaw = m.getMessageSupplier().get().toString().split(",");
				int costLevel = 2;
				if (itemRaw.length >= 2)
				{
					costLevel = Integer.valueOf(itemRaw[1]);
				}
				RegisterThirdPartySeeds(itemRaw[0], costLevel);
			}
			else if (m.getMethod().equals("disc-for-shiny"))
			{
				String[] itemRaw = m.getMessageSupplier().get().toString().split(",");
				int costExact = 10;
				if (itemRaw.length >= 2)
				{
					costExact = Integer.valueOf(itemRaw[1]);
				}
				RegisterThirdPartyDiscForGold(itemRaw[0], costExact);
			}
			else if (m.getMethod().equals("disc-for-any-disc"))
			{
				String itemRaw = m.getMessageSupplier().get().toString();
				RegisterThirdPartyDiscForOtherDisc(itemRaw);
			}
			else if (m.getMethod().equals("disc-for-exact-disc"))
			{
				String[] itemRaw = m.getMessageSupplier().get().toString().split(",");
				int costExact = 10;
				if (itemRaw.length >= 2)
				{
					RegisterThirdPartyDiscForExactDisc(itemRaw[0], itemRaw[1]);
				}
			}
		});
	}



	private static void RegisterThirdPartySeeds(String item, int costLevel)
	{
		ResourceLocation rl = new ResourceLocation(item);
		if (!rl.getNamespace().equals("minecraft") && !ModList.get().isLoaded(rl.getNamespace()))
		{
			return;
		}
		Item itemResolved = ForgeRegistries.ITEMS.getValue(rl);
		if (itemResolved == null || itemResolved.equals(Items.AIR))
		{
			return;
		}
		costLevel = Math.min(costLevel, 6);
		int cost = 2 << costLevel;
		WanderingClericTrades.getInstance().Slot8CottonTrades.add(new BasicTrade(new ItemStack(Items.BREAD, cost), new ItemStack(itemResolved, 2), 2, 10, 1));
	}



	private static void RegisterThirdPartyDiscForGold(String item, int costExact)
	{
		ResourceLocation rl = new ResourceLocation(item);
		if (!rl.getNamespace().equals("minecraft") && !ModList.get().isLoaded(rl.getNamespace()))
		{
			return;
		}
		Item itemResolved = ForgeRegistries.ITEMS.getValue(rl);
		if (itemResolved == null || itemResolved.equals(Items.AIR))
		{
			return;
		}
		if (!(itemResolved instanceof MusicDiscItem))
		{
			return;
		}
		ItemStack costResolved = WanderingClericTrades.getInstance().getCost(costExact); // convert to configured item and percentage
		WanderingClericTrades.getInstance().MusicTrades.add(new BasicTrade(costResolved, new ItemStack(itemResolved), 1, 25, 1));
	}



	private static void RegisterThirdPartyDiscForOtherDisc(String item)
	{
		ResourceLocation rl = new ResourceLocation(item);
		if (!rl.getNamespace().equals("minecraft") && !ModList.get().isLoaded(rl.getNamespace()))
		{
			return;
		}
		Item itemResolved = ForgeRegistries.ITEMS.getValue(rl);
		if (itemResolved == null || itemResolved.equals(Items.AIR))
		{
			return;
		}
		if (!(itemResolved instanceof MusicDiscItem))
		{
			return;
		}
		int indexOther = random.nextInt(WanderingClericTrades.getInstance().MusicTrades.size());
		ItemStack other = WanderingClericTrades.getInstance().MusicTrades.get(indexOther).getOffer(null, null).getResult();
		indexOther = random.nextInt(WanderingClericTrades.getInstance().MusicTrades.size());
		ItemStack other2 = WanderingClericTrades.getInstance().MusicTrades.get(indexOther).getOffer(null, null).getResult();
		WanderingClericTrades.getInstance().MusicTrades.add(new BasicTrade(other, new ItemStack(itemResolved), 1, 25, 1));
		WanderingClericTrades.getInstance().MusicTrades.add(new BasicTrade(new ItemStack(itemResolved), other2, 1, 25, 1));
	}

	private static void RegisterThirdPartyDiscForExactDisc(String itemToBuy, String itemCost)
	{
		ResourceLocation rl = new ResourceLocation(itemToBuy);
		if (!rl.getNamespace().equals("minecraft") && !ModList.get().isLoaded(rl.getNamespace()))
		{
			return;
		}
		Item itemResolved = ForgeRegistries.ITEMS.getValue(rl);
		if (itemResolved == null || itemResolved.equals(Items.AIR))
		{
			return;
		}
		if (!(itemResolved instanceof MusicDiscItem))
		{
			return;
		}
		ResourceLocation rl2 = new ResourceLocation(itemCost);
		if (!rl2.getNamespace().equals("minecraft") && !ModList.get().isLoaded(rl2.getNamespace()))
		{
			return;
		}
		Item item2Resolved = ForgeRegistries.ITEMS.getValue(rl2);
		if (item2Resolved == null || item2Resolved.equals(Items.AIR))
		{
			return;
		}
		if (!(item2Resolved instanceof MusicDiscItem))
		{
			return;
		}
		WanderingClericTrades.getInstance().MusicTrades.add(new BasicTrade(new ItemStack(item2Resolved), new ItemStack(itemResolved), 1, 25, 1));
	}
}
