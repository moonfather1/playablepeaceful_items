package moonfather.playablepeaceful_items.gunpowder.items;

import moonfather.playablepeaceful_items.cotton.CottonBushBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FertilizerItem extends Item
{
	public FertilizerItem()
	{
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).stacksTo(1));
	}



	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
		Block block = blockState.getBlock();
		boolean shouldGrow = false;  int delta = 1;
		IntegerProperty integerproperty = null;
		if (block instanceof CottonBushBlock)
		{
			int age = blockState.getValue(CropBlock.AGE);
			if (age != 3 && age < 6)
			{
				shouldGrow = true;
				integerproperty = CropBlock.AGE;
				delta = 1;
			}
			else if (age >= 6)
			{
				shouldGrow = true;
				integerproperty = CropBlock.AGE;
				delta = -age;
			}
		}
		if (block instanceof CropBlock crop)
		{
			if (!crop.isMaxAge(blockState))
			{
				shouldGrow = true;
				integerproperty = crop.getAgeProperty();
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
			if (! context.getPlayer().isCreative())
			{
				stack.shrink(1);
				context.getPlayer().addItem(new ItemStack(Items.GLASS_BOTTLE));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
