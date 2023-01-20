package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class CottonSeedsItem extends Item
{
	public CottonSeedsItem()
	{
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		if (context.getClickedFace() == Direction.UP)
		{
			if (CottonSeedlingBlock.locationIsValidDirt(context.getLevel(), context.getClickedPos()))
			{
				if (CottonSeedlingBlock.locationHasAirClearance(context.getLevel(), context.getClickedPos().above(), true))
				{
					if (!context.getLevel().isClientSide)
					{
						context.getLevel().setBlockAndUpdate(context.getClickedPos().above(), PeacefulMod.Blocks.CottonSeedling.get().getStateForAge(0));
					}
					if (!context.getPlayer().isCreative())
					{
						stack.shrink(1);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}
}
