package moonfather.playablepeaceful_items.gunpowder.items;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class LavaPoopItemEntity extends BatPoopItemEntity
{
	public LavaPoopItemEntity(EntityType<? extends ItemEntity> entityType, Level world)
	{
		super(entityType, world);
	}

	public LavaPoopItemEntity(Level world)
	{
		super(EntityType.ITEM, world);
	}

	public LavaPoopItemEntity(Level world, double x, double y, double z, ItemStack itemStack)
	{
		super(world, x, y, z, itemStack);
	}

	@Override
	protected Block getParentBlock()
	{
		return PeacefulMod.Blocks.LilypadBlock.get();
	}
}
