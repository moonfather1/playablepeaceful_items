package moonfather.playablepeaceful_items.gunpowder.items;

import moonfather.playablepeaceful_items.shared.BasicItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BatPoopItem extends BasicItem
{
	public BatPoopItem()
	{
		super(false);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity defaultEntity, ItemStack itemStack)
	{
		Entity result = new BatPoopItemEntity(world, defaultEntity.getX(), defaultEntity.getY(), defaultEntity.getZ(), itemStack);
		result.setDeltaMovement(defaultEntity.getDeltaMovement());
		return result;
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world)
	{
		return 60 * 20; // 1 min instead of 5
	}
}
