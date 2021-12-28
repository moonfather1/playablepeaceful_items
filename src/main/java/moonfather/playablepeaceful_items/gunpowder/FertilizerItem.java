package moonfather.playablepeaceful_items.gunpowder;

import moonfather.playablepeaceful_items.cotton.CottonBushBlock;
import moonfather.playablepeaceful_items.items.BasicItem;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.ActionResultType;

public class FertilizerItem extends BasicItem
{
	public FertilizerItem()
	{
		super(new Item.Properties().tab(ItemGroup.TAB_MATERIALS).stacksTo(1));
	}



	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
		Block block = blockState.getBlock();
		boolean shouldGrow = false;  int delta = 1;
		IntegerProperty integerproperty = null;
		if (block instanceof CottonBushBlock)
		{
			int age = blockState.getValue(CropsBlock.AGE);
			if (age != 3 && age < 6)
			{
				shouldGrow = true;
				integerproperty = CropsBlock.AGE;
				delta = 1;
			}
			else if (age >= 6)
			{
				shouldGrow = true;
				integerproperty = CropsBlock.AGE;
				delta = -age;
			}
		}
		if (block instanceof CropsBlock)
		{
			CropsBlock cropsblock = (CropsBlock)block;
			if (!cropsblock.isMaxAge(blockState))
			{
				shouldGrow = true;
				integerproperty = cropsblock.getAgeProperty();
			}
		}
		else if (block instanceof StemBlock)
		{
			int age = blockState.getValue(StemBlock.AGE);
			if (age < 7)
			{
				shouldGrow = true;
				integerproperty = StemBlock.AGE;
			}
		}
		else if (block == Blocks.SWEET_BERRY_BUSH)
		{
			int age = blockState.getValue(SweetBerryBushBlock.AGE);
			if (age < 3)
			{
				shouldGrow = true;
				integerproperty = SweetBerryBushBlock.AGE;
			}
		}

		if (shouldGrow)
		{
			context.getLevel().levelEvent(2005, context.getClickedPos(), 0);
			context.getLevel().setBlockAndUpdate(context.getClickedPos(), blockState.setValue(integerproperty, Integer.valueOf(blockState.getValue(integerproperty) + delta)));
			stack.shrink(1);
			context.getPlayer().addItem(new ItemStack(Items.GLASS_BOTTLE));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
