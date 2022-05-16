package moonfather.playablepeaceful_items.gunpowder.items;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LavaPoopItemEntity extends BatPoopItemEntity
{
	public LavaPoopItemEntity(EntityType<? extends ItemEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public LavaPoopItemEntity(World world)
	{
		super(EntityType.ITEM, world);
	}

	public LavaPoopItemEntity(World world, double x, double y, double z, ItemStack itemStack)
	{
		super(world, x, y, z, itemStack);
	}

	@Override
	protected Block getParentBlock()
	{
		return PeacefulMod.LilypadBlock;
	}
}
