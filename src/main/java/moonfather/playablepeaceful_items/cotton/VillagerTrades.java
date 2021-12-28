package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTrades
{
	@SubscribeEvent
	public static void onVillagerTrades(VillagerTradesEvent event)
	{
		if (OptionsHolder.COMMON.CottonSeedsFromVillager.get())
		{
			if (event.getType() == VillagerProfession.FARMER)
			{
				// adding cotton seeds
				event.getTrades().get(1).add(new BasicTrade(4, new ItemStack(PeacefulMod.CottonSeeds, 2), 4, 5));
			}
		}
	}
}
