package moonfather.playablepeaceful_items.gunpowder.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BatPoopItem extends Item
{
	public BatPoopItem()
	{
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

	@Override
	public Entity createEntity(Level world, Entity defaultEntity, ItemStack itemStack)
	{
		Entity result = new BatPoopItemEntity(world, defaultEntity.getX(), defaultEntity.getY(), defaultEntity.getZ(), itemStack);
		result.setDeltaMovement(defaultEntity.getDeltaMovement());
		return result;
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, Level world)
	{
		return 60 * 20; // 1 min instead of 5
	}
}
