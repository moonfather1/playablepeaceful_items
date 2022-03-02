package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.shared.BasicItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

public class CottonSeedsItem extends BasicItem
{
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		if (context.getClickedFace() == Direction.UP)
		{
			if (CottonSeedlingBlock.locationIsValidDirt(context.getLevel(), context.getClickedPos()))
			{
				if (CottonSeedlingBlock.locationHasAirClearance(context.getLevel(), context.getClickedPos().above(), true))
				{
					if (!context.getLevel().isClientSide)
					{
						context.getLevel().setBlockAndUpdate(context.getClickedPos().above(), PeacefulMod.CottonSeedling.getStateForAge(0));
					}
					if (!context.getPlayer().isCreative())
					{
						stack.shrink(1);
					}
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}
}
